package mario;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class GameController implements Initializable {

    @FXML private Pane gamePane;
    @FXML private Label scoreLabel;
    @FXML private Label livesLabel;
    @FXML private Label statusLabel;
    private boolean pressA = false;
    private boolean pressD = false;
    private boolean levelClearing = false;

    private Player player;
    private List<mario.Platform> platforms = new ArrayList<>();
    private List<Enemy>          enemies   = new ArrayList<>();
    private List<Coin>           coins     = new ArrayList<>();
    private Rectangle door;

    private int score       = 0;
    private int lives       = 3;
    private int currentLevel = 1;
    private static final int MAX_LEVEL = 3;

    // Screen states
    private enum Screen { TITLE, LEVEL_SELECT, PLAYING, LEVEL_CLEAR, GAME_OVER, YOU_WIN }
    private Screen currentScreen = Screen.TITLE;

    private boolean gameRunning = false;
    private final Set<KeyCode> keysDown = new HashSet<>();
    private AnimationTimer gameLoop;

    // Overlay nodes (title / level select / etc.)
    private Pane overlayPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::attachKeyHandlers);
        Platform.runLater(this::showTitleScreen);
    }

    // ── Key handlers ──────────────────────────────────────
    private void attachKeyHandlers() {
        javafx.scene.Scene scene = gamePane.getScene();
        if (scene == null) { Platform.runLater(this::attachKeyHandlers); return; }

        gamePane.setFocusTraversable(true);
        gamePane.requestFocus();

        scene.setOnKeyPressed(e -> {
        	
            
            keysDown.add(e.getCode());

            // Character နဲ့ စစ် (UNDEFINED fix)
            String ch = e.getText().toLowerCase();
            if (ch.equals("a")) pressA = true;
            if (ch.equals("d")) pressD = true;

            // Jump
            if (currentScreen == Screen.PLAYING) {
                if (e.getCode() == KeyCode.SPACE ||
                    e.getCode() == KeyCode.UP    ||
                    e.getCode() == KeyCode.W     ||
                    ch.equals("w")) {
                    if (player != null && player.isAlive()) player.jump();
                }
            }

            // Title screen
            if (currentScreen == Screen.TITLE) {
                showLevelSelectScreen();
            }

            // Level select
            if (currentScreen == Screen.LEVEL_SELECT) {
                if (e.getCode() == KeyCode.DIGIT1 || ch.equals("1")) startLevel(1);
                else if (e.getCode() == KeyCode.DIGIT2 || ch.equals("2")) startLevel(2);
                else if (e.getCode() == KeyCode.DIGIT3 || ch.equals("3")) startLevel(3);
            }

            // Level clear
            if (currentScreen == Screen.LEVEL_CLEAR) {
                if (currentLevel < MAX_LEVEL) startLevel(currentLevel + 1);
                else showYouWinScreen();
            }

            // Game over
            if (currentScreen == Screen.GAME_OVER) {
                lives = 3; score = 0;
                showLevelSelectScreen();
            }

            // You win
            if (currentScreen == Screen.YOU_WIN) {
                lives = 3; score = 0; currentLevel = 1;
                showLevelSelectScreen();
            }
        });

        scene.setOnKeyReleased(e -> {
            keysDown.remove(e.getCode());
            String ch = e.getText().toLowerCase();
            if (ch.equals("a")) pressA = false;
            if (ch.equals("d")) pressD = false;
        });
    }

    // ── TITLE SCREEN ──────────────────────────────────────
    private void showTitleScreen() {
        currentScreen = Screen.TITLE;
        stopGame();
        gamePane.getChildren().clear();

        overlayPane = new Pane();
        overlayPane.setPrefSize(800, 460);
        overlayPane.setStyle("-fx-background-color: linear-gradient(to bottom, #0f3460, #16213e);");

        // Title
        Text title = makeText("MARIO GAME", 800/2.0, 160, 52, Color.web("#e94560"), true);
        Text sub   = makeText("JAVAFX EDITION", 800/2.0, 205, 18, Color.GOLD, false);
        Text press = makeText("CLICK ANYWHERE OR PRESS ANY KEY TO START", 800/2.0, 310, 16, Color.web("#a8dadc"), false);

        // Blinking animation for "press" text
        AnimationTimer blink = new AnimationTimer() {
            long last = 0;
            boolean visible = true;
            public void handle(long now) {
                if (now - last > 600_000_000L) {
                    visible = !visible;
                    press.setVisible(visible);
                    last = now;
                }
            }
        };
        blink.start();

        // Click to start
        overlayPane.setOnMouseClicked(e -> { blink.stop(); showLevelSelectScreen(); });

        overlayPane.getChildren().addAll(title, sub, press);
        gamePane.getChildren().add(overlayPane);

        statusLabel.setText("Mario Game  |  Press any key to start");
        scoreLabel.setText("Score: 0");
        livesLabel.setText("Lives: 3");
    }

    // ── LEVEL SELECT SCREEN ───────────────────────────────
    private void showLevelSelectScreen() {
        currentScreen = Screen.LEVEL_SELECT;
        stopGame();
        gamePane.getChildren().clear();

        overlayPane = new Pane();
        overlayPane.setPrefSize(800, 460);
        overlayPane.setStyle("-fx-background-color: linear-gradient(to bottom, #0f3460, #16213e);");

        Text title = makeText("SELECT LEVEL", 400, 100, 36, Color.web("#e94560"), true);

        // Level boxes
        String[] labels   = { "LEVEL 1", "LEVEL 2", "LEVEL 3" };
        String[] descs    = { "Easy — 3 Enemies", "Medium — 5 Enemies", "Hard — 7 Enemies" };
        String[] keys     = { "Press  1", "Press  2", "Press  3" };
        Color[]  colors   = { Color.web("#4caf50"), Color.web("#ff9800"), Color.web("#f44336") };
        double[] xPos     = { 80, 310, 540 };
        int[]    lvlNums  = { 1, 2, 3 };

        for (int i = 0; i < 3; i++) {
            final int lvl = lvlNums[i];
            Rectangle box = new Rectangle(180, 200, Color.web("#1a3a6e"));
            box.setX(xPos[i]);
            box.setY(160);
            box.setArcWidth(16);
            box.setArcHeight(16);
            box.setStroke(colors[i]);
            box.setStrokeWidth(3);

            Text lbl  = makeText(labels[i],  xPos[i] + 90, 240, 20, colors[i], true);
            Text desc = makeText(descs[i],   xPos[i] + 90, 275, 13, Color.WHITE, false);
            Text key  = makeText(keys[i],    xPos[i] + 90, 320, 15, Color.GOLD, false);

            // Click to select level
            box.setOnMouseClicked(e -> startLevel(lvl));
            box.setOnMouseEntered(e -> box.setFill(Color.web("#253d6e")));
            box.setOnMouseExited(e  -> box.setFill(Color.web("#1a3a6e")));

            overlayPane.getChildren().addAll(box, lbl, desc, key);
        }

        overlayPane.getChildren().add(title);
        gamePane.getChildren().add(overlayPane);
        statusLabel.setText("Press 1 / 2 / 3 to select level  —  or click a box");
    }

    // ── START LEVEL ───────────────────────────────────────
    private void startLevel(int level) {
    	
        currentLevel = level;
        currentLevel = level;
        currentScreen = Screen.PLAYING;
        keysDown.clear(); // ← ဒါထည့်
        pressA = false;   // ← ဒါထည့်
        pressD = false;   // ← ဒါထည့်
        levelClearing = false;
        currentScreen = Screen.PLAYING;
        gamePane.getChildren().clear();
        platforms.clear(); enemies.clear(); coins.clear();
        gamePane.requestFocus();

        // Build from LevelData
        for (LevelData.PlatformDef pd : LevelData.getPlatforms(level)) {
            mario.Platform p = new mario.Platform(pd.x, pd.y, pd.w, pd.h, pd.ground);
            platforms.add(p);
            gamePane.getChildren().add(p.getShape());
        }

        for (LevelData.EnemyDef ed : LevelData.getEnemies(level)) {
            Enemy e = new Enemy(ed.x, ed.y, ed.speed);
            enemies.add(e);
            gamePane.getChildren().add(e.getShape());
        }

        for (LevelData.CoinDef cd : LevelData.getCoins(level)) {
            Coin c = new Coin(cd.x, cd.y);
            coins.add(c);
            gamePane.getChildren().add(c.getShape());
        }

        // Door (exit)
        LevelData.DoorDef dd = LevelData.getDoor(level);
        door = new Rectangle(36, 56, Color.web("#ffd700"));
        door.setX(dd.x); door.setY(dd.y);
        door.setArcWidth(6); door.setArcHeight(6);
        door.setStroke(Color.web("#ff8f00")); door.setStrokeWidth(3);
        // Door label
        Text doorTxt = makeText("EXIT", dd.x + 18, dd.y + 35, 11, Color.web("#212121"), true);
        gamePane.getChildren().addAll(door, doorTxt);

        // Player
        player = new Player(60, 400);
        gamePane.getChildren().add(player.getShape());

        statusLabel.setText("Level " + level + "  |  ← → Move  SPACE Jump  |  Reach the EXIT door!");
        updateHUD();
        startGameLoop();
    }

    // ── GAME LOOP ─────────────────────────────────────────
    private void startGameLoop() {
        gameRunning = true;
        if (gameLoop != null) gameLoop.stop();
        gameLoop = new AnimationTimer() {
            public void handle(long now) { update(); }
        };
        gameLoop.start();
    }

    private void update() {
        if (!gameRunning || currentScreen != Screen.PLAYING) return;

        boolean left  = keysDown.contains(KeyCode.LEFT)  || pressA;
        boolean right = keysDown.contains(KeyCode.RIGHT) || pressD;

        // ── Player ────────────────────────────────────────
        player.setOnGround(false);
        player.update(left, right);

        for (mario.Platform p : platforms) {
            if (p.isPlayerOnTop(player))  player.landOn(p.getY());
            if (p.isPlayerBelow(player))  player.hitFromBelow(p.getY() + p.getHeight());
        }

        // Fell off screen
        if (player.getY() > 520) { loseLife(); return; }

        // ── Enemies ───────────────────────────────────────
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            if (!enemy.isAlive()) {
                it.remove(); gamePane.getChildren().remove(enemy.getShape()); continue;
            }

            enemy.update();

            // Enemy platform collision + edge detection
            for (mario.Platform p : platforms) {
                double ex = enemy.getX(), ey = enemy.getY();
                double ew = enemy.getWidth(), eh = enemy.getHeight();
                double px = p.getX(), py = p.getY(), pw = p.getWidth();

                // Land on platform
                boolean hOv  = ex + ew > px + 2 && ex < px + pw - 2;
                boolean fall = enemy.getVelocityY() >= 0;
                if (hOv && fall && (ey + eh - enemy.getVelocityY()) <= py + 2 && ey + eh >= py) {
                    enemy.landOn(py);
                }

                // Edge detection — platform ကနေ မကျသွားအောင် reverse
                boolean onThisPlatform = ex + ew > px && ex < px + pw
                                      && Math.abs((ey + eh) - py) < 5;
                if (onThisPlatform) {
                    if (ex <= px + 2 || ex + ew >= px + pw - 2) {
                        enemy.reverseDirection();
                    }
                }
            }

            if (enemy.shouldRemove()) {
                it.remove(); gamePane.getChildren().remove(enemy.getShape()); continue;
            }

            // Player-enemy collision
            if (!enemy.isDead() && enemy.intersects(player)) {
                boolean falling = player.getVelocityY() > 0;
                boolean above   = player.getY() + player.getHeight()
                                  < enemy.getY() + enemy.getHeight() * 0.6;
                if (falling && above) {
                    enemy.stomp(); score += 100; updateHUD(); player.jump();
                } else {
                    loseLife(); return;
                }
            }
        }

        // ── Coins ─────────────────────────────────────────
        for (Coin coin : coins) {
            if (!coin.isCollected() && coin.intersects(player)) {
                coin.collect(); score += 10; updateHUD();
            }
        }

     // ── Door collision — level clear ───────────────────
        if (door != null && !levelClearing &&
                player.getShape().getBoundsInParent()
                .intersects(door.getBoundsInParent())) {
            levelClearing = true;
            score += 500;
            updateHUD();
            showLevelClearScreen();
        }
    }

    // ── LEVEL CLEAR SCREEN ────────────────────────────────
    private void showLevelClearScreen() {
        currentScreen = Screen.LEVEL_CLEAR;
        stopGame();

        Pane overlay = new Pane();
        overlay.setPrefSize(800, 460);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");

        String nextMsg = (currentLevel < MAX_LEVEL)
            ? "Press any key for Level " + (currentLevel + 1)
            : "Press any key for YOU WIN screen";

        Text t1 = makeText("LEVEL " + currentLevel + " CLEAR!", 400, 180, 42, Color.GOLD, true);
        Text t2 = makeText("+500 Bonus Points!", 400, 235, 20, Color.web("#a8dadc"), false);
        Text t3 = makeText("Score: " + score, 400, 270, 18, Color.WHITE, false);
        Text t4 = makeText(nextMsg, 400, 330, 16, Color.LIGHTGREEN, false);

        overlay.getChildren().addAll(t1, t2, t3, t4);
        gamePane.getChildren().add(overlay);
        statusLabel.setText("Level " + currentLevel + " Clear!  |  Press any key to continue");
    }

    // ── GAME OVER SCREEN ──────────────────────────────────
    private void showGameOverScreen() {
        currentScreen = Screen.GAME_OVER;
        stopGame();

        Pane overlay = new Pane();
        overlay.setPrefSize(800, 460);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.75);");

        Text t1 = makeText("GAME OVER", 400, 180, 48, Color.web("#e94560"), true);
        Text t2 = makeText("Score: " + score, 400, 240, 22, Color.WHITE, false);
        Text t3 = makeText("Press any key to return to Level Select", 400, 310, 15, Color.web("#a8dadc"), false);

        overlay.getChildren().addAll(t1, t2, t3);
        gamePane.getChildren().add(overlay);
        statusLabel.setText("Game Over  |  Press any key to return");
    }

    // ── YOU WIN SCREEN ────────────────────────────────────
    private void showYouWinScreen() {
        currentScreen = Screen.YOU_WIN;
        stopGame();
        gamePane.getChildren().clear();

        overlayPane = new Pane();
        overlayPane.setPrefSize(800, 460);
        overlayPane.setStyle("-fx-background-color: linear-gradient(to bottom, #0f3460, #16213e);");

        Text t1 = makeText("YOU WIN!", 400, 160, 56, Color.GOLD, true);
        Text t2 = makeText("All 3 Levels Cleared!", 400, 215, 22, Color.web("#a8dadc"), false);
        Text t3 = makeText("Final Score: " + score, 400, 260, 24, Color.WHITE, false);
        Text t4 = makeText("Press any key to play again", 400, 330, 16, Color.LIGHTGREEN, false);

        overlayPane.getChildren().addAll(t1, t2, t3, t4);
        gamePane.getChildren().add(overlayPane);
        statusLabel.setText("YOU WIN!  Final Score: " + score + "  |  Press any key");
    }

    // ── Life helpers ──────────────────────────────────────
    private void loseLife() {
        lives--;
        updateHUD();
        if (lives <= 0) {
            showGameOverScreen();
        } else {
            gamePane.getChildren().remove(player.getShape());
            player = new Player(60, 400);
            gamePane.getChildren().add(player.getShape());
        }
    }

    private void stopGame() {
        gameRunning = false;
        keysDown.clear();
        if (gameLoop != null) gameLoop.stop();
    }

    private void updateHUD() {
        scoreLabel.setText("Score: " + score);
        livesLabel.setText("Lives: " + lives);
    }

    // ── Text helper ───────────────────────────────────────
    private Text makeText(String str, double cx, double cy, double size, Color color, boolean bold) {
        Text t = new Text(str);
        t.setFont(bold ? Font.font("Arial", FontWeight.BOLD, size) : Font.font("Arial", size));
        t.setFill(color);
        t.setTextAlignment(TextAlignment.CENTER);
        // Center horizontally by approximating text width
        t.setX(cx - str.length() * size * 0.3);
        t.setY(cy);
        return t;
    }
}
