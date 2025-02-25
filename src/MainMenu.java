import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MainMenu {
    JFrame frame;
    JButton startButton;
    JButton exitButton;
    JButton instructionsButton;
    JLabel titleLabel;
    JLabel backgroundLabel;
    Clip musicClip;

    public MainMenu() {
        frame = new JFrame("NumberSum - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        setupBackground();
        setupTitle();
        setupButtons();
        addListeners();
        playMusic("music/main menu.wav");

        frame.setVisible(true);
    }

    private void setupBackground() {
        ImageIcon backgroundIcon = new ImageIcon("images/matrix.gif");
        backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 800, 600);
        frame.add(backgroundLabel);
    }

    private void setupTitle() {
        ImageIcon titleIcon = new ImageIcon("images/title.png");
        JLabel titleImageLabel = new JLabel(titleIcon);
        titleImageLabel.setBounds(175, 10, 450, 250);
        backgroundLabel.add(titleImageLabel);

        titleLabel = new JLabel("NumberSum", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(175, 130, 450, 100);
        backgroundLabel.add(titleLabel);
    }

    private void setupButtons() {
        startButton = createButton("", "images/Play.png");
        instructionsButton = createButton("", "images/How to Play.png");
        exitButton = createButton("", "images/Exit.png");

        startButton.setBounds(295, 250, 210, 50);
        instructionsButton.setBounds(300, 350, 200, 50);
        exitButton.setBounds(295, 450, 210, 50);

        backgroundLabel.add(startButton);
        backgroundLabel.add(instructionsButton);
        backgroundLabel.add(exitButton);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text, new ImageIcon(iconPath));
        button.setFont(new Font("Arial", Font.BOLD, 20));
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

    private void addListeners() {
        startButton.addActionListener(e -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.display();
            stopMusic();
            frame.dispose();
        });

        exitButton.addActionListener(e -> System.exit(0));

        instructionsButton.addActionListener(e -> showInstructions());
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(frame,
                "How to Play:\n" +
                        "1. Choose a difficulty level to start.\n" +
                        "2. The grid contains numbers and sum clues for each row and column.\n" +
                        "3. Adjust the grid by clicking numbers to either keep or delete them.\n" +
                        "4. Ensure each row and column matches the given sum clues.\n" +
                        "5. Use the buttons to check your progress, reset the grid, or undo actions.",
                "Game Instructions", JOptionPane.INFORMATION_MESSAGE);    }

    private void playMusic(String musicPath) {
        try {
            File musicFile = new File(musicPath);
            musicClip = AudioSystem.getClip();
            musicClip.open(AudioSystem.getAudioInputStream(musicFile));
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}