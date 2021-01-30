package dawid.kotarba.automater.view

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.accordion.Accordion
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.progressbar.ProgressBar
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.router.Route
import dawid.kotarba.automater.Beans
import dawid.kotarba.automater.device.Mouse
import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import dawid.kotarba.automater.executor.Steps
import org.springframework.core.io.ClassPathResource

import java.util.stream.Collectors

import static com.vaadin.flow.component.icon.VaadinIcon.*

@Route
@Push
@CssImport('./styles/styles.css')
class View extends VerticalLayout {

    private Thread componentsThread

    def planExecutionArea = new TextArea("Execute a Plan:")
    def sleepBetweenStepsField = new TextField("Sleep time between steps:")
    def mouseCoords = new Label()
    def progressBar = new ProgressBar()
    def progressLabel = new Label("Progress: 0%")
    def startButton = new Button("Start", new Icon(PLAY))
    def stopButton = new Button("Stop [Esc]", new Icon(STOP))
    def mouseCoordsButton = new Button("Capture mouse coords", new Icon(CURSOR))
    def shallCaptureMouseCoordinates = false
    def planRun = false

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        alignItems = Alignment.CENTER
        def pageLayout = new VerticalLayout()
        pageLayout.className = "page"
        pageLayout.alignItems = Alignment.CENTER

        def executor = Beans.planExecutor

        planExecutionArea.className = "planExecutionArea"
        def testPlan = new ClassPathResource('plans/ExamplePlan.txt')
        def testPlanText = getTestPlanExecutionLines(testPlan)
        planExecutionArea.value = testPlanText

        sleepBetweenStepsField.value = "100"

        updateStartButton(attachEvent.UI, planExecutionArea, executor)
        updateStopButton(executor)
        updateMouseCoordsButton()

        def planExecutionLayout = new VerticalLayout(
                planExecutionArea,
                new HorizontalLayout(
                        sleepBetweenStepsField,
                        getPlanUpload()
                )
        )

        pageLayout.add(
                new H1("Automater"),
                planExecutionLayout,
                progressLabel,
                progressBar,
                mouseCoords,
                new HorizontalLayout(
                        startButton,
                        stopButton,
                        mouseCoordsButton
                ),
                getStepsDocumentation()
        )

        add(pageLayout)

        componentsThread = new Thread(new ComponentsRunnable(attachEvent.UI))
        componentsThread.start()
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        componentsThread.interrupt()
        componentsThread = null
    }

    private updateStartButton(UI ui, TextArea planExecutionArea, PlanExecutor executor) {
        startButton.addClickListener({
            new Thread(new Runnable() {
                @Override
                void run() {
                    try {
                        def plan = new Plan(planExecutionArea.value, Integer.parseInt(sleepBetweenStepsField.value))
                        planRun = true
                        executor.start(plan)
                    } catch (Exception e) {
                        ui.access {
                            Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE)
                            executor.stop()
                        }
                    }
                }
            }).start()
        })
    }

    private updateStopButton(PlanExecutor executor) {
        stopButton.addClickShortcut(Key.ESCAPE)
        stopButton.addClickListener({
            executor.stop()
            Notification.show("Execution stopped", 1000, Notification.Position.MIDDLE)
        })
    }

    private updateMouseCoordsButton() {
        mouseCoordsButton.addClickListener({
            shallCaptureMouseCoordinates = !shallCaptureMouseCoordinates
        })
    }

    private String getTestPlanExecutionLines(ClassPathResource testPlan) {
        testPlan.file.readLines().stream().collect(Collectors.joining('\n'))
    }

    private class ComponentsRunnable implements Runnable {
        private final UI ui

        ComponentsRunnable(UI ui) {
            this.ui = ui
        }

        @Override
        void run() {
            def mouse = Beans.mouse
            def executor = Beans.planExecutor
            while (true) {
                sleep(200)
                ui.access {
                    updateMouseCoordinates(mouse)
                    updateProgressBar(executor)
                    updateStartButton(executor)
                    updateStopButton(executor)
                }
            }
        }

        private void updateMouseCoordinates(Mouse mouse) {
            if (shallCaptureMouseCoordinates) {
                mouseCoords.text = "Mouse coordinates [ X: ${mouse.x}, Y: ${mouse.y} ]"
            } else {
                mouseCoords.text = ''
            }
        }

        private void updateStartButton(PlanExecutor executor) {
            startButton.enabled = !executor.started
        }

        private void updateStopButton(PlanExecutor executor) {
            stopButton.enabled = executor.started
        }

        private void updateProgressBar(PlanExecutor executor) {
            if (planRun) {
                if (!executor.started) {
                    progressBar.indeterminate = false
                    progressBar.value = 1
                    progressLabel.text = "Progress: 100%"
                } else if (executor.loopPlan) {
                    progressBar.indeterminate = true
                    progressLabel.text = "Progress: ..."
                } else {
                    progressBar.value = executor.planProgress
                    progressLabel.text = "Progress: ${progressBar.value * 100 as int}%"
                }
            }
        }
    }

    private Component getPlanUpload() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer)
        upload.uploadButton = new Button("Upload plan...")
        upload.dropLabel = new Label("Drop a file with plan here")
        upload.addSucceededListener({
            def executionPlan = buffer.inputStream.readLines().stream().collect(Collectors.joining('\n'))
            planExecutionArea.value = executionPlan
        })

        return upload
    }

    private Component getStepsDocumentation() {
        Accordion accordion = new Accordion()

        def layout = new VerticalLayout()
        Steps.steps.values().toList().forEach({ description ->
            def addToPlanButton = new Button("Add")
            addToPlanButton.addClickListener({
                planExecutionArea.value = "${planExecutionArea.value}\n${description.lineExample}"
            })

            def lineExampleLabel = new Label("${description.lineExample}: ")
            lineExampleLabel.className = 'lineExampleLabel'
            def usageLabel = new Label("${description.usage}")
            def labels = new HorizontalLayout(addToPlanButton, lineExampleLabel, usageLabel)
            layout.add(labels)
        })
        accordion.add("Example steps", layout)
    }
}
