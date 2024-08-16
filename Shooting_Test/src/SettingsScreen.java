import javax.swing.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsScreen extends JFrame {
    private Clip bgmClip;
    private float bgmVolume = 0.3f;  // 初期音量を3割に設定

    public SettingsScreen(Clip bgmClip) {
        this.bgmClip = bgmClip;

        setTitle("Settings");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 音量スライダー
        JSlider volumeSlider = new JSlider(0, 100, (int) (bgmVolume * 100));
        volumeSlider.addChangeListener(e -> bgmVolume = volumeSlider.getValue() / 100f);

        // 適用ボタン
        JButton applyButton = new JButton("適用");
        applyButton.setPreferredSize(new Dimension(100, 30)); // ボタンサイズを調整
        applyButton.addActionListener(e -> {
            applySettings();
            new TitleScreen(bgmVolume).setVisible(true);
            this.dispose(); // 設定画面を閉じる
        });

        // 戻るボタン
        JButton backButton = new JButton("戻る");
        backButton.setPreferredSize(new Dimension(100, 30)); // ボタンサイズを調整
        backButton.addActionListener(e -> {
            new TitleScreen(bgmVolume).setVisible(true);
            this.dispose(); // 設定画面を閉じる
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel("BGM 音量"), BorderLayout.NORTH);
        panel.add(volumeSlider, BorderLayout.CENTER);

        // ボタンを中央に配置
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void applySettings() {
        if (bgmClip != null) {
            FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (20.0 * Math.log10(bgmVolume));
            gainControl.setValue(dB);
        }
    }

    public float getBgmVolume() {
        return bgmVolume;
    }
}
