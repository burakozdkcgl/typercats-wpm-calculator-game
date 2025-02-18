package gui;

import logic.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CustomModesMenu extends JPanel {

    public CustomModesMenu(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Return Button
        ImageIcon idleIcon = new ImageIcon(getClass().getResource("/return_idle.png"));
        JButton topRightButton = new JButton(idleIcon);
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/return_hover.png"));
        topRightButton.setRolloverIcon(hoverIcon);
        topRightButton.setBorderPainted(false);
        topRightButton.setContentAreaFilled(false);
        topRightButton.setFocusPainted(false);
        topRightButton.addActionListener(e -> cardLayout.show(mainPanel, "menuPanel"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(topRightButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel for Two Columns
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        // Button Font and Description Font
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Font descriptionFont = new Font("Arial", Font.PLAIN, 14);

        // Dead Mode Button and Description
        JButton deadModeButton = createStyledButton(Language.getText("dead_mode"), buttonFont);
        deadModeButton.addActionListener(e -> cardLayout.show(mainPanel, "deadModePanel"));
        JTextArea deadModeDescription = createStyledLabel(Language.getText("dead_mode_description"), descriptionFont);

        // Rat Mode Button and Description
        JButton ratModeButton = createStyledButton(Language.getText("rat_mode"), buttonFont);
        ratModeButton.addActionListener(e -> cardLayout.show(mainPanel, "ratModePanel"));
        JTextArea ratModeDescription = createStyledLabel(Language.getText("rat_mode_description"), descriptionFont);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Add spacing between elements
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Dead Mode Button
        gbc.gridx = 0; // Column 1
        gbc.gridy = 0; // Row 1
        centerPanel.add(deadModeButton, gbc);

        // Add Rat Mode Button
        gbc.gridx = 1; // Column 2
        gbc.gridy = 0; // Row 1
        centerPanel.add(ratModeButton, gbc);

        // Add Dead Mode Description
        gbc.gridx = 0; // Column 1
        gbc.gridy = 1; // Row 2
        gbc.anchor = GridBagConstraints.PAGE_START; // Aligns text at the top of the allocated cell
        centerPanel.add(deadModeDescription, gbc);

        // Add Rat Mode Description
        gbc.gridx = 1; // Column 2
        gbc.gridy = 1; // Row 2
        gbc.anchor = GridBagConstraints.PAGE_START; // Aligns text at the top of the allocated cell
        centerPanel.add(ratModeDescription, gbc);



        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);

        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.gridx = 0;
        wrapperGbc.gridy = 0;
        wrapperGbc.anchor = GridBagConstraints.CENTER;
        wrapperGbc.insets = new Insets(-160, 0, 0, 0); // Move the content higher (negative top inset)

        wrapperPanel.add(centerPanel, wrapperGbc);
        add(wrapperPanel, BorderLayout.CENTER);


    }

    private JButton createStyledButton(String text, Font font) {
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
        button.setFont(font);
        button.setPreferredSize(new Dimension(200, 100)); // Explicit button size
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
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

        button.setMaximumSize(new Dimension(200, 100));
        button.setMinimumSize(new Dimension(200, 100));
        button.setPreferredSize(new Dimension(200, 100));


        return button;
    }

    private JTextArea createStyledLabel(String text, Font font) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(font);
        textArea.setForeground(Color.DARK_GRAY);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        textArea.setPreferredSize(new Dimension(180, 250)); // Set a fixed width and height
        return textArea;
    }



}
