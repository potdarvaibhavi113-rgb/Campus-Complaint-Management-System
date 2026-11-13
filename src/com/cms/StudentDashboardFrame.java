package com.cms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class StudentDashboardFrame extends JFrame {

    private int userId;
    private String username;

    private JComboBox<String> categoryBox;
    private JTextArea descArea;

    private JTextField searchField;

    private JTable studentTable;
    private DefaultTableModel studentTableModel;

    public StudentDashboardFrame(int userId, String username) {

        this.userId = userId;
        this.username = username;

        setTitle(
            "Campus Complaint Management System - Student Dashboard"
        );

        setSize(1100, 700);

        setMinimumSize(new Dimension(900, 600));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        tabs.addTab(
            "Submit Complaint",
            createSubmitPanel()
        );

        tabs.addTab(
            "Track Complaints",
            createTrackerPanel()
        );

        add(tabs);

        setVisible(true);
    }

    // =========================================
    // SUBMIT COMPLAINT PANEL
    // =========================================

    private JPanel createSubmitPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(new Color(245, 247, 250));

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                25, 30, 25, 30
            )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(12, 12, 12, 12);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;

        // Heading

        JLabel heading = new JLabel(
            "Submit a New Campus Grievance",
            JLabel.CENTER
        );

        heading.setFont(
            new Font("Times New Roman", Font.BOLD, 22)
        );

        heading.setForeground(new Color(41, 128, 185));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(heading, gbc);

        // Student Name

        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;

        gbc.weightx = 0.2;

        panel.add(
            new JLabel("Student Name:"),
            gbc
        );

        JTextField nameField = new JTextField(username);

        nameField.setEditable(false);

        gbc.gridx = 1;

        gbc.weightx = 0.8;

        panel.add(nameField, gbc);

        // Category

        gbc.gridy = 2;
        gbc.gridx = 0;

        gbc.weightx = 0.2;

        panel.add(
            new JLabel("Category:"),
            gbc
        );

        String[] categories = {
            "Infrastructure",
            "IT & Network",
            "Hostel & Mess",
            "Library",
            "E-Waste / Devices",
            "Other"
        };

        categoryBox = new JComboBox<>(categories);

        gbc.gridx = 1;

        gbc.weightx = 0.8;

        panel.add(categoryBox, gbc);

        // Description

        gbc.gridy = 3;
        gbc.gridx = 0;

        gbc.weightx = 0.2;

        panel.add(
            new JLabel("Description:"),
            gbc
        );

        descArea = new JTextArea(6, 25);

        descArea.setLineWrap(true);

        descArea.setWrapStyleWord(true);

        JScrollPane descScroll =
            new JScrollPane(descArea);

        descScroll.setPreferredSize(
            new Dimension(300, 130)
        );

        gbc.gridx = 1;

        gbc.weightx = 0.8;

        gbc.fill = GridBagConstraints.BOTH;

        panel.add(descScroll, gbc);

        // Submit Button

        JButton submitBtn = new JButton(
            "Submit Complaint"
        );

        submitBtn.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        submitBtn.setBackground(
            new Color(41, 128, 185)
        );

        submitBtn.setForeground(Color.WHITE);

        submitBtn.setFocusPainted(false);

        submitBtn.setOpaque(true);

        submitBtn.setBorderPainted(false);

        submitBtn.setPreferredSize(
            new Dimension(200, 45)
        );

        JPanel buttonPanel =
            new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setBackground(
            new Color(245, 247, 250)
        );

        buttonPanel.add(submitBtn);

        gbc.gridy = 4;
        gbc.gridx = 1;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(buttonPanel, gbc);

        submitBtn.addActionListener(
            e -> submitComplaint()
        );

        return panel;
    }

    // =========================================
    // TRACK COMPLAINTS PANEL
    // =========================================

    private JPanel createTrackerPanel() {

        JPanel panel = new JPanel(
            new BorderLayout(10, 10)
        );

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15, 15, 15, 15
            )
        );

        panel.setBackground(
            new Color(245, 247, 250)
        );

        // Top Panel

        JPanel topPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 12, 10)
        );

        topPanel.setBackground(
            new Color(245, 247, 250)
        );

        topPanel.add(
            new JLabel("Search by Category:")
        );

        searchField = new JTextField(18);

        topPanel.add(searchField);

        JButton searchBtn = new JButton("Search");

        styleButton(
            searchBtn,
            new Color(41, 128, 185)
        );

        topPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh");

        styleButton(
            refreshBtn,
            new Color(108, 117, 125)
        );

        topPanel.add(refreshBtn);

        panel.add(
            topPanel,
            BorderLayout.NORTH
        );

        // Table

        String[] columns = {
            "ID",
            "Student Name",
            "Category",
            "Description",
            "Status",
            "Admin Remark",
            "Date"
        };

        studentTableModel =
            new DefaultTableModel(columns, 0) {

                @Override
                public boolean isCellEditable(
                    int row, int column
                ) {
                    return false;
                }
            };

        studentTable = new JTable(studentTableModel);

        studentTable.setRowHeight(28);

        panel.add(
            new JScrollPane(studentTable),
            BorderLayout.CENTER
        );

        // Search Action

        searchBtn.addActionListener(e -> {

            String category =
                searchField.getText().trim();

            if (category.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a category.",
                    "Search",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            loadMyComplaints(category, true);
        });

        searchField.addActionListener(
            e -> searchBtn.doClick()
        );

        // Refresh Action

        refreshBtn.addActionListener(e -> {

            searchField.setText("");

            loadMyComplaints("", false);
        });

        loadMyComplaints("", false);

        return panel;
    }

    // =========================================
    // SUBMIT COMPLAINT
    // =========================================

    private void submitComplaint() {

        String category =
            (String) categoryBox.getSelectedItem();

        String description =
            descArea.getText().trim();

        if (description.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter complaint description.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String query =
            "INSERT INTO complaints " +
            "(student_name, category, description, status, user_id) " +
            "VALUES (?, ?, ?, 'Pending', ?)";

        try (
            Connection conn =
                DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query)
        ) {

            pstmt.setString(1, username);

            pstmt.setString(2, category);

            pstmt.setString(3, description);

            pstmt.setInt(4, userId);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Complaint submitted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            descArea.setText("");

            loadMyComplaints("", false);

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

    // =========================================
    // LOAD STUDENT COMPLAINTS
    // =========================================

    private void loadMyComplaints(
        String searchCategory,
        boolean showNotFound
    ) {

        studentTableModel.setRowCount(0);

        String query;

        if (searchCategory.isEmpty()) {

            query =
                "SELECT * FROM complaints " +
                "WHERE user_id = ? " +
                "ORDER BY created_at DESC";

        } else {

            query =
                "SELECT * FROM complaints " +
                "WHERE user_id = ? AND category LIKE ? " +
                "ORDER BY created_at DESC";
        }

        try (
            Connection conn =
                DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query)
        ) {

            pstmt.setInt(1, userId);

            if (!searchCategory.isEmpty()) {

                pstmt.setString(
                    2,
                    "%" + searchCategory + "%"
                );
            }

            int count = 0;

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    studentTableModel.addRow(
                        new Object[]{

                            rs.getInt("id"),

                            rs.getString("student_name"),

                            rs.getString("category"),

                            rs.getString("description"),

                            rs.getString("status"),

                            rs.getString("admin_remark") != null
                                ? rs.getString("admin_remark")
                                : "None",

                            rs.getTimestamp("created_at")
                        }
                    );

                    count++;
                }
            }

            if (showNotFound && count == 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "No complaints found for category: "
                        + searchCategory,
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Database Error: " + e.getMessage()
            );
        }
    }

    // =========================================
    // BUTTON STYLING
    // =========================================

    private void styleButton(
        JButton button,
        Color color
    ) {

        button.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setBorderPainted(false);

    }
}