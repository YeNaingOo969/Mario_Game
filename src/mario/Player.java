package mario;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {

    private Rectangle shape;
    private double x, y;
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean onGround = false;
    private boolean alive = true;

    private static final double WIDTH  = 32;
    private static final double HEIGHT = 40;
    private static final double MOVE_SPEED   = 4.0;
    private static final double JUMP_FORCE   = -14.0;
    private static final double GRAVITY      = 0.5;
    private static final double MAX_FALL_SPEED = 12.0;

    public Player(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        shape = new Rectangle(WIDTH, HEIGHT);
        shape.setFill(Color.RED);
        shape.setArcWidth(6);
        shape.setArcHeight(6);
        shape.setX(x);
        shape.setY(y);
    }

    public void update(boolean movingLeft, boolean movingRight) {
        // Horizontal movement
        if (movingLeft)  velocityX = -MOVE_SPEED;
        else if (movingRight) velocityX = MOVE_SPEED;
        else velocityX = 0;

        // Gravity
        if (!onGround) {
            velocityY += GRAVITY;
            if (velocityY > MAX_FALL_SPEED) velocityY = MAX_FALL_SPEED;
        }

        x += velocityX;
        y += velocityY;

        // Keep within screen bounds (horizontal)
        if (x < 0) x = 0;
        if (x + WIDTH > 800) x = 800 - WIDTH;

        shape.setX(x);
        shape.setY(y);
    }

    public void jump() {
        if (onGround) {
            velocityY = JUMP_FORCE;
            onGround = false;
        }
    }

    public void landOn(double platformTop) {
        y = platformTop - HEIGHT;
        velocityY = 0;
        onGround = true;
        shape.setY(y);
    }

    public void hitFromBelow(double platformBottom) {
        y = platformBottom;
        velocityY = 2;
        shape.setY(y);
    }

    public void die() {
        alive = false;
        velocityY = -8;
    }

    // ── Getters ──────────────────────────────────────────
    public Rectangle getShape()   { return shape; }
    public double getX()          { return x; }
    public double getY()          { return y; }
    public double getWidth()      { return WIDTH; }
    public double getHeight()     { return HEIGHT; }
    public double getVelocityY()  { return velocityY; }
    public boolean isOnGround()   { return onGround; }
    public boolean isAlive()      { return alive; }

    public void setOnGround(boolean onGround) { this.onGround = onGround; }

    public boolean intersects(Rectangle other) {
        return shape.getBoundsInParent().intersects(other.getBoundsInParent());
    }
}
