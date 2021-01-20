package dawid.kotarba.automater.executor

import dawid.kotarba.automater.executor.steps.LoopStep
import dawid.kotarba.automater.executor.steps.MouseMoveToStep
import dawid.kotarba.automater.executor.steps.SleepStep

class Steps {
    private List<Step> steps

    Steps() {
        steps = []
        steps.add(new MouseMoveToStep())
        steps.add(new SleepStep())
        steps.add(new LoopStep())
    }

    List<Step> getSteps() {
        return steps
    }
}
