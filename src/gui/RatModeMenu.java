package gui;

import logic.Language;
import logic.Word;
import logic.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class RatModeMenu extends JPanel {
    private final JPanel cheesePanel; // Panel to display cheese images
    private final List<JLabel> cheeseLabels; // List to manage cheese icons

    private final JTextField inputField;
    private final JPanel ratPanel;
    private final JButton startButton;
    private final List<Rat> activeRats;
    private int cheeseLeft;
    private final Random random;
    private Timer ratTimer;
    private int correctInputs; // Tracks the number of correct inputs
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public RatModeMenu(CardLayout cardLayout, JPanel mainPanel) {
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        this.cardLayout= cardLayout;
        this.mainPanel = mainPanel;

        random = new Random();
        activeRats = new ArrayList<>();

        // Cheese counter setup
        cheeseLeft = 5;
        cheesePanel = new JPanel();
        cheesePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Horizontal layout for cheese icons
        cheeseLabels = new ArrayList<>();

        // Add initial cheese icons
        for (int i = 0; i < cheeseLeft; i++) {
            JLabel cheeseLabel = new JLabel(new ImageIcon(getClass().getResource("/cheese.png")));
            cheeseLabels.add(cheeseLabel);
            cheesePanel.add(cheeseLabel);
        }

        // Rat display panel
        ratPanel = new JPanel(null);
        ratPanel.setPreferredSize(new Dimension(600, 300));
        ratPanel.setBackground(Color.LIGHT_GRAY);
        ratPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Wrapper panel with BoxLayout to remove extra space
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.add(Box.createVerticalGlue()); // Align to center
        wrapperPanel.add(ratPanel);
        wrapperPanel.add(Box.createVerticalGlue());
        wrapperPanel.setBackground(Color.DARK_GRAY);

        // Input field
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        inputField.setVisible(false); // Initially hidden
        inputField.setEnabled(false);
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    processInput();
                }
            }
        });

        // Start Button
        ImageIcon startIdleIcon = new ImageIcon(getClass().getResource("/start_idle.png"));
        ImageIcon startHoverIcon = new ImageIcon(getClass().getResource("/start_hover.png"));
        ImageIcon scaledRestartIdleIcon = new ImageIcon(startIdleIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ImageIcon scaledRestartHoverIcon = new ImageIcon(startHoverIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        startButton = new JButton(scaledRestartIdleIcon);
        startButton.setRolloverIcon(scaledRestartHoverIcon);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            startButton.setVisible(false); // Hide start button
            inputField.setVisible(true); // Show input field
            inputField.setEnabled(true); // Enable input
            startTest();
        });

        // Return button
        ImageIcon idleIcon = new ImageIcon(getClass().getResource("/return_idle.png"));
        JButton returnButton = new JButton(idleIcon);
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/return_hover.png"));
        returnButton.setRolloverIcon(hoverIcon);
        returnButton.setBorderPainted(false);
        returnButton.setContentAreaFilled(false);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "menuPanel");
            exitTest();
        });

        // Layout setup
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(returnButton, BorderLayout.EAST);
        topPanel.add(cheesePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(startButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(wrapperPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add global KeyEventDispatcher to prevent space key from activating buttons
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (e.getComponent() instanceof JButton) {
                    e.consume(); // Prevent the button from being activated
                    return true; // Consume the event
                }
            }
            return false;
        });

    }

    private void startTest() {

        cheeseLeft = 5;
        correctInputs = 0; // Reset correct input counter

        // Clear and reinitialize cheesePanel
        cheesePanel.removeAll();
        cheeseLabels.clear();
        for (int i = 0; i < cheeseLeft; i++) {
            JLabel cheeseLabel = new JLabel(new ImageIcon(getClass().getResource("/cheese.png")));
            cheeseLabels.add(cheeseLabel);
            cheesePanel.add(cheeseLabel);
        }
        cheesePanel.revalidate();
        cheesePanel.repaint();

        inputField.setEnabled(true);
        inputField.setText("");
        inputField.requestFocus();
        activeRats.clear();
        ratPanel.removeAll();

        ratTimer = new Timer();
        ratTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spawnRat();
            }
        }, 0, 2000);
    }

    private void spawnRat() {
        String word = Word.getWords(Settings.getLanguage() + "_" + Settings.getDifficulty())
                .get(random.nextInt(Word.getWords(Settings.getLanguage() + "_" + Settings.getDifficulty()).size()));
        boolean fromLeft = random.nextBoolean();

        Rat rat = new Rat(word, fromLeft);
        activeRats.add(rat);
        JLabel ratLabel = rat.getLabel();

        int panelHeight = ratPanel.getHeight();
        int ratHeight = ratLabel.getHeight();

        // Constrain spawn position to ensure the rat is fully visible
        int minY = 10; // Minimum padding from the top
        int maxY = panelHeight - ratHeight - 10; // Padding from the bottom
        int spawnY = random.nextInt(Math.max(1, maxY - minY + 1)) + minY; // Random Y within bounds

        if (fromLeft) {
            ratLabel.setLocation(0, spawnY);
        } else {
            ratLabel.setLocation(ratPanel.getWidth() - ratLabel.getWidth(), spawnY);
        }

        ratPanel.add(ratLabel);
        ratPanel.revalidate();
        ratPanel.repaint();

        Timer moveTimer = new Timer();
        moveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> moveRat(rat, moveTimer));
            }
        }, 0, 50);
    }


    private void moveRat(Rat rat, Timer moveTimer) {
        JLabel ratLabel = rat.getLabel();
        int x = ratLabel.getX();
        int targetX = (ratPanel.getWidth() - ratLabel.getWidth()) / 2;

        if (rat.isFromLeft()) {
            x += rat.getSpeed();
        } else {
            x -= rat.getSpeed();
        }

        if ((rat.isFromLeft() && x >= targetX) || (!rat.isFromLeft() && x <= targetX)) {
            if (activeRats.contains(rat)) {
                moveTimer.cancel();
                activeRats.remove(rat);
                ratPanel.remove(ratLabel);
                ratPanel.revalidate();
                ratPanel.repaint();

                // Create a WinnerRat and add it to the panel
                WinnerRat winnerRat = new WinnerRat(ratLabel, rat.isFromLeft(), rat.getSpeed());
                JLabel winnerLabel = winnerRat.getLabel();
                ratPanel.add(winnerLabel);


                cheeseLeft--;
                if (!cheeseLabels.isEmpty()) {
                    JLabel removedCheese = cheeseLabels.remove(cheeseLabels.size() - 1); // Remove the last cheese icon
                    cheesePanel.remove(removedCheese);
                    cheesePanel.revalidate();
                    cheesePanel.repaint();
                }

                if (cheeseLeft <= 0) {
                    endTest();
                }else {
                    // Move the WinnerRat back to the border
                    Timer winnerTimer = new Timer();
                    winnerTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(() -> moveWinnerRat(winnerRat, winnerTimer));
                        }
                    }, 0, 50);
                }
            }
        } else {
            ratLabel.setLocation(x, ratLabel.getY());
        }
    }

    private void processInput() {
        String input = inputField.getText().trim();
        inputField.setText("");

        for (Rat rat : new ArrayList<>(activeRats)) {
            if (rat.getWord().equals(input)) {

                correctInputs++; // Increment correct input counter

                activeRats.remove(rat);
                ratPanel.remove(rat.getLabel());
                ratPanel.revalidate();
                ratPanel.repaint();

                // Create a LoserRat at the same position
                LoserRat loserRat = new LoserRat(rat.getLabel(), rat.isFromLeft(), rat.getSpeed());
                JLabel loserLabel = loserRat.getLabel();
                ratPanel.add(loserLabel);
                ratPanel.revalidate();
                ratPanel.repaint();

                // Move the LoserRat back to the border
                Timer loserTimer = new Timer();
                loserTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> moveLoserRat(loserRat, loserTimer));
                    }
                }, 0, 50);


                break;
            }
        }
    }

    private void endTest() {
        if (ratTimer != null) {
            ratTimer.cancel();
        }

        for (Rat rat : new ArrayList<>(activeRats)) {
            ratPanel.remove(rat.getLabel());
        }
        activeRats.clear();
        ratPanel.revalidate();
        ratPanel.repaint();

        inputField.setEnabled(false);
        inputField.setVisible(false); // Hide input field
        startButton.setEnabled(true); // Enable the Start button

        // Show the results menu
        ResultsMenu resultsMenu = new ResultsMenu(
                cardLayout,
                mainPanel,
                correctInputs
        );
        mainPanel.add(resultsMenu, "resultsPanel");
        cardLayout.show(mainPanel, "resultsPanel");

    }

    private void exitTest() {
        if (ratTimer != null) {
            ratTimer.cancel();
        }

        // Clear active rats
        for (Rat rat : new ArrayList<>(activeRats)) {
            ratPanel.remove(rat.getLabel());
        }
        activeRats.clear();

        // Reset the panel and input field
        ratPanel.removeAll();
        ratPanel.revalidate();
        ratPanel.repaint();

        inputField.setEnabled(false);
        startButton.setEnabled(true); // Enable the Start button

        // Reset cheese panel
        cheesePanel.removeAll();
        cheeseLabels.clear();
        cheesePanel.revalidate();
        cheesePanel.repaint();
    }



    private static class Rat {
        private final String word;
        private final JLabel label;
        private final boolean fromLeft;
        private final int speed;

        public Rat(String word, boolean fromLeft) {
            this.word = word;
            this.fromLeft = fromLeft;

            this.speed = 6;

            ImageIcon ratIcon = new ImageIcon(getClass().getResource("/rat1.png"));
            if (!fromLeft) ratIcon = new ImageIcon(getClass().getResource("/rat2.png"));
            Image scaledImage = ratIcon.getImage().getScaledInstance(50, 25, Image.SCALE_SMOOTH);
            ratIcon = new ImageIcon(scaledImage);

            // Create the label
            label = new JLabel(ratIcon);
            label.setText("<html><center>" + word + "</center></html>"); // Center the text on multiple lines if necessary
            label.setFont(getAdjustedFont(word));
            label.setHorizontalTextPosition(SwingConstants.CENTER); // Center text horizontally
            label.setVerticalTextPosition(SwingConstants.TOP); // Place text above the image
            label.setIconTextGap(0); // Space between text and image
            label.setSize(100, 100); // Adjust size to fit both text and image
            label.setToolTipText(word); // Tooltip for long words

        }

        public String getWord() {
            return word;
        }

        public JLabel getLabel() {
            return label;
        }

        public boolean isFromLeft() {
            return fromLeft;
        }

        public int getSpeed() {
            return speed;
        }

        // Helper method to adjust font size
        private Font getAdjustedFont(String text) {
            int maxLength = 10; // Maximum length for default font size
            int baseFontSize = 16; // Base font size
            int adjustedFontSize = text.length() > maxLength ? baseFontSize - (text.length() - maxLength) : baseFontSize;
            adjustedFontSize = Math.max(10, adjustedFontSize); // Ensure font size doesn't go below 10
            return new Font("Arial", Font.BOLD, adjustedFontSize);
        }
    }


    private static class LoserRat {
        private final JLabel label;
        private final boolean fromLeft;
        private final int speed;

        public LoserRat(JLabel originalLabel, boolean fromLeft, int speed) {
            this.fromLeft = fromLeft;
            this.speed = speed;

            ImageIcon ratIcon = new ImageIcon(getClass().getResource("/rat2.png"));
            if (!fromLeft) ratIcon = new ImageIcon(getClass().getResource("/rat1.png"));
            Image scaledImage = ratIcon.getImage().getScaledInstance(50, 25, Image.SCALE_SMOOTH);
            ratIcon = new ImageIcon(scaledImage);
            this.label = new JLabel(ratIcon);
            this.label.setSize(originalLabel.getSize());
            this.label.setLocation(originalLabel.getLocation());
        }

        public JLabel getLabel() {
            return label;
        }

        public boolean isFromLeft() {
            return fromLeft;
        }

        public int getSpeed() {
            return speed;
        }
    }

    private void moveLoserRat(LoserRat loserRat, Timer timer) {
        JLabel loserLabel = loserRat.getLabel();
        int x = loserLabel.getX();

        // Move the rat back to the border
        if (loserRat.isFromLeft()) {
            x -= loserRat.getSpeed();
        } else {
            x += loserRat.getSpeed();
        }

        // Check if the rat has left the screen
        if ((loserRat.isFromLeft() && x + loserLabel.getWidth() < 0) ||
                (!loserRat.isFromLeft() && x > ratPanel.getWidth())) {
            timer.cancel();
            ratPanel.remove(loserLabel);
            ratPanel.revalidate();
            ratPanel.repaint();
        } else {
            // Update the position
            loserLabel.setLocation(x, loserLabel.getY());
        }
    }

    private static class WinnerRat {
        private final JLabel label;
        private final boolean fromLeft;
        private final int speed;

        public WinnerRat(JLabel originalLabel, boolean fromLeft, int speed) {
            this.fromLeft = fromLeft;
            this.speed = speed;

            // Load the winner rat icon based on direction
            ImageIcon winnerIcon = new ImageIcon(fromLeft ? getClass().getResource("/rat4.png") : getClass().getResource("/rat3.png"));
            Image scaledImage = winnerIcon.getImage().getScaledInstance(50, 25, Image.SCALE_SMOOTH);
            winnerIcon = new ImageIcon(scaledImage);

            // Create the JLabel
            this.label = new JLabel(winnerIcon);
            this.label.setSize(originalLabel.getSize());
            this.label.setLocation(originalLabel.getLocation());
        }

        public JLabel getLabel() {
            return label;
        }

        public boolean isFromLeft() {
            return fromLeft;
        }

        public int getSpeed() {
            return speed;
        }
    }

    private void moveWinnerRat(WinnerRat winnerRat, Timer timer) {
        JLabel winnerLabel = winnerRat.getLabel();
        int x = winnerLabel.getX();

        // Move the rat back to the border
        if (winnerRat.isFromLeft()) {
            x -= winnerRat.getSpeed();
        } else {
            x += winnerRat.getSpeed();
        }

        // Check if the rat has left the screen
        if ((winnerRat.isFromLeft() && x + winnerLabel.getWidth() < 0) ||
                (!winnerRat.isFromLeft() && x > ratPanel.getWidth())) {
            timer.cancel();
            ratPanel.remove(winnerLabel);
            ratPanel.revalidate();
            ratPanel.repaint();
        } else {
            // Update the position
            winnerLabel.setLocation(x, winnerLabel.getY());
        }
    }



    static class ResultsMenu extends JPanel {

        private final Image backgroundImage;

        public ResultsMenu(CardLayout cardLayout, JPanel mainPanel, int correctInputs) {

            // Load the background image
            backgroundImage = new ImageIcon(getClass().getResource("/cat2.png")).getImage();

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setLayout(new BorderLayout(20, 20));
            setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

            // Add correct inputs label
            JLabel correctInputsLabel = new JLabel("<html><div style='text-align:center;'>" + Language.getText("rats_caught") + ": " + correctInputs + "<br>" +
                    Language.getText("difficulty") + ": " + Language.getText(Settings.getDifficulty()) + "</div></html>");
            correctInputsLabel.setFont(new Font("Arial", Font.BOLD, 18));
            correctInputsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add return to menu button (styled like in RatModeMenu)
            ImageIcon idleIcon = new ImageIcon(getClass().getResource("/return_idle.png"));
            JButton returnButton = new JButton(idleIcon);
            ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/return_hover.png"));
            returnButton.setRolloverIcon(hoverIcon);
            returnButton.setBorderPainted(false);
            returnButton.setContentAreaFilled(false);
            returnButton.setFocusPainted(false);
            returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            returnButton.addActionListener(e -> cardLayout.show(mainPanel, "menuPanel"));

            // Restart Button in ResultsMenu
            ImageIcon restartIdleIcon = new ImageIcon(getClass().getResource("/restart_idle.png"));
            ImageIcon restartHoverIcon = new ImageIcon(getClass().getResource("/restart_hover.png"));
            JButton restartButton = new JButton(restartIdleIcon);
            restartButton.setRolloverIcon(restartHoverIcon);
            restartButton.setBorderPainted(false);
            restartButton.setContentAreaFilled(false);
            restartButton.setFocusPainted(false);
            restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            restartButton.addActionListener(e -> {
                // Remove the old RatModeMenu instance
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof RatModeMenu) {
                        mainPanel.remove(comp);
                        break;
                    }
                }

                // Create a new instance and switch to it
                RatModeMenu newRatModeMenu = new RatModeMenu(cardLayout, mainPanel);
                mainPanel.add(newRatModeMenu, "ratModeMenu");
                cardLayout.show(mainPanel, "ratModeMenu");
            });

            // Add return button to the top-right corner
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false); // Transparent background to match the main panel
            topPanel.add(returnButton, BorderLayout.EAST);

            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for perfect centering
            centerPanel.setOpaque(false); // Transparent background

            // Inner panel for vertical stacking
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
            innerPanel.setOpaque(false); // Transparent background

            innerPanel.add(correctInputsLabel);
            innerPanel.add(Box.createVerticalStrut(30)); // Spacer
            innerPanel.add(restartButton);

            // Add inner panel to centerPanel
            centerPanel.add(innerPanel, new GridBagConstraints());

            // Add panels to the main layout
            this.add(topPanel, BorderLayout.NORTH); // Add return button to the top-right
            this.add(centerPanel, BorderLayout.CENTER); // Add results and restart button to the center


        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

    }

}
