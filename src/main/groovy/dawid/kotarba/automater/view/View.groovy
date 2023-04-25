package dawid.kotarba.automater.view

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.accordion.Accordion
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H2
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
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.VaadinSessionScope
import dawid.kotarba.automater.device.Mouse
import dawid.kotarba.automater.service.converter.PlanToJsonConverter
import dawid.kotarba.automater.service.executor.Plan
import dawid.kotarba.automater.service.executor.PlanExecutor
import dawid.kotarba.automater.service.executor.Steps
import dawid.kotarba.automater.util.ClassPathReader

import java.util.stream.Collectors

import static com.vaadin.flow.component.icon.VaadinIcon.CURSOR
import static com.vaadin.flow.component.icon.VaadinIcon.PLAY
import static com.vaadin.flow.component.icon.VaadinIcon.STOP

@Route
@Push
@CssImport('./styles/styles.css')
@PageTitle('automater')
@VaadinSessionScope
class View extends VerticalLayout {

    private UI ui
    private PlanExecutor executor
    private Mouse mouse
    private def componentsThread
    private PlanToJsonConverter planToJsonConverter = new PlanToJsonConverter()

    private TextArea planExecutionArea = new TextArea('Execute a Plan:')
    private TextArea planAsJsonArea = new TextArea('Pan as JSON:')

    private TextField sleepBetweenStepsField = new TextField('Sleep time between steps:')
    private TextField executionTimeField = new TextField('Execution time (in sec):')

    private ProgressBar progressBar = new ProgressBar()

    private Button startButton = new Button('Start [F2]', new Icon(PLAY))
    private Button stopButton = new Button('Stop [Esc]', new Icon(STOP))
    private Button captureMouseButton = new Button('Capture coords [F4]', new Icon(CURSOR))
    private Button captureAddMouseButton = new Button('Capture and add coords [F8]', new Icon(CURSOR))

    private Label planExecutionStatistics = new Label('')
    private Label mouseCoordsCurrent = new Label()
    private Label mouseCoordsCaptured = new Label()
    private Label progressLabel = new Label('Progress: 0%')


    private boolean shallCaptureMouseCoordinates
    private def planStarted

    View(PlanExecutor executor, Mouse mouse) {
        this.executor = executor
        this.mouse = mouse
        this.planStarted = executor.isStarted()
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ui = attachEvent.getUI()
        alignItems = Alignment.CENTER
        def pageLayout = new VerticalLayout()
        pageLayout.className = 'page'
        pageLayout.alignItems = Alignment.CENTER

        mouseCoordsCaptured.className = 'mouseCoordsCaptured'

        setupPlanExecutionArea()
        setupSleepBetweenStepsField()
        setupExecutionTime()
        setupStartButton()
        setupStopButton()
        setupCaptureMouseCoordsButton()
        setupCaptureAddMouseCoordsButton()

        def planExecutionLayout = new VerticalLayout(
                planExecutionArea,
                new HorizontalLayout(
                        sleepBetweenStepsField,
                        executionTimeField,
                        getPlanUpload()
                )
        )

        def accordionSection = new VerticalLayout(getMouseCaptureComponent(), getStepsDocumentation(), getJsonPlanArea())
        accordionSection.setHorizontalComponentAlignment(Alignment.START)

        pageLayout.add(
                new H2('automater'),
                planExecutionLayout,
                new HorizontalLayout(progressLabel, planExecutionStatistics),
                progressBar,
                new HorizontalLayout(
                        startButton,
                        stopButton
                ),
                accordionSection
        )

        add(pageLayout)

        componentsThread = new Thread(new ComponentsRunnable())
        componentsThread.start()
    }

    private setupPlanExecutionArea() {
        planExecutionArea.className = 'planExecutionArea'
        def testPlanText = ClassPathReader.readAsString('plans/ExamplePlan.txt')
        planExecutionArea.value = testPlanText
        planExecutionArea.addValueChangeListener {
            updatePlanAsJsonArea()
        }
    }

    private void setupSleepBetweenStepsField() {
        def defaultSleepTime = '100'
        sleepBetweenStepsField.value = defaultSleepTime
        sleepBetweenStepsField.addValueChangeListener({
            if (!sleepBetweenStepsField.value.matches('\\d+')) {
                sleepBetweenStepsField.value = defaultSleepTime
            } else if (Integer.parseInt(sleepBetweenStepsField.value) < 0) {
                sleepBetweenStepsField.value = defaultSleepTime
            }
            updatePlanAsJsonArea()
        })
    }

    private void setupExecutionTime() {
        def defaultExecutionTime = '3600'
        executionTimeField.value = defaultExecutionTime
        executionTimeField.addValueChangeListener({
            if (!executionTimeField.value.matches('\\d+')) {
                executionTimeField.value = defaultExecutionTime
            } else if (Integer.parseInt(executionTimeField.value) < 0) {
                executionTimeField.value = defaultExecutionTime
            }
            updatePlanAsJsonArea()
        })
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        componentsThread.interrupt()
        componentsThread = null
    }

