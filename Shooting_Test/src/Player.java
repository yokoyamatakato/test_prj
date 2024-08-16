
import java.awt.event.KeyEvent;
import java.util.Set;

public class Player {
    private int x, y;
    private int speed;

    public Player(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update(Set<Integer> pressedKeys) {
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            y -= speed;
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            y += speed;
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            x -= speed;
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            x += speed;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
