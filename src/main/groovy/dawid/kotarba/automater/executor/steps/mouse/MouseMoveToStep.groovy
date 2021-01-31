package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class MouseMoveToStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getTwoIntParams(executionLine)
        mouse.moveTo(params.first, params.second)
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('moveTo')
    }
}
