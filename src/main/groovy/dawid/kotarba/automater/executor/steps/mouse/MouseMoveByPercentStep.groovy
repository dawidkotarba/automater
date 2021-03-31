package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

import static com.google.common.base.Preconditions.checkArgument

class MouseMoveByPercentStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getTwoIntParams(executionLine)
        def xPercent = params.first
        def yPercent = params.second
        checkArgument(xPercent >= -100 && xPercent <= 100)
        checkArgument(yPercent >= -100 && yPercent <= 100)

        mouse.moveByPercentOfTheScreen(xPercent, yPercent)
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('moveByPercentOfTheScreen')
    }

    @Override
    int getArgumentsCount() {
        2
    }
}
