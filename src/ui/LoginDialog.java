package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import database.dao.UserDAO;
import models.User;
import javax.swing.border.EmptyBorder;

public class LoginDialog extends JDialog {
    private User authenticatedUser = null;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean loginSuccessful = false;

    public LoginDialog(JFrame parent) {
        super(parent, "Login to Tracking Taste", true);
        setupUI();
    }

    private void setupUI() {
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Logo or title
        JLabel titleLabel = new JLabel("Tracking Taste", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 10));
        
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Login and Cancel buttons in one row
        JPanel loginButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> attemptLogin());

        cancelButton.addActionListener(e -> {
            loginSuccessful = false;
            dispose();
        });

        loginButtonsPanel.add(loginButton);
        loginButtonsPanel.add(cancelButton);
        
        // Register button in second row
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton registerButton = new JButton("Register New Account");
        registerButton.setForeground(new Color(41, 128, 185));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> showRegistrationDialog());
        registerPanel.add(registerButton);
        
        buttonPanel.add(loginButtonsPanel);
        buttonPanel.add(registerPanel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to dialog
        add(mainPanel);

        // Handle Enter key
        getRootPane().setDefaultButton(loginButton);
        
        // Handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginSuccessful = false;
                dispose();
            }
        });
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password.",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        authenticatedUser = UserDAO.authenticateUser(username, password);
        
        if (authenticatedUser != null) {
            loginSuccessful = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void showRegistrationDialog() {
        RegistrationDialog registrationDialog = new RegistrationDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        registrationDialog.setVisible(true);
        
        if (registrationDialog.isRegistrationSuccessful()) {
            usernameField.setText("");
            passwordField.setText("");
        }
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
