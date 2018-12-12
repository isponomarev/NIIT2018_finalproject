package game;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
    private final ImageView imageView;                      // sprite sheet
    private final int count;                                // number of frames
    private final int columns;                              // number of columns in sprite sheet
    private int offsetX;                                    // offsetX of first frame
    private int offsetY;                                    // offsetY of first frame
    private final int width;                                // frame size (width)
    private final int height;                               // frame size (height)

    public SpriteAnimation(ImageView imageView, Duration duration, int count, int columns,
                           int offsetX, int offsetY, int width, int height) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);                         // set the duration of one cycle of animation
        setCycleCount(Animation.INDEFINITE);                // defines the number of cycles in animation
        setInterpolator(Interpolator.LINEAR);               // interpolation method
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height)); // set the first frame
    }

    // This method defines the behavior of animation (pos - position of animation: 0 - start, 1 - finish)
    protected void interpolate(double pos) {
        int index = 0;
        if (pos <= 0.25)
            index = 0;
        else if (pos <= 0.5)
            index = 1;
        else if (pos <= 0.75)
            index = 2;
        else index = 1;
        final int x = (index % columns) * width + offsetX;
        final int y = (index / columns) * height + offsetY;
        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }

    public void setOffsetX(int x){
        this.offsetX = x;
    }
    public void setOffsetY(int y){
        this.offsetY = y;
    }

}
