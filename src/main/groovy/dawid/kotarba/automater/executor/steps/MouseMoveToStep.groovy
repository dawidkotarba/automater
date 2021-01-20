package dawid.kotarba.automater.executor.steps

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class MouseMoveToStep extends AbstractStep {

    @Override
    StepType getStepType() {
        return StepType.MOUSE
    }

    @Override
    Optional<String> getSupportedMethod() {
        return Optional.of('moveTo')
    }

    @Override
    void execute(String executionLine) {
        def params = getParams(executionLine)
        mouse.moveTo(params.get(0) as int, params.get(1) as int)
    }
}
