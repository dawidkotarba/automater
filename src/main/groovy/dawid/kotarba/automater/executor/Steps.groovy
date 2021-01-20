package dawid.kotarba.automater.executor

import dawid.kotarba.automater.executor.steps.KeyboardPress
import dawid.kotarba.automater.executor.steps.MouseLeftClick
import dawid.kotarba.automater.executor.steps.MouseMoveToStep
import dawid.kotarba.automater.executor.steps.MouseRightClick
import dawid.kotarba.automater.executor.steps.RandomSleepStep
import dawid.kotarba.automater.executor.steps.SleepStep
import dawid.kotarba.automater.executor.switches.LoopSwitch
import dawid.kotarba.automater.executor.switches.WhenMouseIdleSwitch

class Steps {
    private static List<Step> steps = []

    Steps() {
        // switches
        steps.add(new LoopSwitch())
        steps.add(new WhenMouseIdleSwitch())

        // steps
        steps.add(new MouseMoveToStep())
        steps.add(new KeyboardPress())
        steps.add(new MouseLeftClick())
        steps.add(new MouseRightClick())
        steps.add(new SleepStep())
        steps.add(new RandomSleepStep())
    }

    List<Step> getSteps() {
        return steps
    }
}
