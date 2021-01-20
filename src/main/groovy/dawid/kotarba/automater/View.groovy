package dawid.kotarba.automater

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.router.Route
import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import dawid.kotarba.automater.executor.PlanParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource

import java.util.stream.Collectors

@Route
class View extends VerticalLayout {

    private PlanExecutor executor
    private PlanParser parser

    @Autowired
    View(PlanExecutor executor) {
        this.executor = executor
        this.parser = new PlanParser()

        VerticalLayout todosList = new VerticalLayout() // (1)
        TextArea textArea = new TextArea()
        def testPlan = new ClassPathResource('plans/TestPlan.txt')
        def testPlanText = testPlan.getFile().readLines().stream().collect(Collectors.joining('\n'))
        textArea.setValue(testPlanText)

        Button addButton = new Button("Execute") // (3)
        addButton.addClickShortcut(Key.ENTER)
        addButton.addClickListener({
            // TEST HERE
            def plan = new Plan("Plan from text area", textArea.getValue())
            executor.execute(plan)
        })
        add( // (5)
                new H1("Automater"),
                todosList,
                new HorizontalLayout(
                        textArea,
                        addButton
                )
        )
    }
}
