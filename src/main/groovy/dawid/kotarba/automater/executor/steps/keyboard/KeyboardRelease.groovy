package dawid.kotarba.automater.executor.steps.keyboard

import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class KeyboardRelease extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def param = getStringParam(executionLine)
        Keyboard.Button.getByChar(param).ifPresent {
            keyboard.release(it)
        }
    }

    @Override
    StepType getStepType() {
        StepType.KEYBOARD
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('release')
    }
}
