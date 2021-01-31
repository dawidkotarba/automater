package dawid.kotarba.automater.executor.steps.keyboard


import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class KeyboardType extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def param = getStringParam(executionLine)
        keyboard.type(param)
    }

    @Override
    StepType getStepType() {
        StepType.KEYBOARD
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('type')
    }

    @Override
    int getArgumentsCount() {
        1
    }
}
