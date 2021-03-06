package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class MouseRightClick extends AbstractStep {
    @Override
    void execute(String executionLine) {
        mouse.rightClick()
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('rightClick')
    }

    @Override
    int getArgumentsCount() {
        0
    }
}
