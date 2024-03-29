package dawid.kotarba.automater.service.executor.steps.mouse

import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

class MouseRandomScrollStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def maxWheelAmount = getIntParam(executionLine)
        def wheelAmt = random.nextInt(Math.abs(maxWheelAmount))

        if (maxWheelAmount >= 0) {
            mouse.scroll(wheelAmt)
        } else {
            mouse.scroll(-wheelAmt)
        }
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('scrollRandom')
    }

    @Override
    int getArgumentsCount() {
        1
    }
}
