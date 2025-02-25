import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class WinnerBanner {
    JFrame frame;
    GameFrame gameFrame;
    Clip musicClip;

    public WinnerBanner(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void display() {
        frame = new JFrame("You Won!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        setupBackground();
        playMusic();

        frame.setVisible(true);
    }

    private void setupBackground() {
        ImageIcon backgroundIcon = new ImageIcon("images/Win.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 600, 400);
        frame.add(backgroundLabel);

        setupButtons(backgroundLabel);
    }

    private void setupButtons(JLabel backgroundLabel) {
        JButton mainMenuButton = createButton("", "images/MainMenu2.png", 110, 310, 170, 50);
        JButton playAgainButton = createButton("", "images/Play Again.png", 350, 310, 170, 50);

        mainMenuButton.addActionListener(e -> {
            stopMusic();
            MainMenu mainMenu = new MainMenu();
            frame.dispose();
        });

        playAgainButton.addActionListener(e -> {
            stopMusic();
            gameFrame.restart();
            frame.dispose();
        });

        backgroundLabel.add(mainMenuButton);
        backgroundLabel.add(playAgainButton);
    }

    private JButton createButton(String text, String iconPath, int x, int y, int width, int height) {
        JButton button = new JButton(text, new ImageIcon(iconPath));
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private void playMusic() {
        try {
            File musicFile = new File("music/win music.wav");
            musicClip = AudioSystem.getClip();
            musicClip.open(AudioSystem.getAudioInputStream(musicFile));
            musicClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }
}