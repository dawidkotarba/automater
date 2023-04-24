package dawid.kotarba.automater.executor.switches

import dawid.kotarba.automater.Constants
import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType
import org.slf4j.LoggerFactory

import java.lang.invoke.MethodHandles

class LoopSwitch extends AbstractStep {

    private static final def LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    void execute(String executionLine) {
        LOGGER.debug('Found a LoopSwitch...')
    }

    @Override
    StepType getStepType() {
        StepType.SWITCH
    }

    @Override
    Optional<String> getMethod() {
        Optional.of(Constants.SWITCH_LOOP)
    }

    @Override
    int getArgumentsCount() {
        0
    }
}
