package dawid.kotarba.automater.executor.steps.sleep

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class SleepStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def sleepTime = getIntParam(executionLine)

        if (sleepTime <= 0) {
            throw new IllegalArgumentException('Sleep time has to be a positive number')
        }

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

    @Override
    int getArgumentsCount() {
        1
    }
}
