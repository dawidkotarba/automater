package dawid.kotarba.automater.service.executor.steps.mouse

import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

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
