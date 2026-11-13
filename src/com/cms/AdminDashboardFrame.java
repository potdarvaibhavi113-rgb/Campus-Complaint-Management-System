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

public class AdminDashboardFrame extends JFrame {

    // Track Complaints Components

    private JTextField searchField;

    private JTable studentTable;

    private DefaultTableModel studentTableModel;

    // Admin Dashboard Components

    private JTable adminTable;

    private DefaultTableModel adminTableModel;

    private JComboBox<String> statusUpdateBox;

    private JTextArea adminRemarkArea;

    private int selectedComplaintId = -1;

    public AdminDashboardFrame() {

        setTitle(
            "Campus Complaint Management System - Admin Dashboard"
        );

        setSize(1100, 700);

        setMinimumSize(new Dimension(900, 600));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        // First Tab: Track Complaints

        tabs.addTab(
            "Track Complaints",
            createTrackerPanel()
        );

        // Second Tab: Admin Dashboard

        tabs.addTab(
            "Admin Dashboard",
            createAdminPanel()
        );

        add(tabs);

        setVisible(true);
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
            new JLabel("Search by Student Name:")
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

        // Complaint Table

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

            String studentName =
                searchField.getText().trim();

            if (studentName.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a student name.",
                    "Search",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            loadStudentData(studentName, true);
        });

        searchField.addActionListener(
            e -> searchBtn.doClick()
        );

        // Refresh Action

        refreshBtn.addActionListener(e -> {

            searchField.setText("");

            loadStudentData("", false);
        });

        loadStudentData("", false);

