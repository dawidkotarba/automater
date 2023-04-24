package dawid.kotarba.automater.service.executor.steps.mouse

import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

class MouseMiddleClick extends AbstractStep {
    @Override
    void execute(String executionLine) {
        mouse.middleClick()
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('middleClick')
    }

    @Override
    int getArgumentsCount() {
        0
    }
}
