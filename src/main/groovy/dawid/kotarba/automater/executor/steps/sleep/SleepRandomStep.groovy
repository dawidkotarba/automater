package dawid.kotarba.automater.executor.steps.sleep

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class SleepRandomStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def maxSleepTime = getIntParam(executionLine)

        if (maxSleepTime <= 0) {
            throw new IllegalArgumentException('Sleep time has to be a positive number')
        }

        sleep(new Random().nextInt(maxSleepTime))
    }

    @Override
    StepType getStepType() {
        StepType.SLEEP
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('random')
    }

    @Override
    int getArgumentsCount() {
        1
    }
}
