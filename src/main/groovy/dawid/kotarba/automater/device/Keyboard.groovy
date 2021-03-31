package dawid.kotarba.automater.device

import org.springframework.stereotype.Service

import java.awt.*
import java.awt.event.KeyEvent

@Service
class Keyboard {

    static enum Button {
        A(KeyEvent.VK_A, 'a'),
        B(KeyEvent.VK_B, 'b'),
        C(KeyEvent.VK_C, 'c'),
        D(KeyEvent.VK_D, 'd'),
        E(KeyEvent.VK_E, 'e'),
        F(KeyEvent.VK_F, 'f'),
        G(KeyEvent.VK_G, 'g'),
        H(KeyEvent.VK_H, 'h'),
        I(KeyEvent.VK_I, 'i'),
        J(KeyEvent.VK_J, 'j'),
        K(KeyEvent.VK_K, 'k'),
        L(KeyEvent.VK_L, 'l'),
        M(KeyEvent.VK_M, 'm'),
        N(KeyEvent.VK_N, 'n'),
        O(KeyEvent.VK_O, 'o'),
        U(KeyEvent.VK_U, 'u'),
        P(KeyEvent.VK_P, 'p'),
        R(KeyEvent.VK_R, 'r'),
        S(KeyEvent.VK_S, 's'),
        T(KeyEvent.VK_T, 't'),
        Q(KeyEvent.VK_Q, 'q'),
        W(KeyEvent.VK_W, 'w'),
        V(KeyEvent.VK_V, 'v'),
        X(KeyEvent.VK_X, 'x'),
        Y(KeyEvent.VK_Y, 'y'),
        Z(KeyEvent.VK_Z, 'z'),
        _1(KeyEvent.VK_1, '1'),
        _2(KeyEvent.VK_2, '2'),
        _3(KeyEvent.VK_3, '3'),
        _4(KeyEvent.VK_4, '4'),
        _5(KeyEvent.VK_5, '5'),
        _6(KeyEvent.VK_6, '6'),
        _7(KeyEvent.VK_7, '7'),
        _8(KeyEvent.VK_8, '8'),
        _9(KeyEvent.VK_9, '9'),
        _0(KeyEvent.VK_0, '0'),
        PERIOD(KeyEvent.VK_PERIOD, '.'),
        COMMA(KeyEvent.VK_COMMA, ','),
        TILDE(KeyEvent.VK_DEAD_TILDE, '~'),
        COLON(KeyEvent.VK_COLON, ':'),
        SEMICOLON(KeyEvent.VK_SEMICOLON, ';'),
        AMPERSAND(KeyEvent.VK_AMPERSAND, '&'),
        PLUS(KeyEvent.VK_PLUS, '+'),
        MINUS(KeyEvent.VK_MINUS, '-'),
        SLASH(KeyEvent.VK_SLASH, '/'),
        BACKSLASH(KeyEvent.VK_BACK_SLASH, '\\'),
        DIVIDE(KeyEvent.VK_DIVIDE, '/'),
        MULTIPLY(KeyEvent.VK_MULTIPLY, '*'),
        CONTROL(KeyEvent.VK_CONTROL, null),
        ALT(KeyEvent.VK_ALT, null),
        SHIFT(KeyEvent.VK_SHIFT, null),
        TAB(KeyEvent.VK_TAB, null),
        BACKSPACE(KeyEvent.VK_BACK_SPACE, null),
        SPACE(KeyEvent.VK_SPACE, ' '),
        EXCLAMATION_MARK(KeyEvent.VK_EXCLAMATION_MARK, '!'),
        EQUALS(KeyEvent.VK_EQUALS, '='),
        UNDERSCORE(KeyEvent.VK_UNDERSCORE, '_'),
        WINDOWS(KeyEvent.VK_WINDOWS, null),
        ESCAPE(KeyEvent.VK_ESCAPE, null)

        private int keyCode
        private String character

        Button(int keyCode, String character) {
            this.keyCode = keyCode
            this.character = character
        }

        static Optional<Button> getByChar(String character) {
            return Arrays.asList(values()).stream().filter({
                it.character == character.toLowerCase()
            }).findFirst()
        }

        static Optional<Button> getByName(String name) {
            return Arrays.asList(values()).stream().filter({
                it.name() == name.toUpperCase()
            }).findFirst()
        }
    }

    private final Robot robot

    Keyboard() {
        this.robot = new Robot()
    }

    void press(Button button) {
        robot.keyPress(button.keyCode)
        robot.keyRelease(button.keyCode)
    }

    void hold(Button button) {
        robot.keyPress(button.keyCode)
    }

    void release(Button button) {
        robot.keyRelease(button.keyCode)
    }

    void type(String sentence) {
        sentence.chars().each {
            Button.getByChar(Character.toString(it)).ifPresent {
                press(it)
            }
        }
    }
}
