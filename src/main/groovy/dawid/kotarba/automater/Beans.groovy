package dawid.kotarba.automater

import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.device.Mouse
import dawid.kotarba.automater.executor.PlanExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class Beans {
    private static Mouse mouse
    private static Keyboard keyboard
    private static PlanExecutor planExecutor

    @Autowired
    @Lazy
    Beans(Mouse mouseBean, Keyboard keyboardBean, PlanExecutor planExecutorBean) {
        mouse = mouseBean
        keyboard = keyboardBean
        planExecutor = planExecutorBean
    }

    static Mouse getMouse() {
        return mouse
    }

    static Keyboard getKeyboard() {
        return keyboard
    }

    static PlanExecutor getPlanExecutor() {
        return planExecutor
    }
}