        return panel;
    }

    // =========================================
    // ADMIN DASHBOARD PANEL
    // =========================================

    private JPanel createAdminPanel() {

        JPanel panel = new JPanel(
            new BorderLayout(15, 15)
        );

        panel.setBorder(
            BorderFactory.createEmptyBorder(
                15, 15, 15, 15
            )
        );

        panel.setBackground(
            new Color(245, 247, 250)
        );

        // Admin Table

        String[] columns = {

            "ID",
            "Student",
            "Category",
            "Status",
            "Date"

        };

        adminTableModel =
            new DefaultTableModel(columns, 0) {

                @Override
                public boolean isCellEditable(
                    int row, int column
                ) {

                    return false;
                }
            };

        adminTable = new JTable(adminTableModel);

        adminTable.setRowHeight(28);

        panel.add(
            new JScrollPane(adminTable),
            BorderLayout.CENTER
        );

        // Table Selection

        adminTable.getSelectionModel()
            .addListSelectionListener(e -> {

                if (
                    !e.getValueIsAdjusting()
                    && adminTable.getSelectedRow() != -1
                ) {

                    int row =
                        adminTable.convertRowIndexToModel(
                            adminTable.getSelectedRow()
                        );

                    selectedComplaintId =
                        ((Number) adminTableModel.getValueAt(
                            row, 0
                        )).intValue();

                    loadComplaintDetailsForAdmin(
                        selectedComplaintId
                    );
                }
            });

        // Control Panel

        JPanel controlPanel = new JPanel(
            new GridBagLayout()
        );

        controlPanel.setBorder(
            BorderFactory.createTitledBorder(
                "Manage Ticket Status"
            )
        );

        controlPanel.setBackground(Color.WHITE);

        controlPanel.setPreferredSize(
            new Dimension(320, 250)
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets = new Insets(10, 8, 10, 8);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Status Label

        gbc.gridx = 0;
        gbc.gridy = 0;

        controlPanel.add(
            new JLabel("Update Status:"),
            gbc
        );

        // Status ComboBox

        statusUpdateBox = new JComboBox<>(
            new String[]{

                "Pending",
                "In Progress",
                "Resolved",
                "Rejected"

            }
        );

        gbc.gridx = 1;

        controlPanel.add(
            statusUpdateBox,
            gbc
        );

        // Remark Label

        gbc.gridx = 0;
        gbc.gridy = 1;

        controlPanel.add(
            new JLabel("Admin Remark:"),
            gbc
        );

        // Remark Area

        adminRemarkArea = new JTextArea(5, 15);

        adminRemarkArea.setLineWrap(true);

        adminRemarkArea.setWrapStyleWord(true);

        JScrollPane remarkScroll =
            new JScrollPane(adminRemarkArea);

        gbc.gridx = 1;

        controlPanel.add(
            remarkScroll,
            gbc
        );

        // Update Button

        JButton updateBtn =
            new JButton("Update Ticket");

        styleButton(
            updateBtn,
            new Color(39, 174, 96)
        );

        gbc.gridx = 1;
        gbc.gridy = 2;

        controlPanel.add(
            updateBtn,
            gbc
        );

        panel.add(
            controlPanel,
            BorderLayout.EAST
        );

        // Update Action

        updateBtn.addActionListener(
            e -> updateAdminTicket()
        );

        // Load Data

        loadAdminData();

        return panel;
    }

    // =========================================
    // LOAD ALL STUDENT COMPLAINTS
    // =========================================

    private void loadStudentData(
        String searchName,
        boolean showNotFound
    ) {

        studentTableModel.setRowCount(0);

        String query;

        if (searchName.isEmpty()) {

            query =
                "SELECT * FROM complaints " +
                "ORDER BY created_at DESC";

        } else {

            query =
                "SELECT * FROM complaints " +
                "WHERE student_name LIKE ? " +
                "ORDER BY created_at DESC";
        }

        try (
            Connection conn =
                DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query)
        ) {

            if (!searchName.isEmpty()) {

                pstmt.setString(
                    1,
                    "%" + searchName + "%"
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
                    "No complaints found for student: "
                        + searchName,
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
    // LOAD ADMIN DASHBOARD DATA
    // =========================================

    private void loadAdminData() {

        adminTableModel.setRowCount(0);

        String query =
            "SELECT id, student_name, category, " +
            "status, created_at FROM complaints " +
            "ORDER BY created_at DESC";

        try (
            Connection conn =
                DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery()
        ) {

            while (rs.next()) {

                adminTableModel.addRow(
                    new Object[]{

                        rs.getInt("id"),

                        rs.getString("student_name"),

                        rs.getString("category"),

                        rs.getString("status"),

                        rs.getTimestamp("created_at")

                    }
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
    // LOAD SELECTED COMPLAINT DETAILS
    // =========================================

    private void loadComplaintDetailsForAdmin(int id) {

        String query =
            "SELECT status, admin_remark " +
            "FROM complaints WHERE id = ?";

        try (
            Connection conn =
                DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query)
        ) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    statusUpdateBox.setSelectedItem(
                        rs.getString("status")
                    );

                    adminRemarkArea.setText(
                        rs.getString("admin_remark") != null
                            ? rs.getString("admin_remark")
                            : ""
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Unable to load complaint details."
            );
        }
    }

    // =========================================
    // UPDATE COMPLAINT STATUS AND REMARK
    // =========================================

    private void updateAdminTicket() {

        if (selectedComplaintId == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a complaint first.",
                "Warning",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String status =
            (String) statusUpdateBox.getSelectedItem();

        String remark =
            adminRemarkArea.getText().trim();

        String query =
            "UPDATE complaints " +
            "SET status = ?, admin_remark = ? " +
            "WHERE id = ?";

        try (
            Connection conn =
                DBConnection.getConnection();

            PreparedStatement pstmt =
                conn.prepareStatement(query)
        ) {

            pstmt.setString(1, status);

            pstmt.setString(2, remark);

            pstmt.setInt(3, selectedComplaintId);

            int updated = pstmt.executeUpdate();

            if (updated > 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Ticket updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                // Reset selected complaint

                selectedComplaintId = -1;

                adminTable.clearSelection();

                adminRemarkArea.setText("");

                statusUpdateBox.setSelectedIndex(0);

                // Refresh both tabs' data

                loadAdminData();

                loadStudentData("", false);
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
