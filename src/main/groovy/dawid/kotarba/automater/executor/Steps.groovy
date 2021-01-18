package dawid.kotarba.automater.executor

import dawid.kotarba.automater.executor.impl.MouseMoveToStep
import dawid.kotarba.automater.executor.impl.SleepStep

class Steps {
    private List<Step> steps

    Steps() {
        steps = []
        steps.add(new MouseMoveToStep())
        steps.add(new SleepStep())
    }

    List<Step> getSteps() {
        return steps
    }
}
