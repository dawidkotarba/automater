package dawid.kotarba.automater.executor

import dawid.kotarba.automater.executor.steps.keyboard.KeyboardPress
import dawid.kotarba.automater.executor.steps.mouse.MouseLeftClick
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveByStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveToStep
import dawid.kotarba.automater.executor.steps.mouse.MouseRightClick
import dawid.kotarba.automater.executor.steps.sleep.RandomSleepStep
import dawid.kotarba.automater.executor.steps.sleep.SleepStep
import dawid.kotarba.automater.executor.switches.LoopSwitch
import dawid.kotarba.automater.executor.switches.WhenMouseIdleSwitch

class Steps {
    private static List<Step> steps = []

    Steps() {
        // switches
        steps.add(new LoopSwitch())
        steps.add(new WhenMouseIdleSwitch())

        // keyboard
        steps.add(new KeyboardPress())

        // mouse
        steps.add(new MouseMoveToStep())
        steps.add(new MouseMoveByStep())
        steps.add(new MouseLeftClick())
        steps.add(new MouseRightClick())

        // sleep
        steps.add(new SleepStep())
        steps.add(new RandomSleepStep())
    }

    List<Step> getSteps() {
        return steps
    }
}
