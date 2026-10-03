package com.school.view;

import com.school.controller.SchoolController;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog for creating new student and teacher accounts
 */
public class AccountCreationDialog extends JDialog {
    private JTextField idField, usernameField, fullNameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private boolean accountCreated = false;
    private SchoolController schoolController = new SchoolController();
    
    public AccountCreationDialog(JFrame parent) {
        super(parent, "Create New Account", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Role selector
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        JLabel roleLabel = new JLabel("Account Type:");
        mainPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"STUDENT", "TEACHER"});
        mainPanel.add(roleCombo, gbc);
        
        // ID Field
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel idLabel = new JLabel("ID:");
        mainPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        idField = new JTextField();
        mainPanel.add(idField, gbc);
        
        // Username Field
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel userLabel = new JLabel("Username:");
        mainPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField();
        mainPanel.add(usernameField, gbc);
        
        // Password Field
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel passLabel = new JLabel("Password:");
        mainPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        mainPanel.add(passwordField, gbc);
        
        // Full Name Field
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel nameLabel = new JLabel("Full Name:");
        mainPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        fullNameField = new JTextField();
        mainPanel.add(fullNameField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton createBtn = new JButton("Create Account");
        createBtn.addActionListener(e -> createAccount());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void createAccount() {
        String id = idField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String fullName = fullNameField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();
        
        // Validation
        if (id.isEmpty() || username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if username exists
        if (schoolController.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create account
        boolean success = false;
        if ("STUDENT".equals(role)) {
            success = schoolController.createStudentAccount(id, username, password, fullName);
        } else if ("TEACHER".equals(role)) {
            success = schoolController.createTeacherAccount(id, username, password, fullName);
        }
        
        if (success) {
            accountCreated = true;
            JOptionPane.showMessageDialog(this, 
                role + " account created successfully!\nUsername: " + username + "\nPassword: " + password,
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        idField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        fullNameField.setText("");
    }
    
    public boolean isAccountCreated() {
        return accountCreated;
    }
}
