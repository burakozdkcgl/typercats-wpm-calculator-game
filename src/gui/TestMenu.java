package gui;
import logic.Language;
import logic.Settings;
import logic.Word;
import logic.Score;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TestMenu extends JPanel {

    private JLabel wordLabel;
    private final JLabel timerLabel;
    private final JTextField inputField;
    private final JButton startButton;
    private JButton restartButton;
    private Timer timer;
    private int timeLeft;
    private boolean timerStarted = false;
    private final List<String> words;
    private String currentWord;
    private final Random random = new Random();

    private int correctWords = 0;
    private int incorrectWords = 0;
    private int correctKeystrokes = 0;
    private int incorrectKeystrokes = 0;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/cat1.gif"));

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    public TestMenu(CardLayout cardLayout, JPanel mainPanel) {
        // Preload words
        Word.loadWords();
        words = Word.getWords(Settings.getLanguage() + "_" + Settings.getDifficulty());
        wordLabel = new JLabel("");

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        ImageIcon idleIcon = new ImageIcon(getClass().getResource("/return_idle.png"));
        JButton topRightButton = new JButton(idleIcon);
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/return_hover.png"));
        topRightButton.setRolloverIcon(hoverIcon); // Set hover image
        topRightButton.setBorderPainted(false);    // Remove button border
        topRightButton.setContentAreaFilled(false); // Make the button background transparent
        topRightButton.setFocusPainted(false);     // Remove focus outline
        topRightButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "menuPanel");
            resetTest();
        });
        add(topRightButton);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Transparent background
        topPanel.add(topRightButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);


        // Center Panel for Words and Input
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));


        wordLabel = new JLabel("", SwingConstants.CENTER);
        wordLabel.setFont(new Font("Arial", Font.BOLD, 22));
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wordLabel.setPreferredSize(new Dimension(300, 50)); // Set fixed size

        //inputField
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
        inputField.setEnabled(false); // Initially disabled
        inputField.setVisible(false); // Initially invisible
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputField.setMaximumSize(new Dimension(400, 30));
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (!timerStarted) return; // Only track when the test is running

                int keyCode = e.getKeyCode();

                // Ignore space and backspace keys for keystroke counting
                if (keyCode == java.awt.event.KeyEvent.VK_SPACE || keyCode == java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    return;
                }

                // Check if the typed key matches the current word prefix
                char keyChar = e.getKeyChar();
                if (currentWord.startsWith(inputField.getText() + keyChar)) {
                    correctKeystrokes++;
                } else {
                    incorrectKeystrokes++;
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (!timerStarted) return; // Only process input when the test is running

                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    // Handle space key to process the word
                    String input = inputField.getText().trim();
                    if (input.equals(currentWord)) {
                        correctWords++;
                    } else {
                        incorrectWords++;
                    }
                    inputField.setText(""); // Clear input
                    currentWord = words.get(random.nextInt(words.size())); // Pick a random word without removing
                    wordLabel.setText(currentWord);
                    return;
                }

                String input = inputField.getText(); // Current input
                StringBuilder styledText = new StringBuilder("<html><div style='text-align:center;'>");

                for (int i = 0; i < currentWord.length(); i++) {
                    if (i < input.length()) {
                        if (input.charAt(i) == currentWord.charAt(i)) {
                            // Correct character: green
                            styledText.append("<span style='color:green;'>").append(currentWord.charAt(i)).append("</span>");
                        } else {
                            // Incorrect character: red
                            styledText.append("<span style='color:red;'>").append(currentWord.charAt(i)).append("</span>");
                        }
                    } else {
                        // Unentered characters: default color (black)
                        styledText.append("<span style='color:black;'>").append(currentWord.charAt(i)).append("</span>");
                    }
                }

                styledText.append("&nbsp;");
                styledText.append("</div></html>");
                wordLabel.setText(styledText.toString()); // Update the label
            }
        });

        timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setPreferredSize(new Dimension(200, 30)); // Fixed size to avoid layout shifting

        centerPanel.add(wordLabel);
        centerPanel.add(Box.createVerticalStrut(40)); // Add 10px space
        centerPanel.add(inputField);
        centerPanel.add(Box.createVerticalStrut(40)); // Add 10px space
        centerPanel.add(timerLabel);
        centerPanel.add(Box.createVerticalStrut(40)); // Add 10px space

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Set BoxLayout for vertical alignment
        ImageIcon startIdleIcon = new ImageIcon(getClass().getResource("/start_idle.png"));
        ImageIcon startHoverIcon = new ImageIcon(getClass().getResource("/start_hover.png"));
        startButton = new JButton(startIdleIcon);
        startButton.setRolloverIcon(startHoverIcon);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the button
        startButton.addActionListener(e -> startTest());

        JButton returnButton = new JButton(Language.getText("back_to_menu"));
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the button
        returnButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "menuPanel");
            resetTest();
        });
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(15)); // Add vertical space between buttons

        JPanel mainCenterPanel = new JPanel();
        mainCenterPanel.setOpaque(false);
        mainCenterPanel.setLayout(new BoxLayout(mainCenterPanel, BoxLayout.Y_AXIS));
        mainCenterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainCenterPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        // Add the center panel and buttons
        mainCenterPanel.add(centerPanel);
        mainCenterPanel.add(buttonPanel);

        add(mainCenterPanel, BorderLayout.CENTER); // Add mainCenterPanel to CENTER


        restartButton = null;
    }

    private void startTest() {
        if (timerStarted) {
            resetTest();
            return;
        }

        // Reset the background image to the original
        backgroundImageIcon.setImage(new ImageIcon(getClass().getResource("/cat1.gif")).getImage());

        ImageIcon restartIdleIcon = new ImageIcon(getClass().getResource("/restart_idle.png"));
        ImageIcon restartHoverIcon = new ImageIcon(getClass().getResource("/restart_hover.png"));
        startButton.setIcon(restartIdleIcon);
        startButton.setRolloverIcon(restartHoverIcon);

        timerStarted = true;
        inputField.setEnabled(true);
        inputField.setVisible(true);

        inputField.requestFocus();

        // Reset counters
        correctWords = 0;
        incorrectWords = 0;
        correctKeystrokes = 0;
        incorrectKeystrokes = 0;

        // Start timer
        timeLeft = switch (Settings.getTimerDuration()) {
            case "20 seconds" -> 20;
            case "3 minutes" -> 180;
            default -> 60;
        };

        updateTimerLabel();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (timeLeft > 0) {
                        timeLeft--;
                        updateTimerLabel();
                    } else {
                        timer.cancel();
                        endTest();
                    }
                });
            }
        }, 0, 1000);

        currentWord = words.get(random.nextInt(words.size())); // Pick a random word without removing
        wordLabel.setText(currentWord);
    }

    private void updateTimerLabel() {
        int seconds = timeLeft;
        int minutes = seconds / 60;
        seconds %= 60;
        timerLabel.setText(Language.getText("timer_left") + " " + String.format("%02d:%02d", minutes, seconds));
        revalidate(); // Force UI refresh
        repaint();    // Ensure UI repaint
    }

    private void resetTest() {
        if (timer != null) {
            timer.cancel();
        }
        timerStarted = false;

        ImageIcon startIdleIcon = new ImageIcon(getClass().getResource("/start_idle.png"));
        ImageIcon startHoverIcon = new ImageIcon(getClass().getResource("/start_hover.png"));
        startButton.setIcon(startIdleIcon);
        startButton.setRolloverIcon(startHoverIcon);

        timerLabel.setText("");
        inputField.setEnabled(false);
        inputField.setVisible(false); // Initially invisible
        inputField.setText("");
        wordLabel.setText(""); // Reset the wordLabel to "typing_test"
    }

    private void endTest() {

        timerStarted = false;
        inputField.setEnabled(false);
        inputField.setVisible(false);
        resetTest();

        // Change the background image
        backgroundImageIcon.setImage(new ImageIcon(getClass().getResource("/cat2.png")).getImage()); // Replace with your desired background image

        // Create Score object
        Score score = new Score(
                java.time.LocalDate.now().toString(),
                Settings.getDifficulty(),
                Settings.getLanguage(),
                Settings.getTimerDuration(),
                correctWords,
                incorrectWords,
                correctKeystrokes,
                incorrectKeystrokes
        );

        // Display results
        JLabel resultLabel = new JLabel(
                "<html><div style='text-align:center;'>" +
                        Language.getText("total_words") + ": " + (score.getCorrectWordsCount() + score.getIncorrectWordsCount()) + "<br>" +
                        Language.getText("correct_words") + ": " + score.getCorrectWordsCount() + "<br>" +
                        Language.getText("incorrect_words") + ": " + score.getIncorrectWordsCount() + "<br>" +
                        Language.getText("wpm") + ": " + score.getWPM() + "<br>" +
                        Language.getText("correct_keystrokes") + ": " + score.getCorrectKeyStroke() + "<br>" +
                        Language.getText("incorrect_keystrokes") + ": " + score.getIncorrectKeyStroke() + "<br>" +
                        Language.getText("accuracy") + ": " + new DecimalFormat("##.##").format(score.getAccuracy()) + "<br>" +
                        "</div></html>",
                SwingConstants.CENTER
        );
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Score.addScore(score);

        // Add the result label to the UI
        JPanel centerPanel = (JPanel) getComponent(1); // Center panel with wordLabel and inputField
        centerPanel.removeAll(); // Clear existing components
        centerPanel.add(resultLabel);
        centerPanel.add(Box.createVerticalStrut(40)); // Add space between result and button

        ImageIcon restartIdleIcon = new ImageIcon(getClass().getResource("/restart_idle.png"));
        ImageIcon restartHoverIcon = new ImageIcon(getClass().getResource("/restart_hover.png"));
        restartButton = new JButton(restartIdleIcon);
        restartButton.setRolloverIcon(restartHoverIcon);
        restartButton.setBorderPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setFocusPainted(false);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the button
        restartButton.addActionListener(e -> {
            TestMenu testMenu = new TestMenu(cardLayout, mainPanel);
            mainPanel.add(testMenu, "testPanel");
            cardLayout.show(mainPanel, "testPanel");
        });

        centerPanel.add(restartButton);

        // Refresh the UI
        revalidate();
        repaint();
    }
}
