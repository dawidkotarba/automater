package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

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
