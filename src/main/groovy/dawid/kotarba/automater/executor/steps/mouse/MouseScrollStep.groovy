package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

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
