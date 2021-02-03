package dawid.kotarba.automater.device

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class MouseTest extends Specification {

    @Autowired
    private Mouse mouse

    @Autowired
    private Screen screen

    def 'Should move a mouse to a given position'() {
        given:
        mouse.moveTo(0, 0)
        def initialX = mouse.x
        def initialY = mouse.y

        when:
        mouse.moveTo(1, 1)
        def newX = mouse.x
        def newY = mouse.y

        then:
        initialX == newX - 1
        initialY == newY - 1
    }

    def 'Should move a mouse by few pixels'() {
        given:
        mouse.moveTo(1, 1)

        when:
        mouse.moveBy(1, 1)
        def newX = mouse.x
        def newY = mouse.y

        then:
        newX == 2
        newY == 2
    }

    def 'Should move a mouse by few pixels - negative'() {
        given:
        mouse.moveTo(1, 1)

        when:
        mouse.moveBy(-1, -1)
        def newX = mouse.x
        def newY = mouse.y

        then:
        newX == 0
        newY == 0
    }

    def 'Should move a mouse to a middle of the screen in two steps'() {
        given:
        mouse.moveTo(0, 0)

        when:
        // 10% + 40% = 50%
        mouse.moveByPercentOfTheScreen(10, 10)
        mouse.moveByPercentOfTheScreen(40, 40)

        then:
        mouse.x == screen.middle.x
        mouse.y == screen.middle.y
    }

    def 'Should move a mouse to a middle of the screen immediately'() {
        given:
        mouse.moveTo(0, 0)

        when:
        mouse.moveToPercentOfTheScreen(50, 50)

        then:
        mouse.x == screen.middle.x
        mouse.y == screen.middle.y
    }

    def 'Should indicate that a mouse was moving'() {
        given:
        !mouse.mouseMoving
        mouse.moveTo(1, 1)

        when:
        mouse.moveBy(-1, -1)
        def newX = mouse.x
        def newY = mouse.y

        then:
        newX == 0
        newY == 0

        and:
        mouse.mouseMoving
    }
}