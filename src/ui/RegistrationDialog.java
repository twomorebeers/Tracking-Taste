package ui;

import javax.swing.*;
import java.awt.*;
import database.dao.UserDAO;
import javax.swing.border.EmptyBorder;

public class RegistrationDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private boolean registrationSuccessful = false;

    public RegistrationDialog(JFrame parent) {
        super(parent, "Register New Account", true);
        setupUI();
    }

    private void setupUI() {
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Create New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Confirm Password:"));
        inputPanel.add(confirmPasswordField);
        
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        registerButton.setBackground(new Color(41, 128, 185));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> attemptRegistration());

        cancelButton.addActionListener(e -> {
            registrationSuccessful = false;
            dispose();
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        getRootPane().setDefaultButton(registerButton);
    }

    private void attemptRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match.",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            confirmPasswordField.setText("");
            return;
        }

        // Check if username exists
        if (UserDAO.userExists(username)) {
            JOptionPane.showMessageDialog(this,
                "Username already exists. Please choose another.",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt registration
        if (UserDAO.registerUser(username, password)) {
            registrationSuccessful = true;
            JOptionPane.showMessageDialog(this,
                "Registration successful! You can now login.",
                "Registration Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration failed. Please try again.",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isRegistrationSuccessful() {
        return registrationSuccessful;
    }
}
