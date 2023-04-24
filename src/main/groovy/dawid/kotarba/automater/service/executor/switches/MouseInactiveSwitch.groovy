package dawid.kotarba.automater.service.executor.switches

import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

class MouseInactiveSwitch extends AbstractStep {

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
        Optional.of(Constants.SWITCH_MOUSE_INACTIVE)
    }

    @Override
    int getArgumentsCount() {
        0
    }
}
