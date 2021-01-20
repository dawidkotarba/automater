package dawid.kotarba.automater.executor.steps.sleep

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class RandomSleepStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def maxSleepTime = getIntParam(executionLine)
        sleep(new Random().nextInt(maxSleepTime))
    }

    @Override
    StepType getStepType() {
        StepType.SLEEP
    }

    @Override
    Optional<String> getSupportedMethod() {
        Optional.of('random')
    }
}