    private void setupStartButton() {
        startButton.addClickShortcut(Key.F2)
        startButton.addClickListener {
            new Thread(new Runnable() {
                @Override
                void run() {
                    try {
                        ui.access {
                            planExecutionStatistics.text = ''
                        }

                        def plan = new Plan()
                                .withExecutionPlan(planExecutionArea.value)
                                .withSleepBetweenSteps(Integer.parseInt(sleepBetweenStepsField.value))
                                .withMaxExecutionTime(Integer.parseInt(executionTimeField.value))
                        planStarted = true
                        def statistics = executor.start(plan)
                        ui.access {
                            planExecutionStatistics.text = "(${statistics.stepsExecuted} steps executed in ${statistics.executionTime} ms)"
                        }
                    } catch (Exception e) {
                        e.printStackTrace()
                        ui.access {
                            Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE)
                        }
                        executor.stop()
                    }
                }
            }).start()
        }
    }

    private void setupStopButton() {
        stopButton.addClickShortcut(Key.ESCAPE)
        stopButton.addClickListener {
            executor.stop()
            Notification.show('Execution stopped', 1000, Notification.Position.MIDDLE)
        }
    }

    private void setupCaptureMouseCoordsButton() {
        captureMouseButton.addClickShortcut(Key.F4)
        captureMouseButton.addClickListener {
            mouseCoordsCaptured.text = "MOUSE moveTo ${mouse.x} ${mouse.y}"
        }
    }

    private void setupCaptureAddMouseCoordsButton() {
        captureAddMouseButton.addClickShortcut(Key.F8)
        captureAddMouseButton.addClickListener {
            mouseCoordsCaptured.text = "MOUSE moveTo ${mouse.x} ${mouse.y}"
            planExecutionArea.value = "${planExecutionArea.value}\n${mouseCoordsCaptured.text}"
        }
    }

    private class ComponentsRunnable implements Runnable {
        @Override
        void run() {
            while (true) {
                sleep(200)
                ui.access {
                    updateMouseCoordinates()
                    updateProgressBar()
                    updateStartButton()
                    updateStopButton()
                }
            }
        }

        private void updateMouseCoordinates() {
            if (shallCaptureMouseCoordinates) {
                mouseCoordsCurrent.text = "Mouse coordinates = X: ${mouse.x}, Y: ${mouse.y}"
            } else {
                mouseCoordsCurrent.text = ''
            }
        }

        private void updateStartButton() {
            startButton.enabled = !executor.started
        }

        private void updateStopButton() {
            stopButton.enabled = executor.started
        }

        private void updateProgressBar() {
            if (planStarted) {
                if (!executor.started) {
                    progressBar.indeterminate = false
                    progressBar.value = 1
                    progressLabel.text = 'Progress: 100%'
                } else if (executor.loopPlan) {
                    progressBar.indeterminate = true
                    progressLabel.text = 'Progress: ...'
                } else {
                    progressBar.value = executor.planProgress
                    progressLabel.text = "Progress: ${progressBar.value * 100 as int}%"
                }
            }
        }
    }

    private updatePlanAsJsonArea() {
        def planAsJson = planToJsonConverter.convertToJson(planExecutionArea.value, sleepBetweenStepsField.value, executionTimeField.value)
        this.planAsJsonArea.value = planAsJson
    }

    private Component getPlanUpload() {
        def buffer = new MemoryBuffer()
        def upload = new Upload(buffer)
        upload.uploadButton = new Button('Import plan...')
        upload.dropLabel = new Label('Drop a file with plan here')
        upload.addSucceededListener({
            def executionPlan = buffer.inputStream.readLines().stream().collect(Collectors.joining('\n'))
            planExecutionArea.value = executionPlan
        })

        return upload
    }

    private Component getMouseCaptureComponent() {
        def layout = new VerticalLayout(
                new HorizontalLayout(mouseCoordsCurrent, mouseCoordsCaptured),
                new HorizontalLayout(captureMouseButton, captureAddMouseButton)
        )

        def accordion = new Accordion()
        accordion.add('Mouse coordinates', layout)
        accordion.addOpenedChangeListener {
            shallCaptureMouseCoordinates = it.openedIndex.isPresent()
        }
        accordion.close()
        return accordion
    }

    private Component getStepsDocumentation() {
        def layout = new VerticalLayout()
        Steps.steps.values().toList().forEach { description ->
            def addToPlanButton = new Button('Add')
            addToPlanButton.addClickListener({
                planExecutionArea.value = "${planExecutionArea.value}\n${description.lineExample}"
            })

            def lineExampleLabel = new Label("${description.lineExample}: ")
            lineExampleLabel.className = 'lineExampleLabel'
            def usageLabel = new Label("${description.usage}")
            def labels = new HorizontalLayout(addToPlanButton, lineExampleLabel, usageLabel)
            layout.add(labels)
        }

        def accordion = new Accordion()
        accordion.close()
        accordion.add('Example steps', layout)
        return accordion
    }

    private Component getJsonPlanArea() {
        def accordion = new Accordion()
        planAsJsonArea.readOnly = true
        planAsJsonArea.className = 'planExecutionArea'
        def layout = new VerticalLayout()
        updatePlanAsJsonArea()
        layout.add(planAsJsonArea)
        accordion.close()
        accordion.add('Show Plan as JSON', layout)
        return accordion
    }
}
