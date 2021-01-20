package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class MouseMoveToStep extends AbstractStep {

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getSupportedMethod() {
        Optional.of('moveTo')
    }

    @Override
    void execute(String executionLine) {
        def params = getParams(executionLine)
        mouse.moveTo(params.get(0) as int, params.get(1) as int)
    }
}
