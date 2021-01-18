package dawid.kotarba.automater.executor.impl

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

class SleepStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getParams(executionLine)
        sleep(params[0] as long)
    }

    @Override
    StepType getStepType() {
        return StepType.SLEEP
    }

    @Override
    String getSupportedMethod() {
        return ""
    }
}
