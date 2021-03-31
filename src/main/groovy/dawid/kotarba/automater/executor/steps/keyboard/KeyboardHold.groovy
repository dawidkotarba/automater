package dawid.kotarba.automater.executor.steps.keyboard

import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

import static com.google.common.base.Preconditions.checkArgument

class KeyboardHold extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def param = getStringParam(executionLine)
        def executed = false

        Keyboard.Button.getByChar(param).ifPresent {
            keyboard.hold(it)
            executed = true
        }

        Keyboard.Button.getByName(param).ifPresent {
            if (!executed) {
                keyboard.hold(it)
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
        Optional.of('hold')
    }

    @Override
    int getArgumentsCount() {
        return 1
    }
}
