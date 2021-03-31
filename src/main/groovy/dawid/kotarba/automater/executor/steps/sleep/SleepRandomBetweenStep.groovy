package dawid.kotarba.automater.executor.steps.sleep

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class SleepRandomBetweenStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getTwoIntParams(executionLine)
        def minSleepTime = params.first
        def maxSleepTime = params.second

        if (minSleepTime <= 0 || maxSleepTime <= 0) {
            throw new IllegalArgumentException('Sleep times must be positive')
        }

        if (minSleepTime >= maxSleepTime) {
            throw new IllegalArgumentException('Minimal sleep time has to be bigger than maximal sleep time')
        }
        sleep(new Random().nextInt(maxSleepTime - minSleepTime) + minSleepTime)
    }

    @Override
    StepType getStepType() {
        StepType.SLEEP
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('randomBetween')
    }

    @Override
    int getArgumentsCount() {
        2
    }
}
