package dawid.kotarba.automater.executor.steps

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
    Optional<String> getSupportedMethod() {
        Optional.of('leftClick')
    }
}
