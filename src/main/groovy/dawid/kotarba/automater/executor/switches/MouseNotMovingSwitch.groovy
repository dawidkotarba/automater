package dawid.kotarba.automater.executor.switches

import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class MouseNotMovingSwitch extends AbstractStep {

    @Override
    void execute(String executionLine) {
        // intentionally left blank
    }

    @Override
    StepType getStepType() {
        StepType.SWITCH
    }

    @Override
    Optional<String> getMethod() {
        Optional.of(Constants.SWITCH_MOUSE_NOT_MOVING)
    }

    @Override
    int getArgumentsCount() {
        0
    }
}
