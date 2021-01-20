package dawid.kotarba.automater.executor.steps.sleep

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class SleepStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getParams(executionLine)
        def sleepTime = params[0] as int
        sleep(sleepTime)
    }

    @Override
    StepType getStepType() {
        StepType.SLEEP
    }

    @Override
    Optional<String> getSupportedMethod() {
        Optional.of('of')
    }
}
