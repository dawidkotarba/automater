package dawid.kotarba.automater.device

import dawid.kotarba.automater.model.Point
import org.springframework.stereotype.Service

import java.awt.*

@Service
class Screen {

    int getWidth() {
        def gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return gd.getDisplayMode().getWidth();
    }

    int getHeight() {
        def gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return gd.getDisplayMode().getHeight();
    }

    Point getResolution() {
        return new Point(getWidth(), getHeight())
    }

    Point getMiddle() {
        return new Point(getWidth() / 2 as int, getHeight() / 2 as int)
    }
}
