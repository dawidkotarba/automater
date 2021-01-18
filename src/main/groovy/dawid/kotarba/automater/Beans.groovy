package dawid.kotarba.automater

import dawid.kotarba.automater.device.Keyboard
import dawid.kotarba.automater.device.Mouse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class Beans {
    private static Mouse mouse
    private static Keyboard keyboard

    @Autowired
    @Lazy
    Beans(Mouse mouseBean, Keyboard keyboardBean) {
        mouse = mouseBean
        keyboard = keyboardBean
    }

    static Mouse getMouse() {
        return mouse
    }

    static Keyboard getKeyboard() {
        return keyboard
    }
}
