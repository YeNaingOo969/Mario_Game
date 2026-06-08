package mario;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {

    private Rectangle shape;
    private double x, y, width, height;
    private boolean isGround;

    public Platform(double x, double y, double width, double height, boolean isGround) {
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
        this.isGround = isGround;

        shape = new Rectangle(width, height);
        shape.setX(x);
        shape.setY(y);

        if (isGround) {
            shape.setFill(Color.web("#5d4037"));   // dark brown
        } else {
            shape.setFill(Color.web("#8d6e63"));   // lighter brown
        }
        shape.setArcWidth(4);
        shape.setArcHeight(4);
    }

    // ── Collision helpers ─────────────────────────────────
    public boolean isPlayerOnTop(Player player) {
        double px = player.getX(), py = player.getY();
        double pw = player.getWidth(), ph = player.getHeight();

        boolean horizontalOverlap = px + pw > x + 4 && px < x + width - 4;
        boolean wasAbove  = (py + ph - player.getVelocityY()) <= y + 2;
        boolean nowBelow  = py + ph >= y;

        return horizontalOverlap && wasAbove && nowBelow && player.getVelocityY() >= 0;
    }

    public boolean isPlayerBelow(Player player) {
        double px = player.getX(), py = player.getY();
        double pw = player.getWidth();

        boolean horizontalOverlap = px + pw > x + 4 && px < x + width - 4;
        boolean wasBelow = (py - player.getVelocityY()) >= y + height - 2;
        boolean nowAbove = py <= y + height;

        return horizontalOverlap && wasBelow && nowAbove && player.getVelocityY() < 0;
    }

    // ── Getters ──────────────────────────────────────────
    public Rectangle getShape() { return shape; }
    public double getX()        { return x; }
    public double getY()        { return y; }
    public double getWidth()    { return width; }
    public double getHeight()   { return height; }
    public boolean isGround()   { return isGround; }
}
