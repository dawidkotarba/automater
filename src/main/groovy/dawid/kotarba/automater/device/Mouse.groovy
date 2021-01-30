package dawid.kotarba.automater.device


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import java.awt.*
import java.awt.event.InputEvent

import static com.google.common.base.Preconditions.checkArgument

@Service
class Mouse {

    private final Robot robot
    private final Screen screen
    private int lastX = x
    private int lastY = y

    @Autowired
    Mouse(Screen screen) {
        this.robot = new Robot()
        this.screen = screen
    }

    int getX() {
        return MouseInfo.pointerInfo.location.x
    }

    int getY() {
        return MouseInfo.pointerInfo.location.y
    }

    @Scheduled(fixedDelay = 500L)
    void saveLocation() {
        lastX = x
        lastY = y
    }

    boolean isMouseMoving() {
        return x != lastX || y != lastY
    }

    void moveTo(int x, int y) {
        robot.mouseMove(x, y)
    }

    void moveBy(int x, int y) {
        robot.mouseMove(getX() + x, getY() + y)
    }

    void moveToPercentOfTheScreen(int xPercent, int yPercent) {
        checkArgument(xPercent >= 0 && xPercent <= 100)
        checkArgument(yPercent >= 0 && yPercent <= 100)

        def x = screen.width * xPercent / 100
        def y = screen.height * yPercent / 100
        moveTo(x as int, y as int)
    }

    void moveByPercentOfTheScreen(int xPercent, int yPercent) {
        checkArgument(xPercent >= -100 && xPercent <= 100)
        checkArgument(yPercent >= -100 && yPercent <= 100)

        def x = screen.width * xPercent / 100
        def y = screen.height * yPercent / 100

        moveTo(getX() + x as int, getY() + y as int)
    }

    void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    }

    void middleClick() {
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
    }

    void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
    }

    void scroll(int wheelAmt) {
        robot.mouseWheel(wheelAmt)
    }
}
