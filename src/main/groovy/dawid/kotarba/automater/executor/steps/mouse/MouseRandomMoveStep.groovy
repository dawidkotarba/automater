package dawid.kotarba.automater.executor.steps.mouse


import dawid.kotarba.automater.executor.AbstractStep
import dawid.kotarba.automater.executor.StepType

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
    Optional<String> getSupportedMethod() {
        Optional.of('moveRandom')
    }
}
