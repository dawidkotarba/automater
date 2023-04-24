package dawid.kotarba.automater.service.executor.steps.keyboard

import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

import static com.google.common.base.Preconditions.checkArgument

class KeyboardRelease extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def param = getStringParam(executionLine)
        def executed = false

        Keyboard.Button.getByChar(param).ifPresent {
            keyboard.release(it)
            executed = true
        }

        Keyboard.Button.getByName(param).ifPresent {
            if (!executed) {
                keyboard.release(it)
                executed = true
            }
        }

        checkArgument(executed)
    }

    @Override
    StepType getStepType() {
        StepType.KEYBOARD
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('release')
    }

    @Override
    int getArgumentsCount() {
        1
    }
}
