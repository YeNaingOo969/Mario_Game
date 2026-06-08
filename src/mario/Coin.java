package mario;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Coin {

    private Circle shape;
    private double x, y;
    private boolean collected = false;

    private static final double RADIUS = 10;

    public Coin(double x, double y) {
        this.x = x;
        this.y = y;
        shape = new Circle(RADIUS, Color.GOLD);
        shape.setCenterX(x);
        shape.setCenterY(y);
        shape.setStroke(Color.ORANGE);
        shape.setStrokeWidth(2);
    }

    public void collect() {
        collected = true;
        shape.setVisible(false);
    }

    public boolean isCollected() { return collected; }

    public boolean intersects(Player player) {
        if (collected) return false;
        return shape.getBoundsInParent().intersects(player.getShape().getBoundsInParent());
    }

    public Circle getShape() { return shape; }
    public double getX()     { return x; }
    public double getY()     { return y; }
}
