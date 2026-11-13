package com.cms;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public RegisterFrame() {

        setTitle("Student Registration");
        setSize(400, 280);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title

        JLabel titleLabel = new JLabel(
            "Create Student Account",
            JLabel.CENTER
        );

        titleLabel.setFont(
            new Font("Segoe UI", Font.BOLD, 18)
        );

        titleLabel.setForeground(new Color(39, 174, 96));

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

        // Register Button

        JButton saveBtn = new JButton("Register");

        saveBtn.setBackground(new Color(39, 174, 96));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setOpaque(true);
        saveBtn.setBorderPainted(false);

        gbc.gridx = 1;
        gbc.gridy = 3;

        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> registerStudent());

        passField.addActionListener(e -> registerStudent());

        add(panel);

        setVisible(true);
    }

    private void registerStudent() {

        String username = userField.getText().trim();

        String password =
            new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Fields cannot be empty.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (password.length() < 6) {

            JOptionPane.showMessageDialog(
                this,
                "Password must contain at least 6 characters.",
                "Weak Password",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String query =
            "INSERT INTO users (username, password, role) " +
            "VALUES (?, ?, 'Student')";

        try (
            Connection conn = DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query)
        ) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Registration successful! You can now log in.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (SQLException ex) {

            if ("23000".equals(ex.getSQLState())) {

                JOptionPane.showMessageDialog(
                    this,
                    "Username already exists. Please choose another.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
                );

            } else {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
