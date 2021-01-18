package dawid.kotarba.automater

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.router.Route

@Route
class View extends VerticalLayout {
    View() {
        VerticalLayout todosList = new VerticalLayout() // (1)
        TextArea textArea = new TextArea() // (2)
        Button addButton = new Button("Add") // (3)
        addButton.addClickShortcut(Key.ENTER)
        addButton.addClickListener({
            // (4)
            Checkbox checkbox = new Checkbox(textArea.getValue())
            todosList.add(checkbox)
        })
        add( // (5)
                new H1("Vaadin Todo"),
                todosList,
                new HorizontalLayout(
                        textArea,
                        addButton
                )
        )
    }
}
