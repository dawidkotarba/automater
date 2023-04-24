package dawid.kotarba.automater.service.executor.steps.mouse

import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

class MouseScrollStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def wheelAmt = getIntParam(executionLine)
        mouse.scroll(wheelAmt)
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('scroll')
    }

    @Override
    int getArgumentsCount() {
        1
    }
}
