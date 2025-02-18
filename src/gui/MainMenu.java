package gui;

import logic.Language;
import logic.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainMenu {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private JButton startButton, scoresButton, settingsButton, achievementsButton, exitButton, customButton;

    private final ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/bg.png"));

    public MainMenu() {

        // Create CardLayout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Store reference to this MainMenu in mainPanel for later access
        mainPanel.putClientProperty("mainMenu", this);

        // Add all necessary panels
        addMainMenuPanel();

        // Show the main menu initially
        cardLayout.show(mainPanel, "menuPanel");

    }

    private void addMainMenuPanel() {


        JPanel mainMenuPanel = new JPanel(new BorderLayout(20, 20)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));


        // Start Typing Test Button
        startButton = createStyledButton(Language.getText("start_test"));
        startButton.addActionListener(e -> {
            TestMenu testMenu = new TestMenu(cardLayout, mainPanel);
            mainPanel.add(testMenu, "testPanel");
            cardLayout.show(mainPanel, "testPanel");
        });

        customButton = createStyledButton("<html><div style='text-align: center;'>" + Language.getText("customs").replace("(", "<br>(") + "</div></html>");
        customButton.addActionListener(e -> {
            CustomModesMenu customMenu = new CustomModesMenu(cardLayout, mainPanel);
            DeadModeMenu deadModeMenu = new DeadModeMenu(cardLayout,mainPanel);
            RatModeMenu ratModeMenu = new RatModeMenu(cardLayout,mainPanel);
            mainPanel.add(customMenu, "customPanel");
            mainPanel.add(deadModeMenu, "deadModePanel");
            mainPanel.add(ratModeMenu, "ratModePanel");
            cardLayout.show(mainPanel, "customPanel");
        });

        // View Scores Button - navigate to ScoreMenu
        scoresButton = createStyledButton(Language.getText("view_scores"));
        scoresButton.addActionListener(e -> {
            ScoreMenu scoreMenu = new ScoreMenu(cardLayout, mainPanel);
            mainPanel.add(scoreMenu, "scorePanel");
            cardLayout.show(mainPanel, "scorePanel");
        });

        // View Scores Button - navigate to ScoreMenu
        achievementsButton = createStyledButton(Language.getText("achievements"));
        achievementsButton.addActionListener(e -> {
            AchievementsMenu achievementsMenu = new AchievementsMenu(cardLayout, mainPanel);
            mainPanel.add(achievementsMenu, "achievementsPanel");
            cardLayout.show(mainPanel, "achievementsPanel");
        });

        // Settings Button
        settingsButton = createStyledButton(Language.getText("settings"));
        settingsButton.addActionListener(e -> {
            SettingsMenu settingsMenu = new SettingsMenu(cardLayout, mainPanel);
            mainPanel.add(settingsMenu, "settingsPanel");
            cardLayout.show(mainPanel, "settingsPanel");
            mainPanel.revalidate();
            mainPanel.repaint();
        });


        // Exit Button
        exitButton = createStyledButton(Language.getText("exit"));
        exitButton.addActionListener(e -> System.exit(0));


        // Center Panel for Buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between buttons
        gbc.fill = GridBagConstraints.NONE; // Prevent stretching
        gbc.anchor = GridBagConstraints.CENTER; // Center-align buttons

        Dimension buttonSize = new Dimension(200, 100); // Smaller button size

        // Add Buttons to Center Panel
        gbc.gridx = 0; gbc.gridy = 0;
        startButton.setPreferredSize(buttonSize);
        centerPanel.add(startButton, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        customButton.setPreferredSize(buttonSize);
        centerPanel.add(customButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        scoresButton.setPreferredSize(buttonSize);
        centerPanel.add(scoresButton, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        achievementsButton.setPreferredSize(buttonSize);
        centerPanel.add(achievementsButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        settingsButton.setPreferredSize(buttonSize);
        centerPanel.add(settingsButton, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        exitButton.setPreferredSize(buttonSize);
        centerPanel.add(exitButton, gbc);

        // Ensure Center Panel is Transparent
        centerPanel.setOpaque(false);

        // Add Center Panel to Main Menu Panel
        mainMenuPanel.add(centerPanel, BorderLayout.CENTER);

        // Add Main Menu Panel to Main Panel
        mainPanel.add(mainMenuPanel, "menuPanel");

        // Show Main Menu Panel
        cardLayout.show(mainPanel, "menuPanel");


        // Add Global KeyEventDispatcher to Prevent Space Key from Activating Buttons
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


    public JPanel getMainPanel() {
        Settings.getInstance();
        refreshText();
        return mainPanel;
    }

    public void refreshText() {
        if (startButton != null) {
            startButton.setText(Language.getText("start_test"));
        }
        if (scoresButton != null) {
            scoresButton.setText(Language.getText("view_scores"));
        }
        if (achievementsButton != null) {
            achievementsButton.setText(Language.getText("achievements"));
        }
        if (settingsButton != null) {
            settingsButton.setText(Language.getText("settings"));
        }
        if (exitButton != null) {
            exitButton.setText(Language.getText("exit"));
        }
        if (customButton != null) {
            customButton.setText("<html><div style='text-align: center;'>" + Language.getText("customs").replace("(", "<br>(") + "</div></html>");

        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            public void paintComponent(Graphics g) {
                // Always apply custom colors based on button state
                if (getModel().isPressed()) {
                    setBackground(Color.DARK_GRAY); // Maintain hover color on click
                } else if (getModel().isRollover()) {
                    setBackground(Color.DARK_GRAY);
                } else {
                    setBackground(Color.LIGHT_GRAY);
                }
                super.paintComponent(g);
            }
        };

        // Remove default focus and border painting
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);

        // Add custom hover and click behaviors
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.DARK_GRAY);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.LIGHT_GRAY);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Color.DARK_GRAY); // Prevent pressed state change
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(Color.DARK_GRAY); // Maintain hover color
            }
        });

        // Override the button's UI to prevent default pressed behavior
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        return button;
    }

}