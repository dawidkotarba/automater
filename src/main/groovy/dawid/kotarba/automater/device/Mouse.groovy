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
    private final Random random
    private int lastX = getX()
    private int lastY = getY()

    @Autowired
    Mouse(Screen screen) {
        this.robot = new Robot()
        this.screen = screen
        random = new Random()
    }

    int getX() {
        return MouseInfo.getPointerInfo().getLocation().getX()
    }

    int getY() {
        return MouseInfo.getPointerInfo().getLocation().getY()
    }

    @Scheduled(fixedDelay = 500L)
    void saveLocation() {
        lastX = getX()
        lastY = getY()
    }

    boolean isMouseMoving() {
        return getX() != lastX || getY() != lastY
    }

    void moveTo(int x, int y) {
        robot.mouseMove(x, y)
    }

    void moveSomewhere() {
        def width = random.nextInt(screen.getWidth())
        def height = random.nextInt(screen.getHeight())
        robot.mouseMove(width, height)
    }

    void moveBy(int x, int y) {
        robot.mouseMove(getX() + x, getY() + y)
    }

    void moveToPercentOfTheScreen(int xPercent, int yPercent) {
        checkArgument(xPercent >= 0 && xPercent <= 100)
        checkArgument(yPercent >= 0 && yPercent <= 100)

        def x = screen.getWidth() * xPercent / 100
        def y = screen.getHeight() * yPercent / 100
        moveTo(x as int, y as int)
    }

    void moveByPercentOfTheScreen(int xPercent, int yPercent) {
        checkArgument(xPercent >= -100 && xPercent <= 100)
        checkArgument(yPercent >= -100 && yPercent <= 100)

        def x = screen.getWidth() * xPercent / 100
        def y = screen.getHeight() * yPercent / 100

        moveTo(getX() + x as int, getY() + y as int)
    }

    void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
    }

    void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK)
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
    }

    void scroll(int wheelAmt) {
        robot.mouseWheel(wheelAmt)
    }
}
