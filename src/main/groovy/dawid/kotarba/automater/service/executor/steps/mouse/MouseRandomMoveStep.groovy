package dawid.kotarba.automater.service.executor.steps.mouse


import dawid.kotarba.automater.service.executor.AbstractStep
import dawid.kotarba.automater.service.executor.StepType

class MouseRandomMoveStep extends AbstractStep {
    @Override
    void execute(String executionLine) {
        def width = random.nextInt(screen.width)
        def height = random.nextInt(screen.height)
        mouse.moveTo(width, height)
    }

    @Override
    StepType getStepType() {
        StepType.MOUSE
    }

    @Override
    Optional<String> getMethod() {
        Optional.of('moveRandom')
    }

    @Override
    int getArgumentsCount() {
        0
    }
}
