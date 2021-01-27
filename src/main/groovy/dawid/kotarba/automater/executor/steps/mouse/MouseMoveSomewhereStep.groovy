package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class MouseMoveSomewhereStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        mouse.moveSomewhere()
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getSupportedMethod() {
        Optional.of('moveSomewhere')
    }
}
