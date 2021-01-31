package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class MouseLeftClick extends AbstractStep {
    @Override
    void execute(String executionLine) {
        mouse.leftClick()
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('leftClick')
    }

    @Override
    int getArgumentsCount() {
        0
    }
}
