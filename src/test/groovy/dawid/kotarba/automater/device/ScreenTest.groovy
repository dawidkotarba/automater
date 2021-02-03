package dawid.kotarba.automater.device


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ScreenTest extends Specification {

    @Autowired
    private Screen screen

    def 'Should get a screen width'() {

        expect:
        screen.width > 0
    }

    def 'Should get a screen height'() {

        expect:
        screen.height > 0
    }

    def 'Should get a screen resolution'() {
        given:
        def resolution = screen.resolution

        expect:
        resolution.x == screen.width
        resolution.y == screen.height

        and:
        resolution.x > 0
        resolution.y > 0
    }

    def 'Should get a screen middle point'() {
        given:
        def middle = screen.middle

        expect:
        middle.x == screen.width / 2
        middle.y == screen.height / 2

        and:
        middle.x > 0
        middle.y > 0
    }
}
