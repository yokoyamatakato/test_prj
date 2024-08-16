import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShootingGame extends JPanel implements KeyListener {
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private Image playerImage;
    private Image enemyImage;
    private Set<Integer> pressedKeys;
    private Clip bgmClip;
    private JFrame parentFrame;
    private float bgmVolume; // Store the volume setting

    public ShootingGame(JFrame parentFrame, float bgmVolume) {
        this.parentFrame = parentFrame;
        this.bgmVolume = bgmVolume; // Initialize the volume
        parentFrame.setTitle("Shooting Game");
        parentFrame.setSize(1600, 1200);
        parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parentFrame.add(this);
        parentFrame.setLocationRelativeTo(null);
        parentFrame.setVisible(true);

        player = new Player(400, 500, 8);
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        enemies.add(new Enemy(800, 100, 5, 1000, "Enemy 1"));
        loadImages("C://sources//test_prj//Shooting_Test//assets//pic//player.png", "C://sources//test_prj//Shooting_Test//assets//pic//enemy.png");
        pressedKeys = new HashSet<>();
        
        // BGM再生
        bgmClip = playBGM("C://sources//test_prj//Shooting_Test//assets//bgm//sample.wav", bgmVolume);

        Timer timer = new Timer(20, e -> {
            player.update(pressedKeys);
            updateBullets();
            updateEnemies();
            repaint();
        });
        timer.start();
        setFocusable(true);
        addKeyListener(this);
    }

    private void loadImages(String playerImagePath, String enemyImagePath) {
        try {
            playerImage = ImageIO.read(new File(playerImagePath));
            enemyImage = ImageIO.read(new File(enemyImagePath));

            if (playerImage == null || enemyImage == null) {
                throw new IOException("Image files could not be loaded.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "画像ファイルの読み込みに失敗しました。ファイルパスを確認してください。",
                    "エラー", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void updateBullets() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.getY() < 0) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    private void updateEnemies() {
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Enemy enemy : enemies) {
            enemy.update(bullets);
            
            // ビームと敵の当たり判定
            for (Bullet bullet : bullets) {
                if (bullet.getX() >= enemy.getX() && bullet.getX() <= enemy.getX() + 180 &&
                    bullet.getY() >= enemy.getY() && bullet.getY() <= enemy.getY() + 180) {
                    enemy.decreaseHp(5);  // HPを減少
                    bulletsToRemove.add(bullet);  // 被弾したビームを削除
                }
            }

            if (!enemy.isAlive()) {
                enemiesToRemove.add(enemy);
                stopBGM();  // 敵が全滅したらBGMを停止
                showGameOverOptions();
            }
        }

        enemies.removeAll(enemiesToRemove);
        bullets.removeAll(bulletsToRemove);
    }

    private void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
        }
    }

    private void showGameOverOptions() {
        int option = JOptionPane.showOptionDialog(this,
            "ゲームクリア！",
            "ゲームクリア",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[] {"もう一度", "タイトルに戻る"},
            "もう一度");

        if (option == 0) {
            resetGame();
        } else if (option == 1) {
            parentFrame.dispose();
            new TitleScreen(bgmVolume).setVisible(true);  // Pass the volume back to TitleScreen
        }
    }

    private void resetGame() {
        parentFrame.dispose();
        new ShootingGame(new JFrame(), bgmVolume); // Restart the game with the current volume
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // プレイヤーの描画
        g.drawImage(playerImage, player.getX(), player.getY(), this);

        // 敵の描画
        for (Enemy enemy : enemies) {
            g.drawImage(enemyImage, enemy.getX(), enemy.getY(), this);
            drawHealthBar(g, enemy);
        }

        // 弾の描画
        for (Bullet bullet : bullets) {
            g.setColor(Color.RED);
            g.fillRect(bullet.getX(), bullet.getY(), 5, 10);
        }
    }

    private void drawHealthBar(Graphics g, Enemy enemy) {
        int barWidth = 100;
        int barHeight = 10;
        int xOffset = -25;
        int yOffset = -20;
        int healthBarX = enemy.getX() + xOffset;
        int healthBarY = enemy.getY() + yOffset;

        g.setColor(Color.GRAY);
        g.fillRect(healthBarX, healthBarY, barWidth, barHeight);

        g.setColor(Color.GREEN);
        int healthWidth = (int) (((double) enemy.getHp() / enemy.getMaxHp()) * barWidth);
        g.fillRect(healthBarX, healthBarY, healthWidth, barHeight);

        g.setColor(Color.BLACK);
        g.drawRect(healthBarX, healthBarY, barWidth, barHeight);

        g.drawString(enemy.getName(), healthBarX, healthBarY - 5);
    }

    private Clip playBGM(String bgmPath, float volume) {
        try {
            File soundFile = new File(bgmPath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);  // Corrected method name
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // 音量設定を30%にする
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);

            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        bullets.add(new Bullet(player.getX() + playerImage.getWidth(null) / 2 - 2, player.getY(), 10));
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || 
        e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
        // プレイヤーの移動ロジック
    }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
