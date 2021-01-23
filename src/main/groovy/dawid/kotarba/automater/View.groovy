package dawid.kotarba.automater

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.progressbar.ProgressBar
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.router.Route
import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanParser
import org.springframework.core.io.ClassPathResource

import java.util.stream.Collectors

@Route
@Push
class View extends VerticalLayout {

    private final PlanParser parser
    private Thread mouseLocationThread
    private Thread progressBarThread

    def mouseCoords = new Label("Retrieving mouse coordinates...")
    def progressBar = new ProgressBar()


    View() {
        def executor = Beans.planExecutor
        this.parser = new PlanParser()

        def planExecutionLayout = new VerticalLayout()
        def planExecutionArea = new TextArea()
        def testPlan = new ClassPathResource('plans/TestPlan.txt')
        def testPlanText = testPlan.getFile().readLines().stream().collect(Collectors.joining('\n'))
        planExecutionArea.setValue(testPlanText)
        planExecutionArea.setWidth("500px")
        planExecutionLayout.add(planExecutionArea)

        def startButton = new Button("Start")
        def stopButton = new Button("Stop")
        stopButton.addClickShortcut(Key.ESCAPE)
        startButton.addClickListener({
            new Thread(new Runnable() {
                @Override
                void run() {
                    def plan = new Plan("Plan from text area", planExecutionArea.getValue())
                    executor.start(plan)
                }
            }).start()
        })
        stopButton.addClickListener({
            executor.stop()
            new Notification("Execution stopped", 3000).open()
        })
        add(
                new H1("Automater"),
                new HorizontalLayout(
                        planExecutionLayout,
                ),
                progressBar,
                mouseCoords,
                new HorizontalLayout(
                        startButton,
                        stopButton
                ),
        )
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        def eventUi = attachEvent.getUI()
        progressBarThread = new Thread(new ProgressBarRunnable(eventUi, progressBar))
        progressBarThread.start()

        mouseLocationThread = new Thread(new MouseLocationRunnable(eventUi, mouseCoords))
        mouseLocationThread.start()
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        progressBarThread.interrupt()
        progressBarThread = null

        mouseLocationThread.interrupt()
        mouseLocationThread = null
    }

    private static class ProgressBarRunnable implements Runnable {
        private final UI ui
        private ProgressBar progressBar

        ProgressBarRunnable(UI ui, ProgressBar progressBar) {
            this.ui = ui
            this.progressBar = progressBar
        }

        @Override
        void run() {
            def executor = Beans.planExecutor
            while (true) {
                sleep(10)
                ui.access {
                    progressBar.value = executor.planProgress
                }
            }
        }
    }

    private static class MouseLocationRunnable implements Runnable {
        private final UI ui
        private final Label mouseCoords

        MouseLocationRunnable(UI ui, Label mouseCoords) {
            this.ui = ui
            this.mouseCoords = mouseCoords
        }

        @Override
        void run() {
            def mouse = Beans.mouse
            while (true) {
                sleep(10)
                ui.access {
                    mouseCoords.text = "Mouse coordinates [ X: ${mouse.x}, Y: ${mouse.y} ]"
                }
            }
        }
    }
}
