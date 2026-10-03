package com.school.view;

import com.school.controller.SchoolController;
import com.school.data.DatabaseConnection;
import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private SchoolController controller = new SchoolController();

    // Colors matching the image
    Color bgColor = new Color(158, 179, 194);    // Muted Blue
    Color btnColor = new Color(47, 85, 101);     // Dark Teal
    Color headerBoxColor = new Color(232, 222, 222); // Light Pinkish-Grey

    public LoginGUI() {
        // Initialize database on first run
        System.out.println("[LOGIN] Current working directory: " + System.getProperty("user.dir"));
        DatabaseConnection.initializeDatabase();
        DatabaseConnection.populateInitialData();
        DatabaseConnection.verifyDatabaseData(); // NEW: Debug verification
        
        setTitle("School Management System - Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout());

        // --- TOP: LOGO ---
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JLabel logoLabel = new JLabel("SCHOOOL", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Serif", Font.BOLD, 30));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        topPanel.add(logoLabel);

        // --- CENTER: LOGIN FORM ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Title Box "Login Access" (Matching the Attendance Record box style)
        JLabel titleLabel = new JLabel("Login Access", SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(headerBoxColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);

        // 2. Email/ID Label and Field
        gbc.gridwidth = 1; gbc.gridy = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.add(userLabel, gbc);

        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        centerPanel.add(userField, gbc);

        // 3. Password Label and Field
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        centerPanel.add(passField, gbc);

        // 4. Forgot Password Link (Visual only)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel forgotLabel = new JLabel("Forgot Password?", SwingConstants.LEFT);
        forgotLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        centerPanel.add(forgotLabel, gbc);

        // 5. Login Button (Styled like the "Record" button)
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(btnColor);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setPreferredSize(new Dimension(150, 45));
        loginBtn.setFocusPainted(false);
        gbc.gridy = 4; gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(loginBtn, gbc);

        // --- BOTTOM: NAV BAR (Visual matching) ---
        JPanel navBar = new JPanel(new GridLayout(1, 5));
        navBar.setPreferredSize(new Dimension(800, 50));
        navBar.setBackground(new Color(220, 220, 220));
        String[] tabs = {"Login", "Attendance Record", "Student Grade", "Announcements", "Manage"};
        for (String tab : tabs) {
            JButton b = new JButton(tab);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            navBar.add(b);
        }

        // --- ACTION LOGIC ---
        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            
            // Debug output
            System.out.println("[DEBUG] Entered user: '" + user + "', pass: '" + pass + "'");
            
            String role = controller.login(user, pass);
            
            System.out.println("[DEBUG] Login result: " + role);

            if (role.equals("TEACHER")) {
                System.out.println("[DEBUG] Login successful - TEACHER");
                new SchoolSystemGUI(true, user);
                this.dispose();
            } else if (role.equals("STUDENT")) {
                System.out.println("[DEBUG] Login successful - STUDENT");
                new SchoolSystemGUI(false, user);
                this.dispose();
            } else {
                System.out.println("[DEBUG] Login failed - showing error");
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}