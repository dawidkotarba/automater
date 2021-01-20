package dawid.kotarba.automater.executor.steps.keyboard

import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class KeyboardPress extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getParams(executionLine)
        Keyboard.Button.getByChar(params[0]).ifPresent {
            keyboard.press(it)
        }
    }

    @Override
    StepType getStepType() {
        StepType.KEYBOARD
    }

    @Override
    Optional<String> getSupportedMethod() {
        Optional.of('press')
    }
}