package dawid.kotarba.automater.executor.switches

import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.invoke.MethodHandles

class WhenMouseIdleSwitch extends AbstractStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    void execute(String executionLine) {
        LOGGER.println('Found WhenMouseIdleSwitch...')
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getSupportedMethod() {
        Optional.of(Constants.MOUSE_IDLE)
    }
}
