package dawid.kotarba.automater.service.executor.steps.mouse

import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

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
