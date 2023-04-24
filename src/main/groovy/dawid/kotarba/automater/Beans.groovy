package dawid.kotarba.automater

import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.device.Mouse
import dawid.kotarba.automater.device.Screen
import dawid.kotarba.automater.service.executor.PlanExecutor
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class Beans {
    private static Screen screen
    private static Mouse mouse
    private static Keyboard keyboard
    private static PlanExecutor planExecutor

    @Lazy
    Beans(Screen screen, Mouse mouse, Keyboard keyboard, PlanExecutor planExecutor) {
        this.screen = screen
        this.mouse = mouse
        this.keyboard = keyboard
        this.planExecutor = planExecutor
    }

    static Screen getScreen() {
        return screen
    }

    static Mouse getMouse() {
        return mouse
    }

    static Keyboard getKeyboard() {
        return keyboard
    }
}
