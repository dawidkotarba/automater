package dawid.kotarba.automater.executor.steps.sleep

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class SleepStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def sleepTime = getIntParam(executionLine)
        sleep(sleepTime)
    }

    @Override
    StepType getStepType() {
        StepType.SLEEP
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('of')
    }
}
