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

    def mouseCoords = new Label("Retrieving mouse coordinates...")
    def progressBar = new ProgressBar()
    def startButton = new Button("Start", new Icon(VaadinIcon.PLAY))
    def stopButton = new Button("Stop", new Icon(VaadinIcon.STOP))

    View() {
        def executor = Beans.planExecutor

        def planExecutionLayout = new VerticalLayout()
        def planExecutionArea = new TextArea()
        def testPlan = new ClassPathResource('plans/ExamplePlan.txt')
        def testPlanText = testPlan.getFile().readLines().stream().collect(Collectors.joining('\n'))
        planExecutionArea.setValue(testPlanText)
        planExecutionArea.setWidth("500px")
        planExecutionLayout.add(planExecutionArea)

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
                new VerticalLayout(
                        new Label("Execute a Plan:"),
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
        componentsThread = new Thread(new ComponentsRunnable(attachEvent.getUI(), progressBar, mouseCoords, startButton, stopButton))
        componentsThread.start()
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        componentsThread.interrupt()
        componentsThread = null
    }

    private static class ComponentsRunnable implements Runnable {
        private final UI ui
        private final ProgressBar progressBar
        private final Label mouseCoords
        private final Button startButton
        private final Button stopButton

        ComponentsRunnable(UI ui, ProgressBar progressBar, Label mouseCoords, Button startButton, Button stopButton) {
            this.ui = ui
            this.progressBar = progressBar
            this.mouseCoords = mouseCoords
            this.startButton = startButton
            this.stopButton = stopButton
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
            mouseCoords.text = "Mouse coordinates [ X: ${mouse.x}, Y: ${mouse.y} ]"
        }

        private void updateStartButton(PlanExecutor executor) {
            startButton.enabled = !executor.isStarted()
        }

        private void updateStopButton(PlanExecutor executor) {
            stopButton.enabled = executor.isStarted()
        }

        private void updateProgressBar(PlanExecutor executor) {
            if (!executor.isStarted()) {
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
