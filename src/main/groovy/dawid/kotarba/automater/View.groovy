package dawid.kotarba.automater

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.progressbar.ProgressBar
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.router.Route
import dawid.kotarba.automater.device.Mouse
import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import org.springframework.core.io.ClassPathResource

import java.util.stream.Collectors

@Route
@Push
class View extends VerticalLayout {

    private Thread componentsThread

    def mouseCoords = new Label()
    def progressBar = new ProgressBar()
    def startButton = new Button("Start", new Icon(VaadinIcon.PLAY))
    def stopButton = new Button("Stop", new Icon(VaadinIcon.STOP))
    def mouseCoordsButton = new Button("Capture mouse coords", new Icon(VaadinIcon.CURSOR))
    def shallCaptureMouseCoordinates = false

    View() {
        alignItems = Alignment.CENTER

        def pageLayout = new VerticalLayout()
        pageLayout.alignItems = Alignment.CENTER
        pageLayout.width = "500px"

        def executor = Beans.planExecutor

        def planExecutionArea = new TextArea()
        def testPlan = new ClassPathResource('plans/ExamplePlan.txt')
        def testPlanText = testPlan.file.readLines().stream().collect(Collectors.joining('\n'))
        planExecutionArea.value = testPlanText
        planExecutionArea.width = "500px"

        updateStartButton(planExecutionArea.value, executor)
        updateStopButton(executor)
        updateMouseCoordsButton()

        def planExecutionLayout = new VerticalLayout(
                new Label("Execute a Plan:"),
                planExecutionArea,
        )
        planExecutionLayout.alignItems = Alignment.CENTER

        pageLayout.add(
                new H1("Automater"),
                planExecutionLayout,
                progressBar,
                mouseCoords,
                new HorizontalLayout(
                        startButton,
                        stopButton,
                        mouseCoordsButton
                ),
        )

        add(pageLayout)
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        componentsThread = new Thread(new ComponentsRunnable(attachEvent.UI))
        componentsThread.start()
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        componentsThread.interrupt()
        componentsThread = null
    }

    private updateStartButton(String executionLines, PlanExecutor executor) {
        startButton.addClickListener({
            new Thread(new Runnable() {
                @Override
                void run() {
                    def plan = new Plan("Plan from text area", executionLines)
                    executor.start(plan)
                }
            }).start()
        })
    }

    private updateStopButton(PlanExecutor executor) {
        stopButton.addClickShortcut(Key.ESCAPE)
        stopButton.addClickListener({
            executor.stop()
            new Notification("Execution stopped", 3000).open()
        })
    }

    private updateMouseCoordsButton() {
        mouseCoordsButton.addClickListener({
            shallCaptureMouseCoordinates = !shallCaptureMouseCoordinates
        })
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
                mouseCoords.text = ""
            }
        }

        private void updateStartButton(PlanExecutor executor) {
            startButton.enabled = !executor.started
        }

        private void updateStopButton(PlanExecutor executor) {
            stopButton.enabled = executor.started
        }

        private void updateProgressBar(PlanExecutor executor) {
            if (!executor.started) {
                progressBar.indeterminate = false
                progressBar.value = 1
            } else if (executor.loopPlan) {
                progressBar.indeterminate = true
            } else {
                progressBar.value = executor.planProgress
            }
        }
    }
}
