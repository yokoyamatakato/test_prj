
public class Bullet {
    private int x, y;
    private int speed;

    public Bullet(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update() {
        y -= speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
