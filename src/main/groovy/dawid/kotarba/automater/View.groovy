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
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.router.Route
import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import dawid.kotarba.automater.executor.PlanParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource

import java.util.stream.Collectors

@Route
@Push
class View extends VerticalLayout {

    private final PlanExecutor executor
    private final PlanParser parser
    private MouseLocationThread thread

    @Autowired
    View(PlanExecutor executor) {
        this.executor = executor
        this.parser = new PlanParser()

        VerticalLayout layout = new VerticalLayout()
        Label label = new Label()
        label.text = 'Ala ma kota'
        TextArea textArea = new TextArea()
        def testPlan = new ClassPathResource('plans/TestPlan.txt')
        def testPlanText = testPlan.getFile().readLines().stream().collect(Collectors.joining('\n'))
        textArea.setValue(testPlanText)
        textArea.setWidth("500px")
        Button startButton = new Button("Start")
        def stopButton = new Button("Stop [Esc]")
        stopButton.addClickShortcut(Key.ESCAPE)
        startButton.addClickListener({
            // TEST HERE
            def plan = new Plan("Plan from text area", textArea.getValue())
            new Notification("Plan started", 3000).open()
            executor.start(plan)
        })
        stopButton.addClickListener({
            executor.stop()
            new Notification("Execution stopped", 3000).open()
        })
        add( // (5)
                new H1("Automater"),
                layout,
                new HorizontalLayout(
                        label,
                        textArea,
                        startButton,
                        stopButton
                )
        )
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        def mouseCoords = new Label("Retrieving mouse coordinates...")
        add(mouseCoords)

        // Start the data feed thread
        thread = new MouseLocationThread(attachEvent.getUI(), mouseCoords)
        thread.start()
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        thread.interrupt()
        thread = null
    }

    private static class MouseLocationThread extends Thread {
        private final UI ui
        private final Label mouseCoords

        MouseLocationThread(UI ui, Label mouseCoords) {
            this.ui = ui
            this.mouseCoords = mouseCoords
        }

        @Override
        void run() {
            def mouse = Beans.mouse
            while (true) {
                sleep(50)
                ui.access {
                    mouseCoords.text = "Mouse coordinates [ X: ${mouse.x}, Y: ${mouse.y} ]"
                }
            }
        }
    }
}
