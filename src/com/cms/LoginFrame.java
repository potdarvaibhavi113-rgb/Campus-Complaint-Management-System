
package com.cms;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {

        setTitle("Complaint Management System - Login");
        setSize(420, 320);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title

        JLabel titleLabel = new JLabel(
            "System Login",
            JLabel.CENTER
        );

        titleLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 20)
        );

        titleLabel.setForeground(
            new Color(41, 128, 185)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        // Username

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(new JLabel("Username:"), gbc);

        userField = new JTextField(15);

        gbc.gridx = 1;

        panel.add(userField, gbc);

        // Password

        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(new JLabel("Password:"), gbc);

        passField = new JPasswordField(15);

        gbc.gridx = 1;

        panel.add(passField, gbc);

        // Login Button

        JButton loginBtn = new JButton("Login");

        loginBtn.setBackground(
            new Color(41, 128, 185)
        );

        loginBtn.setForeground(Color.WHITE);

        loginBtn.setFocusPainted(false);

        loginBtn.setOpaque(true);

        loginBtn.setBorderPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 3;

        panel.add(loginBtn, gbc);

        // Register Button

        JButton registerBtn =
            new JButton("Register Student");

        registerBtn.setBackground(
            new Color(39, 174, 96)
        );

        registerBtn.setForeground(Color.WHITE);

        registerBtn.setFocusPainted(false);

        registerBtn.setOpaque(true);

        registerBtn.setBorderPainted(false);

        gbc.gridx = 1;

        panel.add(registerBtn, gbc);

        // Button Actions

        loginBtn.addActionListener(
            e -> handleLogin()
        );

        passField.addActionListener(
            e -> handleLogin()
        );

        registerBtn.addActionListener(
            e -> new RegisterFrame()
        );

        add(panel);

        setVisible(true);
    }

    // =====================================
    // LOGIN METHOD
    // =====================================

    private void handleLogin() {

        String username =
            userField.getText().trim();

        String password =
            new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Fetch user ID, username and role

        String query =
            "SELECT id, username, role FROM users " +
            "WHERE username = ? AND password = ?";

        try (
            Connection conn =
                DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query)
        ) {

            pstmt.setString(1, username);

            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    int userId =
                        rs.getInt("id");

                    String loggedUsername =
                        rs.getString("username");

                    String role =
                        rs.getString("role");

                    // STUDENT LOGIN

                    if ("Student".equalsIgnoreCase(role)) {

                        JOptionPane.showMessageDialog(
                            this,
                            "Student Login Successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                        dispose();

                        StudentDashboardFrame student =
                            new StudentDashboardFrame(
                                userId,
                                loggedUsername
                            );

                        student.setLocationRelativeTo(null);

                        student.setVisible(true);

                    }

                    // ADMIN LOGIN

                    else if ("Admin".equalsIgnoreCase(role)) {

                        JOptionPane.showMessageDialog(
                            this,
                            "Admin Login Successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                        dispose();

                        // IMPORTANT:
                        // Open ONLY AdminDashboardFrame.
                        // Do not open ComplaintApp here.

                        AdminDashboardFrame admin =
                            new AdminDashboardFrame();

                        admin.setLocationRelativeTo(null);

                        admin.setVisible(true);

                    }

                    // INVALID ROLE

                    else {

                        JOptionPane.showMessageDialog(
                            this,
                            "Invalid user role!",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Database Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================
    // MAIN METHOD
    // =====================================

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {

            new LoginFrame();

        });
    }
}
