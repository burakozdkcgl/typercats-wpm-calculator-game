package gui;

import logic.Language;
import logic.Settings;
import logic.Word;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class DeadModeMenu extends JPanel {

    private JLabel wordLabel;
    private final JTextField inputField;
    private final JButton startButton;

    private boolean timerStarted = false;
    private final List<String> words;
    private String currentWord;
    private final Random random = new Random();
    private boolean testEnded = false;

    private int correctWords = 0;
    private int correctKeystrokes = 0;

    private final ImageIcon backgroundImageIcon1 = new ImageIcon(getClass().getResource("/cat1.gif"));
    private final ImageIcon backgroundImageIcon2 = new ImageIcon(getClass().getResource("/cat2.png"));

    // Load images for start and restart buttons
    ImageIcon startIdleIcon = new ImageIcon(getClass().getResource("/start_idle.png"));
    ImageIcon startHoverIcon = new ImageIcon(getClass().getResource("/start_hover.png"));
    ImageIcon restartIdleIcon = new ImageIcon(getClass().getResource("/restart_idle.png"));
    ImageIcon restartHoverIcon = new ImageIcon(getClass().getResource("/restart_hover.png"));

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (testEnded) {
            g.drawImage(backgroundImageIcon2.getImage(), 0, 0, getWidth(), getHeight(), this); // End test background
        } else {
            g.drawImage(backgroundImageIcon1.getImage(), 0, 0, getWidth(), getHeight(), this); // Default background
        }
    }

    public DeadModeMenu(CardLayout cardLayout, JPanel mainPanel) {

        // Preload words
        Word.loadWords();
        words = Word.getWords(Settings.getLanguage() + "_" + Settings.getDifficulty());
        wordLabel = new JLabel("");

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
        // Disable the space bar triggering the button
        InputMap inputMap = topRightButton.getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "none");
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
                if (keyCode == java.awt.event.KeyEvent.VK_BACK_SPACE) endTest();

                // Ignore Enter key entirely
                if (keyCode == java.awt.event.KeyEvent.VK_ENTER) {
                    e.consume(); // Consume the event to prevent any default action
                    return;
                }





                // Check if the typed key matches the current word prefix
                char keyChar = e.getKeyChar();
                if (currentWord.startsWith(inputField.getText() + keyChar)) {
                    correctKeystrokes++;
                } else {
                    if (keyCode == java.awt.event.KeyEvent.VK_SPACE){
                        String input = inputField.getText().trim();
                        if (!input.equals(currentWord)) {
                            endTest(); // End the test if the word is incomplete or incorrect
                        }
                    }
                    else{
                        endTest();
                    }
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


        centerPanel.add(wordLabel);
        centerPanel.add(Box.createVerticalStrut(40)); // Add 10px space
        centerPanel.add(inputField);
        centerPanel.add(Box.createVerticalStrut(40)); // Add 10px space




        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Set BoxLayout for vertical alignment
        // Initialize startButton with start_idle
        startButton = new JButton(startIdleIcon);
        startButton.setRolloverIcon(startHoverIcon); // Set hover image
        startButton.setBorderPainted(false);         // Remove button border
        startButton.setContentAreaFilled(false);     // Transparent background
        startButton.setFocusPainted(false);          // Remove focus outline
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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


    }


    private void startTest() {
        if (timerStarted) {
            resetTest(); // Reset and restart the test
            return;
        }

        testEnded = false; // Ensure test state is not ended
        timerStarted = true;

        // Update start button
        // Update start button to restart button
        startButton.setIcon(restartIdleIcon);      // Set restart idle icon
        startButton.setRolloverIcon(restartHoverIcon); // Set restart hover icon

        // Reset and enable input field
        inputField.setText(""); // Clear input
        inputField.setEnabled(true);
        inputField.setVisible(true);
        inputField.requestFocus();

        // Reset counters
        correctWords = 0;
        correctKeystrokes = 0;

        // Set a new word
        currentWord = words.get(random.nextInt(words.size()));
        wordLabel.setText(currentWord);

        // Reset center panel
        JPanel centerPanel = (JPanel) getComponent(1);
        centerPanel.removeAll();
        centerPanel.add(wordLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(inputField);

        // Force UI refresh
        revalidate();
        repaint();
    }




    private void resetTest() {
        testEnded = false; // Reset test state
        timerStarted = false;

        // Reset start button text
        startButton.setText(Language.getText("start_test"));

        // Reset input field
        inputField.setText(""); // Clear input
        inputField.setEnabled(false);
        inputField.setVisible(false);
        inputField.requestFocus(); // Refocus to ensure it's active

        // Reset wordLabel
        wordLabel.setText("");

        // Reset center panel
        JPanel centerPanel = (JPanel) getComponent(1); // Center panel with wordLabel and inputField
        centerPanel.removeAll();
        centerPanel.add(wordLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(inputField);

        // Force UI refresh
        revalidate();
        repaint();
    }




    private void endTest() {
        testEnded = true; // Set test state to ended
        timerStarted = false;

        // Hide input field and word label
        inputField.setEnabled(false);
        inputField.setVisible(false);
        wordLabel.setText(""); // Clear the word display

        // Display results
        JLabel resultLabel = new JLabel(
                "<html><div style='text-align:center;'>" +
                        Language.getText("correct_words") + ": " + correctWords + "<br>" +
                        Language.getText("correct_keystrokes") + ": " + correctKeystrokes + "<br>" +
                        Language.getText("difficulty") + ": " + Language.getText(Settings.getDifficulty()) +
                        "</div></html>",
                SwingConstants.CENTER
        );
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Refresh the center panel
        JPanel centerPanel = (JPanel) getComponent(1);
        centerPanel.removeAll();
        centerPanel.add(resultLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(startButton);

        // Force the UI to refresh
        revalidate();
        repaint();
    }





}
