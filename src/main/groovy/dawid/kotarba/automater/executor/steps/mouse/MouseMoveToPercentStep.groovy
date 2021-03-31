package dawid.kotarba.automater.executor.steps.mouse

import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkArgument

class MouseMoveToPercentStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getTwoIntParams(executionLine)
        def xPercent = params.first
        def yPercent = params.second
        checkArgument(xPercent >= 0 && xPercent <= 100)
        checkArgument(yPercent >= 0 && yPercent <= 100)

        mouse.moveToPercentOfTheScreen(params.first, params.second)
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('moveToPercentOfTheScreen')
    }

    @Override
    int getArgumentsCount() {
        2
    }
}
