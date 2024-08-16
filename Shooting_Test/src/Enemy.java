import java.util.List;  // これを追加します
import java.util.Random;

public class Enemy {
    private int x, y;
    private int speed;
    private int hp;
    private int maxHp;
    private String name;

    public Enemy(int x, int y, int speed, int hp, String name) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.hp = hp;
        this.maxHp = hp;
        this.name = name;
    }

    public void update(List<Bullet> bullets) {
        Bullet nearestBullet = null;
        int minDistance = Integer.MAX_VALUE;

        for (Bullet bullet : bullets) {
            int distance = Math.abs(bullet.getX() - this.x);
            if (distance < minDistance) {
                minDistance = distance;
                nearestBullet = bullet;
            }
        }

        // Improved avoidance logic with a wider reaction range
        if (nearestBullet != null && minDistance < 300) {  // Increased reaction range
            if (nearestBullet.getX() < this.x) {
                x += speed;  // Move right to avoid the bullet
            } else if (nearestBullet.getX() > this.x) {
                x -= speed;  // Move left to avoid the bullet
            }
        }
    }

    public void decreaseHp(int amount) {
        this.hp -= amount;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
