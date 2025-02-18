package gui;

import logic.Language;
import logic.Score;
import logic.Settings;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreMenu extends JPanel {

    private final ImageIcon backgroundImageIcon = new ImageIcon(getClass().getResource("/cat4.png"));

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    JButton resetButton;

    // Load images for reset button
    ImageIcon restartIdleIcon = new ImageIcon(getClass().getResource("/restart_idle.png"));
    ImageIcon restartHoverIcon = new ImageIcon(getClass().getResource("/restart_hover.png"));

    ImageIcon scaledRestartIdleIcon = new ImageIcon(restartIdleIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
    ImageIcon scaledRestartHoverIcon = new ImageIcon(restartHoverIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));


    String[] columnNames = {
            Language.getText("date"),
            Language.getText("difficulty"),
            Language.getText("language"),
            Language.getText("timer"),
            Language.getText("wordCount"),
            Language.getText("wpm"),
            Language.getText("keyStrokeCount"),
            Language.getText("accuracy"),
    };

    public ScoreMenu(CardLayout cardLayout, JPanel mainPanel) {



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
        add(topRightButton);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Transparent background
        topPanel.add(topRightButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Continue populating the table with data
        Object[][] data = new Object[Score.getScoreList().size()][8]; // 8 columns for the data
        for (int i = 0; i < Score.getScoreList().size(); i++) {

            ArrayList<Score> list = Score.getScoreList();
            Collections.reverse(list);
            Score score = list.get(i);

            data[i][0] = score.getDate();

            data[i][1] = Language.getText(score.getDifficulty());

            String scoreLanguage = Language.getText(score.getLanguage());
            if (score.getLanguage().equals("turkish") && Settings.getLanguage().equals("english")) scoreLanguage = "Turkish";
            else if (score.getLanguage().equals("english") && Settings.getLanguage().equals("turkish")) scoreLanguage = "İngilizce";
            data[i][2] = scoreLanguage;

            data[i][3] = Language.getText(score.getTimerDuration());

            data[i][4] = "<html>" + (score.getCorrectWordsCount() + score.getIncorrectWordsCount()) + " ("
                    + "<font color='green'>" + score.getCorrectWordsCount() + "</font>"
                    + " | "
                    + "<font color='red'>" + score.getIncorrectWordsCount() + "</font>"
                    + ")</html>";

            data[i][5] = new DecimalFormat("##.#").format(score.getWPM());

            data[i][6] = "<html>" + (score.getCorrectKeyStroke() + score.getIncorrectKeyStroke()) + " ("
                    + "<font color='green'>" + score.getCorrectKeyStroke() + "</font>"
                    + " | "
                    + "<font color='red'>" + score.getIncorrectKeyStroke() + "</font>"
                    + ")</html>";

            data[i][7] = " %" + new DecimalFormat("##.##").format(score.getAccuracy());
        }


        JTable scoreTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);




        // Initialize resetButton with restart_idle
        resetButton = new JButton(scaledRestartIdleIcon);
        resetButton.setRolloverIcon(scaledRestartHoverIcon); // Set hover image
        resetButton.setBorderPainted(false);          // Remove button border
        resetButton.setContentAreaFilled(false);      // Transparent background
        resetButton.setFocusPainted(false);           // Remove focus outline

        resetButton.addActionListener(e -> {
            // Define custom options for the confirmation dialog
            Object[] options = {
                    Language.getText("yes"),
                    Language.getText("no")
            };

            // Use the top-level ancestor (e.g., JFrame) as the parent for the dialogs
            Window parentWindow = SwingUtilities.getWindowAncestor(this);

            int confirmation = JOptionPane.showOptionDialog(
                    parentWindow, // Ensure both dialogs use the same parent
                    Language.getText("reset_scores_confirmation"),
                    "",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options, // Custom options for "Yes" and "No"
                    options[0] // Default button
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                Score.resetScores(); // Clear scores from the file

                // Rebuild the ScoreMenu panel
                JPanel newScoreMenu = new ScoreMenu(cardLayout, mainPanel);
                mainPanel.add(newScoreMenu, "scorePanel");
                cardLayout.show(mainPanel, "scorePanel");
            }
        });


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setOpaque(false); // Transparent background
        bottomPanel.add(resetButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

}
