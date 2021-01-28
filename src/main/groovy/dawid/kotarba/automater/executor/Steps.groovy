package dawid.kotarba.automater.executor

import dawid.kotarba.automater.executor.steps.keyboard.KeyboardHold
import dawid.kotarba.automater.executor.steps.keyboard.KeyboardPress
import dawid.kotarba.automater.executor.steps.keyboard.KeyboardRelease
import dawid.kotarba.automater.executor.steps.keyboard.KeyboardType
import dawid.kotarba.automater.executor.steps.mouse.MouseLeftClick
import dawid.kotarba.automater.executor.steps.mouse.MouseMiddleClick
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveByPercentStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveByStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveSomewhereStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveToPercentStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveToStep
import dawid.kotarba.automater.executor.steps.mouse.MouseRightClick
import dawid.kotarba.automater.executor.steps.mouse.MouseScrollStep
import dawid.kotarba.automater.executor.steps.sleep.RandomSleepStep
import dawid.kotarba.automater.executor.steps.sleep.SleepStep
import dawid.kotarba.automater.executor.switches.LoopSwitch
import dawid.kotarba.automater.executor.switches.WhenMouseIdleSwitch

class Steps {
    private static List<Step> steps = []
    static {
        // switches
        steps.add(new LoopSwitch())
        steps.add(new WhenMouseIdleSwitch())

        // keyboard
        steps.add(new KeyboardPress())
        steps.add(new KeyboardHold())
        steps.add(new KeyboardRelease())
        steps.add(new KeyboardType())

        // mouse
        steps.add(new MouseMoveToStep())
        steps.add(new MouseMoveByStep())
        steps.add(new MouseLeftClick())
        steps.add(new MouseMiddleClick())
        steps.add(new MouseRightClick())
        steps.add(new MouseMoveToPercentStep())
        steps.add(new MouseMoveByPercentStep())
        steps.add(new MouseScrollStep())
        steps.add(new MouseMoveSomewhereStep())

        // sleep
        steps.add(new SleepStep())
        steps.add(new RandomSleepStep())
    }

    static List<Step> getSteps() {
        return steps
    }
}
