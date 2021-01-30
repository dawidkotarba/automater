package dawid.kotarba.automater.executor

import dawid.kotarba.automater.executor.steps.keyboard.KeyboardHold
import dawid.kotarba.automater.executor.steps.keyboard.KeyboardPress
import dawid.kotarba.automater.executor.steps.keyboard.KeyboardRelease
import dawid.kotarba.automater.executor.steps.keyboard.KeyboardType
import dawid.kotarba.automater.executor.steps.mouse.MouseLeftClick
import dawid.kotarba.automater.executor.steps.mouse.MouseMiddleClick
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveByPercentStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveByStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveToPercentStep
import dawid.kotarba.automater.executor.steps.mouse.MouseMoveToStep
import dawid.kotarba.automater.executor.steps.mouse.MouseRandomMoveStep
import dawid.kotarba.automater.executor.steps.mouse.MouseRandomScrollStep
import dawid.kotarba.automater.executor.steps.mouse.MouseRightClick
import dawid.kotarba.automater.executor.steps.mouse.MouseScrollStep
import dawid.kotarba.automater.executor.steps.sleep.SleepRandomStep
import dawid.kotarba.automater.executor.steps.sleep.SleepStep
import dawid.kotarba.automater.executor.switches.LoopSwitch
import dawid.kotarba.automater.executor.switches.WhenMouseIdleSwitch

class Steps {
    private static Map<Step, Description> steps = [:]

    static {
        // switches
        addStep(new LoopSwitch(), 'SWITCH loop', 'Loop execution of a plan. Can be placed anywhere in the plan.')
        addStep(new WhenMouseIdleSwitch(), 'SWITCH mouseIdle', 'Execute only when mouse is idle. Can be placed anywhere in the plan.')

        // keyboard
        addStep(new KeyboardPress(), 'KEYBOARD press a', 'Press a key')
        addStep(new KeyboardHold(), 'KEYBOARD hold a', 'Holds a key')
        addStep(new KeyboardRelease(), 'KEYBOARD release a', 'Release a key')
        addStep(new KeyboardType(), 'KEYBOARD type abc', 'Types the given text')

        // mouse
        addStep(new MouseMoveToStep(), 'MOUSE moveTo 100 200', 'Move mouse to the given position (x,y)')
        addStep(new MouseMoveByStep(), 'MOUSE moveBy 1 -2', 'Move mouse by the given value in pixels (x,y)')
        addStep(new MouseLeftClick(), 'MOUSE leftClick', 'Click left mouse button')
        addStep(new MouseMiddleClick(), 'MOUSE middleClick', 'Click middle mouse button')
        addStep(new MouseRightClick(), 'MOUSE rightClick', 'Click right mouse button')
        addStep(new MouseMoveToPercentStep(), 'MOUSE moveToPercentOfTheScreen 50 50', 'Move mouse to the given position in percent (x,y)')
        addStep(new MouseMoveByPercentStep(), 'MOUSE moveByPercentOfTheScreen 10 20', 'Move mouse by the given value in percents (x,y)')
        addStep(new MouseScrollStep(), 'MOUSE scroll -1', 'Scroll a mouse wheel (lines, can be negative)')
        addStep(new MouseRandomScrollStep(), 'MOUSE scrollRandom 5', 'Scroll a mouse wheel randomly (upper bound, can be negative)')
        addStep(new MouseRandomMoveStep(), 'MOUSE moveRandom', 'Move a mouse to a random position')

        // sleep
        addStep(new SleepStep(), 'SLEEP of 500', 'Sleep for a given time (in ms)')
        addStep(new SleepRandomStep(), 'SLEEP random 200', 'Sleep for a given or lower time (upper bound in ms)')
    }

    static Map<Step, Description> getSteps() {
        return steps
    }

    private static void addStep(Step step, String lineExample, String usage) {
        steps.put(step, new Description(step, lineExample, usage))
    }

    static class Description {
        private Step step
        private String lineExample
        private String usage

        Description(Step step, String lineExample, String usage) {
            this.step = step
            this.lineExample = lineExample
            this.usage = usage
        }

        Step getStep() {
            return step
        }

        String getLineExample() {
            return lineExample
        }

        String getUsage() {
            return usage
        }
    }
}
