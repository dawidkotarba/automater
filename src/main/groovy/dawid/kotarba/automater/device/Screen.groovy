package dawid.kotarba.automater.device

import dawid.kotarba.automater.model.Point
import org.springframework.stereotype.Service

import java.awt.*

@Service
class Screen {

    int getWidth() {
        def gd = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice;
        return gd.displayMode.width;
    }

    int getHeight() {
        def gd = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice;
        return gd.displayMode.height;
    }

    Point getResolution() {
        return new Point(width, height)
    }

    Point getMiddle() {
        return new Point(width / 2 as int, height / 2 as int)
    }
}
