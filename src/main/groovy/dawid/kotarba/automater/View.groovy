package dawid.kotarba.automater

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.router.Route
import dawid.kotarba.automater.executor.Plan
import dawid.kotarba.automater.executor.PlanExecutor
import org.springframework.beans.factory.annotation.Autowired

@Route
class View extends VerticalLayout {

    private PlanExecutor executor

    @Autowired
    View(PlanExecutor executor) {
        this.executor = executor
        VerticalLayout todosList = new VerticalLayout() // (1)
        TextArea textArea = new TextArea() // (2)
        Button addButton = new Button("Execute") // (3)
        addButton.addClickShortcut(Key.ENTER)
        addButton.addClickListener({
            // TEST HERE
            def plan = new Plan("test name")
            plan.addExecutionLine("MOUSE moveTo 100 100")
            plan.addExecutionLine("SLEEP 500")
            plan.addExecutionLine("MOUSE moveTo 300 300")
            plan.addExecutionLine("SLEEP 500")
            plan.addExecutionLine("MOUSE moveTo 500 500")
            plan.addExecutionLine("SLEEP 500")
            plan.addExecutionLine("MOUSE moveTo 800 800")
            plan.addExecutionLine("SLEEP 500")
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
