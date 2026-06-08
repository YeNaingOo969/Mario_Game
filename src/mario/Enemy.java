package mario;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Enemy {

    private Rectangle shape;
    private double x, y;
    private double velocityX;
    private double velocityY = 0;
    private boolean alive = true;
    private boolean dead  = false;

    private static final double WIDTH  = 30;
    private static final double HEIGHT = 30;
    private static final double GRAVITY = 0.5;

    private int squishTimer = 0;
    private double moveSpeed;

    public Enemy(double startX, double startY, double speed) {
        this.x = startX;
        this.y = startY;
        this.moveSpeed = speed;
        this.velocityX = -speed;

        shape = new Rectangle(WIDTH, HEIGHT);
        shape.setFill(Color.web("#795548"));
        shape.setArcWidth(8);
        shape.setArcHeight(8);
        shape.setX(x);
        shape.setY(y);
    }

    public void update() {
        if (!alive) return;
        if (dead) { squishTimer++; return; }

        velocityY += GRAVITY;
        if (velocityY > 12) velocityY = 12;

        x += velocityX;
        y += velocityY;

        if (x <= 0)          { x = 0;          velocityX =  moveSpeed; }
        if (x + WIDTH > 800) { x = 800 - WIDTH; velocityX = -moveSpeed; }

        shape.setX(x);
        shape.setY(y);
    }

    public void landOn(double platformTop) {
        y = platformTop - HEIGHT;
        velocityY = 0;
        shape.setY(y);
    }
    

    public void stomp() {
        dead = true;
        squishTimer = 0;
        shape.setHeight(10);
        shape.setY(y + HEIGHT - 10);
        shape.setFill(Color.web("#4e342e"));
    }
    public void reverseDirection() {
        velocityX = -velocityX;
    }

    public boolean shouldRemove() { return dead && squishTimer > 30; }

    public Rectangle getShape()  { return shape; }
    public double getX()         { return x; }
    public double getY()         { return y; }
    public double getWidth()     { return WIDTH; }
    public double getHeight()    { return HEIGHT; }
    public double getVelocityY() { return velocityY; }
    public boolean isAlive()     { return alive; }
    public boolean isDead()      { return dead; }

    public void setAlive(boolean alive) { this.alive = alive; }

    public boolean intersects(Player player) {
        return shape.getBoundsInParent().intersects(player.getShape().getBoundsInParent());
    }
}
