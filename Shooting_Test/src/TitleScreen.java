import javax.swing.*;
import java.awt.*;

public class TitleScreen extends JFrame {
    private float bgmVolume;

    public TitleScreen(float bgmVolume) {
        this.bgmVolume = bgmVolume;
        setTitle("Title Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // タイトル画面のパネル
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        // プレイボタン
        JButton playButton = new JButton("プレイ");
        playButton.setPreferredSize(new Dimension(150, 40)); // ボタンサイズを調整
        playButton.addActionListener(e -> {
            new ShootingGame(new JFrame(), bgmVolume);
            this.dispose(); // タイトル画面を閉じる
        });

        // 設定ボタン
        JButton settingsButton = new JButton("設定");
        settingsButton.setPreferredSize(new Dimension(150, 40)); // ボタンサイズを調整
        settingsButton.addActionListener(e -> {
            new SettingsScreen(null).setVisible(true);
            this.dispose(); // タイトル画面を閉じる
        });

        // やめるボタン
        JButton exitButton = new JButton("やめる");
        exitButton.setPreferredSize(new Dimension(150, 40)); // ボタンサイズを調整
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(playButton);
        panel.add(settingsButton);
        panel.add(exitButton);

        add(panel);
        pack();  // フレームサイズをボタンに合わせる
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TitleScreen(0.3f).setVisible(true);  // 初期音量3割でスタート
        });
    }
}
