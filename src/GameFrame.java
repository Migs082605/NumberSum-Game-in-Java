import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class GameFrame {
    JFrame frame;
    JPanel gridPanel;
    MainGame mainGame;
    JLabel timerLabel;
    Timer timer;
    int timeRemaining;
    boolean timerStarted;
    Clip musicClip;

    public void display() {
        frame = new JFrame("NumberSum Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel difficultyPanel = createDifficultyPanel();
        frame.add(difficultyPanel, BorderLayout.NORTH);

        gridPanel = new JPanel();
        frame.add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Select Difficulty: ");
        label.setFont(new Font("Times New Roman", Font.BOLD, 20));
        panel.add(label);

        JButton easyButton = createButton("", "images/Easy.png");
        JButton mediumButton = createButton("", "images/Medium.png");
        JButton hardButton = createButton("", "images/Hard.png");

        panel.add(easyButton);
        panel.add(mediumButton);
        panel.add(hardButton);

        timerLabel = new JLabel("Time: --:--");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(timerLabel);

        easyButton.addActionListener(e -> setupGame(4, "easy"));
        mediumButton.addActionListener(e -> setupGame(5, "medium"));
        hardButton.addActionListener(e -> setupGame(6, "hard"));

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton checkButton = createButton("", "images/Submit.png");
        JButton resetButton = createButton("", "images/Reset.png");
        JButton undoButton = createButton("", "images/Undo.png");
        JButton newPuzzleButton = createButton("", "images/New Puzzle.png");

        checkButton.addActionListener(e -> {
            if (mainGame.checkSolution()) {
                stopTimerAndMusic();
                showWinnerBanner();
            } else {
                stopTimerAndMusic();
                showLoserBanner();
            }
        });
        resetButton.addActionListener(e -> mainGame.resetGrid());
        undoButton.addActionListener(e -> mainGame.undo());
        newPuzzleButton.addActionListener(e -> {
            if (mainGame != null) {
                int gridSize = mainGame.getGridSize();
                String difficulty = mainGame.getDifficulty();
                setupGame(gridSize, difficulty);
            }
        });

        panel.add(checkButton);
        panel.add(resetButton);
        panel.add(undoButton);
        panel.add(newPuzzleButton);

        return panel;
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

    private void setupGame(int gridSize, String difficulty) {
        stopTimerAndMusic(); // Stop current music and timer

        mainGame = new MainGame(gridSize, difficulty);
        frame.remove(gridPanel);
        gridPanel = mainGame.createGrid();
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        if (!difficulty.equalsIgnoreCase("easy")) {
            setupTimer(difficulty);
        } else {
            timerLabel.setText("Time: --:--");
        }
        addGridClickListener(difficulty);
    }

    private void setupTimer(String difficulty) {
        if (timer != null) {
            timer.stop();
        }

        timerStarted = false;
        switch (difficulty.toLowerCase()) {
            case "medium":
                timeRemaining = 60;
                timerLabel.setText("Time: 01:00");
                break;
            case "hard":
                timeRemaining = 180;
                timerLabel.setText("Time: 03:00");
                break;
        }
    }

    private void startTimerAndMusic(String difficulty) {
        if (timerStarted) {
            return;
        }

        timerStarted = true;
        startMusic(difficulty);

        if (!difficulty.equalsIgnoreCase("easy")) {
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timeRemaining--;
                    timerLabel.setText("Time: " + formatTime(timeRemaining));
                    if (timeRemaining <= 0) {
                        stopTimerAndMusic();
                        showLoserBanner();
                    }
                }
            });
            timer.start();
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void addGridClickListener(String difficulty) {
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.addActionListener(e -> startTimerAndMusic(difficulty));
            }
        }
    }

    private void startMusic(String difficulty) {
        String musicPath;
        switch (difficulty.toLowerCase()) {
            case "easy":
                musicPath = "music/easy music.wav";
                break;
            case "medium":
                musicPath = "music/medium music.wav";
                break;
            case "hard":
                musicPath = "music/hard music.wav";
                break;
            default:
                throw new IllegalArgumentException("Unsupported difficulty level: " + difficulty);
        }
        playMusic(musicPath);
    }

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

    private void stopTimerAndMusic() {
        if (timer != null) {
            timer.stop();
        }
        stopMusic();
    }

    private void showLoserBanner() {
        LoserBanner loserBanner = new LoserBanner(this);
        loserBanner.display();
        frame.dispose();
    }

    private void showWinnerBanner() {
        WinnerBanner winnerBanner = new WinnerBanner(this);
        winnerBanner.display();
        frame.dispose();
    }

    public void restart() {
        display();
    }
}