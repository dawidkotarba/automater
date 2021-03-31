package dawid.kotarba.automater.executor.steps.sleep

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

import static com.google.common.base.Preconditions.checkArgument

class SleepRandomBetweenStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getTwoIntParams(executionLine)
        def minSleepTime = params.first
        def maxSleepTime = params.second
        checkArgument(minSleepTime >= 0 || maxSleepTime >= 0)
        checkArgument(minSleepTime <= maxSleepTime)

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
