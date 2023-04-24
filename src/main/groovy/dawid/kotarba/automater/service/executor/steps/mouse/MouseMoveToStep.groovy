package dawid.kotarba.automater.service.executor.steps.mouse

import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

import static com.google.common.base.Preconditions.checkArgument

class MouseMoveToStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def params = getTwoIntParams(executionLine)
        def x = params.first
        def y = params.second
        checkArgument(x >= 0 && y >= 0)

        mouse.moveTo(x, y)
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('moveTo')
    }

    @Override
    int getArgumentsCount() {
        2
    }
}
