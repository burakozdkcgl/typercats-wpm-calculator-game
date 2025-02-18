package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import logic.Settings;
import logic.Language;

public class SettingsMenu extends JPanel {

    private final JLabel difficultyLabel;
    private final JLabel languageLabel;
    private final JLabel timerLabel;
    private final JComboBox<String> difficultyComboBox;
    private final JComboBox<String> languageComboBox;
    private final JComboBox<String> timerComboBox;

    private final ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/bg.png"));

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the background image
        if (backgroundImageIcon != null) {
            g2d.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

    }


    public SettingsMenu(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 120, 50));

        // Top Right Button
        ImageIcon idleIcon = new ImageIcon(getClass().getResource("/return_idle.png"));
        JButton topRightButton = new JButton(idleIcon);
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/return_hover.png"));
        topRightButton.setRolloverIcon(hoverIcon);
        topRightButton.setBorderPainted(false);
        topRightButton.setContentAreaFilled(false);
        topRightButton.setFocusPainted(false);
        topRightButton.addActionListener(e -> {
            MainMenu mainMenu = (MainMenu) mainPanel.getClientProperty("mainMenu");
            if (mainMenu != null) {
                mainMenu.refreshText();
            }
            cardLayout.show(mainPanel, "menuPanel");
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(topRightButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Centering Panel for Vertical and Horizontal Centering
        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setOpaque(false);
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.anchor = GridBagConstraints.CENTER; // Center the panel

        // Settings Panel with GridBagLayout
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        // Difficulty setting
        difficultyLabel = new JLabel(Language.getText("difficulty"));
        difficultyComboBox = createSmallComboBox(new String[]{
                Language.getText("easy"),
                Language.getText("medium"),
                Language.getText("hard")
        });
        difficultyComboBox.setSelectedItem(Language.getText(Settings.getDifficulty()));
        difficultyComboBox.addActionListener(e -> {
            String selectedDifficulty = "medium";
            if (Objects.equals(difficultyComboBox.getSelectedItem(), Language.getText("easy"))) selectedDifficulty = "easy";
            if (Objects.equals(difficultyComboBox.getSelectedItem(), Language.getText("hard"))) selectedDifficulty = "hard";
            Settings.setDifficulty(selectedDifficulty);
        });

        gbc.gridx = 0; gbc.gridy = 0; // Row 0, Column 0
        settingsPanel.add(difficultyLabel, gbc);

        gbc.gridx = 1; // Row 0, Column 1
        settingsPanel.add(difficultyComboBox, gbc);

        // Language setting
        languageLabel = new JLabel(Language.getText("language"));
        languageComboBox = createSmallComboBox(new String[]{
                "English",
                "Türkçe"
        });
        if ((Settings.getLanguage().equals("turkish"))) languageComboBox.setSelectedItem("Türkçe");
        else languageComboBox.setSelectedItem("English");
        languageComboBox.addActionListener(e -> {
            String selectedLanguage = "english";
            if (Objects.equals(languageComboBox.getSelectedItem(), "Türkçe")) selectedLanguage = "turkish";
            Settings.setLanguage(selectedLanguage);
            refreshText();
        });

        gbc.gridx = 0; gbc.gridy = 1; // Row 1, Column 0
        settingsPanel.add(languageLabel, gbc);

        gbc.gridx = 1; // Row 1, Column 1
        settingsPanel.add(languageComboBox, gbc);

        // Timer setting
        timerLabel = new JLabel(Language.getText("timer_duration"));
        timerComboBox = createSmallComboBox(new String[]{
                Language.getText("20 seconds"),
                Language.getText("1 minute"),
                Language.getText("3 minutes")
        });
        timerComboBox.setSelectedItem(Language.getText(Settings.getTimerDuration()));
        timerComboBox.addActionListener(e -> {
            String selectedTimer = "20 seconds";
            if (Objects.equals(timerComboBox.getSelectedItem(), Language.getText("1 minute"))) selectedTimer = "1 minute";
            if (Objects.equals(timerComboBox.getSelectedItem(), Language.getText("3 minutes"))) selectedTimer = "3 minutes";
            Settings.setTimerDuration(selectedTimer);
        });

        gbc.gridx = 0; gbc.gridy = 2; // Row 2, Column 0
        settingsPanel.add(timerLabel, gbc);

        gbc.gridx = 1; // Row 2, Column 1
        settingsPanel.add(timerComboBox, gbc);

        // Add the settings panel to the centering panel
        centeringPanel.add(settingsPanel, outerGbc);

        // Add the centering panel to the main panel
        add(centeringPanel, BorderLayout.CENTER);
    }




    private JComboBox<String> createSmallComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI()); // Use a plain UI to avoid padding
        Dimension size = new Dimension(100, 22); // Set smaller dimensions
        comboBox.setPreferredSize(size);
        comboBox.setMaximumSize(size);
        comboBox.setMinimumSize(size);
        // Add a visible border
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Light gray border
        return comboBox;
    }

    private void refreshText() {
        difficultyLabel.setText(Language.getText("difficulty"));
        languageLabel.setText(Language.getText("language"));
        timerLabel.setText(Language.getText("timer_duration"));

        difficultyComboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                Language.getText("easy"),
                Language.getText("medium"),
                Language.getText("hard")
        }));
        difficultyComboBox.setSelectedItem(Language.getText(Settings.getDifficulty()));

        timerComboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                Language.getText("20 seconds"),
                Language.getText("1 minute"),
                Language.getText("3 minutes")
        }));
        timerComboBox.setSelectedItem(Language.getText(Settings.getTimerDuration()));
    }
}
