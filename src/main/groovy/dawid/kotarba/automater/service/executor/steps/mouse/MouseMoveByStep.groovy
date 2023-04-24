package dawid.kotarba.automater.service.executor.steps.mouse

import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

class MouseMoveByStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getTwoIntParams(executionLine)
        mouse.moveBy(params.first, params.second)
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('moveBy')
    }

    @Override
    int getArgumentsCount() {
        2
    }
}
