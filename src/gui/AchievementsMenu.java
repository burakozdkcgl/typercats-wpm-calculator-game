package gui;

import logic.Achievement;

import javax.swing.*;
import java.awt.*;

public class AchievementsMenu extends JPanel {

    private final ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/cat3.png"));

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    public AchievementsMenu(CardLayout cardLayout, JPanel mainPanel) {

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        ImageIcon idleIcon = new ImageIcon(getClass().getResource("/return_idle.png"));
        JButton topRightButton = new JButton(idleIcon);
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/return_hover.png"));
        topRightButton.setRolloverIcon(hoverIcon); // Set hover image
        topRightButton.setBorderPainted(false);    // Remove button border
        topRightButton.setContentAreaFilled(false); // Make the button background transparent
        topRightButton.setFocusPainted(false);     // Remove focus outline
        topRightButton.addActionListener(e -> cardLayout.show(mainPanel, "menuPanel"));
        topRightButton.setBounds(getWidth() - 60, 10, 50, 50); // Adjust position and size


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Transparent background
        topPanel.add(topRightButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);


        // Achievements Panel
        JPanel achievementsPanel = new JPanel();
        achievementsPanel.setOpaque(false);
        achievementsPanel.setLayout(new GridLayout(4, 3, 10, 10)); // 4 rows, 3 columns, with spacing
        achievementsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Color[] progressColors = {
                new Color(200, 0, 0),    // Deep Red (0-10%)
                new Color(220, 50, 0),   // Dark Red-Orange (10-20%)
                new Color(230, 100, 0),  // Muted Orange-Red (20-30%)
                new Color(240, 140, 0),  // Muted Orange (30-40%)
                new Color(245, 170, 0),  // Soft Burnt Orange (40-50%)
                new Color(210, 180, 0),  // Olive-Orange Transition (50-60%)
                new Color(180, 200, 0),  // Muted Olive Green (60-70%)
                new Color(120, 180, 0),  // Soft Green (70-80%)
                new Color(80, 150, 0),   // Dark Olive Green (80-90%)
                new Color(50, 120, 0)    // Deep Forest Green (90-100%)
        };


        // Add achievement elements
        for (int i = 0; i < 12; i++) {

            Achievement achievement = new Achievement(i);
            // Individual achievement panel
            JPanel achievements = new JPanel(new BorderLayout(5, 5));
            achievements.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Border for visibility

            // Title Label
            JLabel achievementTitle = new JLabel(achievement.getTitle(), SwingConstants.CENTER);
            achievementTitle.setFont(new Font("Arial", Font.BOLD, 14));
            achievements.add(achievementTitle, BorderLayout.NORTH);

            // Description Label (replacing JTextArea)
            JLabel achievementDescription = new JLabel("<html><div style='text-align: center;'>"+ achievement.getDescription() +"</div></html>" ,  SwingConstants.CENTER );
            achievementDescription.setFont(new Font("Arial", Font.PLAIN, 12));
            achievements.add(achievementDescription, BorderLayout.CENTER);

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(achievement.getProgress()); // Initial progress (can be updated dynamically later)


            // Set initial color
            int initialValue = progressBar.getValue();
            int colorIndex = Math.min(initialValue / 10, 9); // Determine the color index (0-9)
            progressBar.setForeground(progressColors[colorIndex]);

            // Use a ChangeListener for dynamic updates
            progressBar.addChangeListener(e -> {
                int value = progressBar.getValue();
                int index = Math.min(value / 10, 9); // Determine the color index (0-9)
                progressBar.setForeground(progressColors[index]); // Update color dynamically
            });


            achievements.add(progressBar, BorderLayout.SOUTH);

            // Add to achievements panel
            achievementsPanel.add(achievements);
        }

        // Wrapper Panel
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false); // Make the wrapper transparent
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(-20, 0, 0, 0)); // Adjust the top padding to center the panel
        wrapperPanel.add(achievementsPanel, BorderLayout.CENTER); // Center the grid

        add(wrapperPanel, BorderLayout.CENTER);

    }
}
