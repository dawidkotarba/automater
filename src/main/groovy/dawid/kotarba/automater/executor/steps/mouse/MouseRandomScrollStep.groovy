package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

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
}
