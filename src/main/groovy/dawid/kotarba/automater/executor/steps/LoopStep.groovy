package dawid.kotarba.automater.executor.steps

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.invoke.MethodHandles

class LoopStep extends AbstractStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    void execute(String executionLine) {
        LOGGER.println("Found loop step...")
    }

    @Override
    StepType getStepType() {
        return StepType.LOOP
    }

    @Override
    Optional<String> getSupportedMethod() {
        return Optional.empty()
    }
}
