package mario;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds static level definitions for Level 1, 2, 3.
 * Each level is harder: more enemies, faster enemies, more gaps.
 */
public class LevelData {

    public static class EnemyDef {
        public double x, y, speed;
        public EnemyDef(double x, double y, double speed) {
            this.x = x; this.y = y; this.speed = speed;
        }
    }

    public static class PlatformDef {
        public double x, y, w, h;
        public boolean ground;
        public PlatformDef(double x, double y, double w, double h, boolean ground) {
            this.x = x; this.y = y; this.w = w; this.h = h; this.ground = ground;
        }
    }

    public static class CoinDef {
        public double x, y;
        public CoinDef(double x, double y) { this.x = x; this.y = y; }
    }

    public static class DoorDef {
        public double x, y;
        public DoorDef(double x, double y) { this.x = x; this.y = y; }
    }

    // ── LEVEL 1 — Easy ────────────────────────────────────
    public static List<PlatformDef> getPlatforms(int level) {
        List<PlatformDef> list = new ArrayList<>();
        if (level == 1) {
            list.add(new PlatformDef(0,   460, 800, 40, true));
            list.add(new PlatformDef(150, 370, 120, 20, false));
            list.add(new PlatformDef(320, 310, 100, 20, false));
            list.add(new PlatformDef(480, 350, 140, 20, false));
            list.add(new PlatformDef(650, 270, 110, 20, false));
            list.add(new PlatformDef(50,  250, 100, 20, false));
        } else if (level == 2) {
            // Gaps in the ground, more floating platforms
            list.add(new PlatformDef(0,   460, 300, 40, true));
            list.add(new PlatformDef(360, 460, 200, 40, true));
            list.add(new PlatformDef(620, 460, 180, 40, true));
            list.add(new PlatformDef(100, 370, 100, 20, false));
            list.add(new PlatformDef(260, 310, 90,  20, false));
            list.add(new PlatformDef(400, 260, 80,  20, false));
            list.add(new PlatformDef(550, 300, 110, 20, false));
            list.add(new PlatformDef(680, 230, 100, 20, false));
            list.add(new PlatformDef(30,  200, 80,  20, false));
        } else {
            // Level 3 — many gaps, small platforms
            list.add(new PlatformDef(0,   460, 180, 40, true));
            list.add(new PlatformDef(260, 460, 120, 40, true));
            list.add(new PlatformDef(460, 460, 100, 40, true));
            list.add(new PlatformDef(650, 460, 150, 40, true));
            list.add(new PlatformDef(80,  370,  80, 20, false));
            list.add(new PlatformDef(220, 310,  70, 20, false));
            list.add(new PlatformDef(360, 260,  80, 20, false));
            list.add(new PlatformDef(490, 210,  70, 20, false));
            list.add(new PlatformDef(610, 260,  90, 20, false));
            list.add(new PlatformDef(700, 190,  80, 20, false));
            list.add(new PlatformDef(30,  200,  70, 20, false));
            list.add(new PlatformDef(160, 150,  70, 20, false));
        }
        return list;
    }

    public static List<EnemyDef> getEnemies(int level) {
        List<EnemyDef> list = new ArrayList<>();
        if (level == 1) {
            list.add(new EnemyDef(300, 420, 1.5));
            list.add(new EnemyDef(550, 420, 1.5));
            list.add(new EnemyDef(400, 270, 1.5));
        } else if (level == 2) {
            list.add(new EnemyDef(400, 420, 2.0));  // 50 → 400 ပြောင်း
            list.add(new EnemyDef(500, 420, 2.0));  // 150 → 500 ပြောင်း
            list.add(new EnemyDef(650, 420, 2.0));
            list.add(new EnemyDef(750, 420, 2.0));
            list.add(new EnemyDef(300, 270, 2.0));
        } else {
            list.add(new EnemyDef(300, 420, 2.8));  // 50 → 300 ပြောင်း
            list.add(new EnemyDef(400, 420, 2.8));
            list.add(new EnemyDef(500, 420, 2.8));
            list.add(new EnemyDef(700, 420, 2.8));
            list.add(new EnemyDef(500, 340, 2.8));
            list.add(new EnemyDef(400, 230, 2.8));
            list.add(new EnemyDef(650, 230, 2.8));
        }
        return list;
    }

    public static List<CoinDef> getCoins(int level) {
        List<CoinDef> list = new ArrayList<>();
        if (level == 1) {
            list.add(new CoinDef(180, 340)); list.add(new CoinDef(210, 340));
            list.add(new CoinDef(350, 280));
            list.add(new CoinDef(510, 320)); list.add(new CoinDef(545, 320));
            list.add(new CoinDef(680, 240));
            list.add(new CoinDef(80,  220));
        } else if (level == 2) {
            list.add(new CoinDef(120, 340)); list.add(new CoinDef(150, 340));
            list.add(new CoinDef(280, 280)); list.add(new CoinDef(310, 280));
            list.add(new CoinDef(420, 230)); list.add(new CoinDef(450, 230));
            list.add(new CoinDef(570, 270)); list.add(new CoinDef(600, 270));
            list.add(new CoinDef(700, 200)); list.add(new CoinDef(50,  170));
        } else {
            list.add(new CoinDef(90,  340)); list.add(new CoinDef(230, 280));
            list.add(new CoinDef(375, 230)); list.add(new CoinDef(505, 180));
            list.add(new CoinDef(625, 230)); list.add(new CoinDef(715, 160));
            list.add(new CoinDef(45,  170)); list.add(new CoinDef(175, 120));
            list.add(new CoinDef(300, 120)); list.add(new CoinDef(430, 120));
        }
        return list;
    }

    /** Door position for each level — placed on the last platform */
    public static DoorDef getDoor(int level) {
        if (level == 1) return new DoorDef(670, 220);
        if (level == 2) return new DoorDef(700, 180);
        return new DoorDef(720, 140);
    }
}
