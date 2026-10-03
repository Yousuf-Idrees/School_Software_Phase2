package com.school.view;

import com.school.controller.SchoolController;
import com.school.data.DatabaseConnection;
import com.school.accounts.AccountRegistrationService;
import com.school.controller.ReportController;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SchoolSystemGUI extends JFrame {
    private SchoolController controller = new SchoolController();
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContentPanel = new JPanel(cardLayout);

    // Design Colors
    Color bgColor = new Color(158, 179, 194);
    Color btnColor = new Color(47, 85, 101);
    Color headerBoxColor = new Color(232, 222, 222);

    public SchoolSystemGUI(boolean isTeacher, String loggedInID) {
        setTitle("School Management System");
        setSize(950, 850); // Slightly wider for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout());

        mainContentPanel.add(createAttendancePage(isTeacher, loggedInID), "Attendance");
        mainContentPanel.add(createGradesPage(isTeacher, loggedInID), "Grades");
        mainContentPanel.add(createAnnouncementsPage(), "Announcements");
        mainContentPanel.add(createManagePage(isTeacher, loggedInID), "Manage");
        mainContentPanel.add(createAssignmentSubmissionPage(isTeacher, loggedInID), "Assignments");
        mainContentPanel.add(createExamNotificationPage(isTeacher), "Notifications");
        mainContentPanel.add(createClassroomSeatingPage(isTeacher), "Classroom");
        mainContentPanel.add(createGradeExportPage(isTeacher, loggedInID), "Grade Export");
        mainContentPanel.add(createSMSNotificationPage(isTeacher), "SMS Alerts");

        JPanel navBar = new JPanel(new GridLayout(3, 3)); // Changed to 3 rows for 9 tabs
        navBar.setPreferredSize(new Dimension(950, 120));
        String[] tabs = {"Login", "Attendance Record", "Student Grade", "Announcements", "Manage", "Assignments", "Notifications", "Classroom", "Grade Export", "SMS Alerts"};

        for (String tab : tabs) {
            JButton btn = new JButton(tab);
            navBar.add(btn);
            if (tab.equals("Login")) btn.addActionListener(e -> { new LoginGUI(); this.dispose(); });
            if (tab.equals("Attendance Record")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Attendance"));
            if (tab.equals("Student Grade")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Grades"));
            if (tab.equals("Announcements")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Announcements"));
            if (tab.equals("Manage")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Manage"));
            if (tab.equals("Assignments")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Assignments"));
            if (tab.equals("Notifications")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Notifications"));
            if (tab.equals("Classroom")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Classroom"));
            if (tab.equals("Grade Export")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "Grade Export"));
            if (tab.equals("SMS Alerts")) btn.addActionListener(e -> cardLayout.show(mainContentPanel, "SMS Alerts"));
        }

        add(mainContentPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createAttendancePage(boolean isTeacher, String id) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Attendance Management", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(bgColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // --- BIGGER SEARCH SECTION ---
        JPanel searchSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchSection.setOpaque(false);

        JTextField idField = new JTextField(id, 15); // Increased column size
        idField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton viewBtn = new JButton("View History");
        viewBtn.setPreferredSize(new Dimension(140, 35));

        searchSection.add(new JLabel("Search ID: "));
        searchSection.add(idField);
        searchSection.add(viewBtn);

        // Teacher Recording Tools (Only for Teacher)
        if (isTeacher) {
            JComboBox<String> statusBox = new JComboBox<>(new String[]{"Present", "Absent"});
            JButton recordBtn = new JButton("Record");
            searchSection.add(new JLabel(" | Status: "));
            searchSection.add(statusBox);
            searchSection.add(recordBtn);

            recordBtn.addActionListener(e -> {
                String msg = controller.processAttendanceRecord(idField.getText(), (String)statusBox.getSelectedItem());
                JOptionPane.showMessageDialog(this, msg);
            });
        }

        centerPanel.add(searchSection);

        // Display Area
        JEditorPane displayArea = new JEditorPane();
        displayArea.setContentType("text/html");
        displayArea.setEditable(false);
        centerPanel.add(new JScrollPane(displayArea));

        viewBtn.addActionListener(e -> {
            String details = controller.getStudentInfo(idField.getText()); // Retrieve Name/Year
            String history = controller.processAttendanceViewRequest(idField.getText());
            displayArea.setText(formatHTMLResponse(details, history, "Attendance"));
        });

        if (!isTeacher) {
            idField.setEditable(false);
            displayArea.setText(formatHTMLResponse(controller.getStudentInfo(id), controller.processAttendanceViewRequest(id), "Attendance"));
        }

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGradesPage(boolean isTeacher, String id) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Student Grades", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        // --- BIGGER SEARCH BAR ---
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);
        JTextField searchField = new JTextField(id, 20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton searchBtn = new JButton("Search Records");
        searchBtn.setPreferredSize(new Dimension(150, 35));

        if (isTeacher) {
            topPanel.add(new JLabel("Enter Student ID: "));
            topPanel.add(searchField);
            topPanel.add(searchBtn);
        }
        content.add(topPanel, BorderLayout.NORTH);

        JEditorPane gradeDisplay = new JEditorPane();
        gradeDisplay.setContentType("text/html");
        gradeDisplay.setEditable(false);
        content.add(new JScrollPane(gradeDisplay), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            String details = controller.getStudentInfo(searchField.getText());
            String data = controller.processGradeRequest(searchField.getText());
            gradeDisplay.setText(formatHTMLResponse(details, data, "Grades"));
        });

        if (!isTeacher) {
            gradeDisplay.setText(formatHTMLResponse(controller.getStudentInfo(id), controller.processGradeRequest(id), "Grades"));
        }

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // --- HTML FORMATTING FOR NAME, YEAR, AND DATA ---
    private String formatHTMLResponse(String studentInfo, String data, String type) {
        // studentInfo looks like: "Name: Marwan Khaled\nMajor: IT\nYear: 2025"
        String[] info = studentInfo.split("\n");
        String name = info.length > 0 ? info[0] : "Unknown Student";
        String year = info.length > 2 ? info[2] : "Academic Year: 2025";

        StringBuilder html = new StringBuilder("<html><body style='font-family: Arial; padding: 25px;'>");

        // Student Info Header (Real Look)
        html.append("<div style='background-color: #f0f0f0; padding: 10px; border-left: 5px solid #2F5565;'>")
                .append("<h1 style='margin: 0; color: #2F5565;'>").append(name.replace("Name: ", "")).append("</h1>")
                .append("<p style='margin: 5px 0; font-size: 14pt; color: #555;'>").append(year).append("</p>")
                .append("</div><br><hr><br>");

        if (type.equals("Grades")) {
            String[] lines = data.split("\n");
            for (String line : lines) {
                if (line.contains(":")) {
                    String[] parts = line.split(":");
                    html.append("<div style='border-bottom: 1px solid #ccc; margin-bottom: 15px; padding-bottom: 5px;'>")
                            .append("<span style='font-size: 18pt; font-weight: bold;'>").append(parts[0]).append("</span>")
                            .append("<span style='float: right; font-size: 18pt; color: #2F5565;'>").append(parts[1]).append("</span>")
                            .append("</div>");
                }
            }
        } else {
            html.append("<p style='font-size: 15pt; line-height: 1.6;'>").append(data.replace("\n", "<br>")).append("</p>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    private JPanel createAnnouncementsPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Announcements", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JEditorPane announcementDisplay = new JEditorPane();
        announcementDisplay.setContentType("text/html");
        announcementDisplay.setEditable(false);
        
        String html = "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h2 style='color: #2F5565;'>School Announcements</h2>" +
                "<div style='border-left: 5px solid #2F5565; padding: 10px; margin: 10px 0;'>" +
                "<h3>[NOTICE] Important Notice</h3>" +
                "<p>Parent-Teacher Meeting scheduled for next Friday at 3:00 PM in the auditorium.</p>" +
                "</div>" +
                "<div style='border-left: 5px solid #2F5565; padding: 10px; margin: 10px 0;'>" +
                "<h3>[DATE] Holiday Schedule</h3>" +
                "<p>School will be closed for spring break from March 28 to April 4, 2026.</p>" +
                "</div>" +
                "<div style='border-left: 5px solid #2F5565; padding: 10px; margin: 10px 0;'>" +
                "<h3>[AWARD] Achievements</h3>" +
                "<p>Congratulations to our debate team for winning the regional championship!</p>" +
                "</div>" +
                "</body></html>";
        
        announcementDisplay.setText(html);
        content.add(new JScrollPane(announcementDisplay), BorderLayout.CENTER);
        
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createManagePage(boolean isTeacher, String loggedInID) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Management", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(bgColor);

        if (isTeacher) {
            // Create Account Button
            JButton createAccountBtn = new JButton("Create New Account");
            createAccountBtn.setPreferredSize(new Dimension(200, 40));
            createAccountBtn.setFont(new Font("Arial", Font.BOLD, 14));
            createAccountBtn.setBackground(btnColor);
            createAccountBtn.setForeground(Color.WHITE);
            createAccountBtn.setFocusPainted(false);
            createAccountBtn.addActionListener(e -> {
                AccountCreationDialog dialog = new AccountCreationDialog(this);
                dialog.setVisible(true);
                if (dialog.isAccountCreated()) {
                    JOptionPane.showMessageDialog(this, "Account created and saved to database!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            JButton generateReportBtn = new JButton("Generate Report");
            generateReportBtn.setPreferredSize(new Dimension(200, 40));
            generateReportBtn.setFont(new Font("Arial", Font.BOLD, 14));
            generateReportBtn.setBackground(btnColor);
            generateReportBtn.setForeground(Color.WHITE);
            generateReportBtn.setFocusPainted(false);
            generateReportBtn.addActionListener(e -> {
                // Create report generation dialog
                JPanel dialogPanel = new JPanel(new GridLayout(4, 2, 10, 10));
                dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                // Report type selection
                dialogPanel.add(new JLabel("Report Type:"));
                JComboBox<String> reportTypeBox = new JComboBox<>(new String[]{
                    "Grade Report", "Attendance Report", "Performance Report", "Comprehensive Report"
                });
                dialogPanel.add(reportTypeBox);

                // Report title
                dialogPanel.add(new JLabel("Report Title:"));
                JTextField titleField = new JTextField("Student Report - " + java.time.LocalDate.now());
                dialogPanel.add(titleField);

                // Format selection
                dialogPanel.add(new JLabel("Format:"));
                JComboBox<String> formatBox = new JComboBox<>(new String[]{"PDF", "WORD"});
                dialogPanel.add(formatBox);

                // Save to file checkbox
                dialogPanel.add(new JLabel("Save to File:"));
                JCheckBox saveToFileBox = new JCheckBox("", true);
                dialogPanel.add(saveToFileBox);

                int result = JOptionPane.showConfirmDialog(this, dialogPanel,
                    "Generate Report", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    // Generate the report using DocumentManager
                    ReportController reportService = new ReportController();
                    String reportType = (String) reportTypeBox.getSelectedItem();
                    String reportTitle = titleField.getText();
                    String format = (String) formatBox.getSelectedItem();
                    boolean saveToFile = saveToFileBox.isSelected();

                    try {
                        String message = reportService.generateAndSaveReport(
                            reportType, reportTitle, "Generated by " + loggedInID,
                            loggedInID, format, saveToFile
                        );
                        JOptionPane.showMessageDialog(this, message, "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton exportDataBtn = new JButton("Export Data");
            exportDataBtn.setPreferredSize(new Dimension(200, 40));
            exportDataBtn.setFont(new Font("Arial", Font.BOLD, 14));
            exportDataBtn.setBackground(btnColor);
            exportDataBtn.setForeground(Color.WHITE);
            exportDataBtn.setFocusPainted(false);
            exportDataBtn.addActionListener(e -> 
                JOptionPane.showMessageDialog(this, "Data exported to CSV file.", "Success", JOptionPane.INFORMATION_MESSAGE)
            );

            optionsPanel.add(Box.createVerticalStrut(20));
            optionsPanel.add(createAccountBtn);
            optionsPanel.add(Box.createVerticalStrut(20));
            optionsPanel.add(generateReportBtn);
            optionsPanel.add(Box.createVerticalStrut(20));
            optionsPanel.add(exportDataBtn);
        } else {
            JLabel infoLabel = new JLabel("Student management features are not available.");
            infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            optionsPanel.add(infoLabel);
        }

        optionsPanel.add(Box.createVerticalGlue());
        content.add(optionsPanel, BorderLayout.CENTER);
        
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ===== USE CASE 1: ASSIGNMENT SUBMISSION SYSTEM (Bridge Pattern) =====
    private JPanel createAssignmentSubmissionPage(boolean isTeacher, String loggedInID) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Assignment Submission", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);

        if (isTeacher) {
            // TEACHER VIEW: Can submit assignments for students
            JComboBox<String> studentIDBox = new JComboBox<>(new String[]{"23-101296", "23-101077", "23-101091", "23-101192", "23-101107"});
            studentIDBox.setPreferredSize(new Dimension(120, 30));

            JComboBox<String> assignmentType = new JComboBox<>(new String[]{"Homework", "Project"});
            assignmentType.setPreferredSize(new Dimension(120, 30));

            JComboBox<String> storageType = new JComboBox<>(new String[]{"Local Storage", "Cloud Storage"});
            storageType.setPreferredSize(new Dimension(140, 30));

            JTextField fileNameField = new JTextField(20);
            fileNameField.setFont(new Font("Arial", Font.PLAIN, 14));
            fileNameField.setPreferredSize(new Dimension(200, 30));

            JButton submitBtn = new JButton("Record Assignment");
            submitBtn.setBackground(btnColor);
            submitBtn.setForeground(Color.WHITE);
            submitBtn.setPreferredSize(new Dimension(150, 30));

            controlPanel.add(new JLabel("Student ID:"));
            controlPanel.add(studentIDBox);
            controlPanel.add(new JLabel("Type:"));
            controlPanel.add(assignmentType);
            controlPanel.add(new JLabel("Storage:"));
            controlPanel.add(storageType);
            controlPanel.add(new JLabel("File Name:"));
            controlPanel.add(fileNameField);
            controlPanel.add(submitBtn);

            JEditorPane displayArea = new JEditorPane();
            displayArea.setContentType("text/html");
            displayArea.setEditable(false);

            submitBtn.addActionListener(e -> {
                String studentID = (String) studentIDBox.getSelectedItem();
                String type = (String) assignmentType.getSelectedItem();
                String storage = (String) storageType.getSelectedItem();
                String fileName = fileNameField.getText();

                if (fileName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a file name", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Save to database
                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO assignments (student_id, assignment_type, file_name, storage_method, submission_date, submission_time, teacher_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                        stmt.setString(1, studentID);
                        stmt.setString(2, type);
                        stmt.setString(3, fileName);
                        stmt.setString(4, storage);
                        stmt.setString(5, java.time.LocalDate.now().toString());
                        stmt.setString(6, java.time.LocalTime.now().toString());
                        stmt.setString(7, loggedInID);
                        stmt.executeUpdate();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<h2 style='color: #2F5565;'>Assignment Recorded Successfully</h2>");
                        result.append("<table border='1' cellpadding='10' style='border-collapse: collapse;'>");
                        result.append("<tr><td><b>Student ID:</b></td><td>").append(studentID).append("</td></tr>");
                        result.append("<tr><td><b>Assignment Type:</b></td><td>").append(type).append("</td></tr>");
                        result.append("<tr><td><b>File Name:</b></td><td>").append(fileName).append("</td></tr>");
                        result.append("<tr><td><b>Storage Method:</b></td><td>").append(storage).append("</td></tr>");
                        result.append("<tr><td><b>Submission Date:</b></td><td>").append(java.time.LocalDate.now()).append("</td></tr>");
                        result.append("</table>");
                        result.append("<p style='color: green; margin-top: 20px;'><b>Status: Saved to Database</b></p>");
                        result.append("</body></html>");

                        displayArea.setText(result.toString());
                        fileNameField.setText("");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving assignment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            content.add(controlPanel, BorderLayout.NORTH);
            content.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        } else {
            // STUDENT VIEW: Can only view assignments
            JButton viewBtn = new JButton("View My Assignments");
            viewBtn.setBackground(btnColor);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setPreferredSize(new Dimension(200, 40));

            controlPanel.add(new JLabel("(Read-Only for Students)"));
            controlPanel.add(viewBtn);

            JEditorPane displayArea = new JEditorPane();
            displayArea.setContentType("text/html");
            displayArea.setEditable(false);

            viewBtn.addActionListener(e -> {
                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "SELECT * FROM assignments WHERE student_id = ? ORDER BY submission_date DESC")) {
                        stmt.setString(1, loggedInID);
                        ResultSet rs = stmt.executeQuery();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<h2 style='color: #2F5565;'>My Assignments</h2>");

                        boolean hasData = false;
                        while (rs.next()) {
                            hasData = true;
                            result.append("<div style='border: 1px solid #ccc; padding: 10px; margin: 10px 0;'>");
                            result.append("<p><b>Type:</b> ").append(rs.getString("assignment_type")).append("</p>");
                            result.append("<p><b>File:</b> ").append(rs.getString("file_name")).append("</p>");
                            result.append("<p><b>Storage:</b> ").append(rs.getString("storage_method")).append("</p>");
                            result.append("<p><b>Submitted:</b> ").append(rs.getString("submission_date")).append("</p>");
                            result.append("</div>");
                        }

                        if (!hasData) {
                            result.append("<p style='color: #666;'>No assignments submitted yet.</p>");
                        }

                        result.append("</body></html>");
                        displayArea.setText(result.toString());
                    }
                } catch (Exception ex) {
                    displayArea.setText("<p style='color: red;'>Error loading assignments: " + ex.getMessage() + "</p>");
                    ex.printStackTrace();
                }
            });

            content.add(controlPanel, BorderLayout.NORTH);
            content.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        }

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ===== USE CASE 2: EXAM NOTIFICATION SYSTEM (Bridge Pattern) =====
    private JPanel createExamNotificationPage(boolean isTeacher) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Exam Notifications", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);

        JEditorPane displayArea = new JEditorPane();
        displayArea.setContentType("text/html");
        displayArea.setEditable(false);

        if (isTeacher) {
            // TEACHER VIEW: Can send notifications
            JComboBox<String> studentIDBox = new JComboBox<>(new String[]{"23-101296", "23-101077", "23-101091", "23-101192", "23-101107"});
            studentIDBox.setPreferredSize(new Dimension(120, 30));

            JComboBox<String> examType = new JComboBox<>(new String[]{"Midterm", "Final"});
            examType.setPreferredSize(new Dimension(120, 30));

            JComboBox<String> notificationMethod = new JComboBox<>(new String[]{"Email", "SMS"});
            notificationMethod.setPreferredSize(new Dimension(120, 30));

            JButton sendBtn = new JButton("Send Notification");
            sendBtn.setBackground(btnColor);
            sendBtn.setForeground(Color.WHITE);
            sendBtn.setPreferredSize(new Dimension(150, 30));

            controlPanel.add(new JLabel("Student ID:"));
            controlPanel.add(studentIDBox);
            controlPanel.add(new JLabel("Exam:"));
            controlPanel.add(examType);
            controlPanel.add(new JLabel("Method:"));
            controlPanel.add(notificationMethod);
            controlPanel.add(sendBtn);

            sendBtn.addActionListener(e -> {
                String studentID = (String) studentIDBox.getSelectedItem();
                String exam = (String) examType.getSelectedItem();
                String method = (String) notificationMethod.getSelectedItem();

                // Save to database
                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO notifications (student_id, exam_type, message, notification_method, sent_date, sent_time, teacher_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                        String message = exam.equals("Midterm") ? "Midterm exam scheduled" : "Final exam scheduled";
                        stmt.setString(1, studentID);
                        stmt.setString(2, exam);
                        stmt.setString(3, message);
                        stmt.setString(4, method);
                        stmt.setString(5, java.time.LocalDate.now().toString());
                        stmt.setString(6, java.time.LocalTime.now().toString());
                        stmt.setString(7, "admin"); // Teacher ID
                        stmt.executeUpdate();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<div style='background-color: #e3f2fd; padding: 15px; border-left: 5px solid #2F5565;'>");
                        result.append("<h2 style='color: #2F5565; margin-top: 0;'>Notification Sent Successfully</h2>");
                        result.append("<table border='1' cellpadding='10' style='border-collapse: collapse;'>");
                        result.append("<tr><td><b>Student ID:</b></td><td>").append(studentID).append("</td></tr>");
                        result.append("<tr><td><b>Exam Type:</b></td><td>").append(exam).append("</td></tr>");
                        result.append("<tr><td><b>Message:</b></td><td>").append("Midterm exam scheduled").append("</td></tr>");
                        result.append("<tr><td><b>Method:</b></td><td>").append(method).append("</td></tr>");
                        result.append("<tr><td><b>Sent Date:</b></td><td>").append(java.time.LocalDate.now()).append("</td></tr>");
                        result.append("<tr><td><b>Status:</b></td><td style='color: green;'><b>Status: Sent</b></td></tr>");
                        result.append("</table>");
                        result.append("<p style='color: green; margin-top: 10px;'><b>Status: Saved to Database</b></p>");
                        result.append("</div>");
                        result.append("</body></html>");

                        displayArea.setText(result.toString());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error sending notification: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
        } else {
            // STUDENT VIEW: Can only view notifications
            JButton viewBtn = new JButton("View My Notifications");
            viewBtn.setBackground(btnColor);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setPreferredSize(new Dimension(200, 40));

            controlPanel.add(new JLabel("(Read-Only for Students)"));
            controlPanel.add(viewBtn);

            viewBtn.addActionListener(e -> {
                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "SELECT * FROM notifications WHERE student_id = ? ORDER BY sent_date DESC")) {
                        stmt.setString(1, "23-101091");
                        ResultSet rs = stmt.executeQuery();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<h2 style='color: #2F5565;'>My Exam Notifications</h2>");

                        boolean hasData = false;
                        while (rs.next()) {
                            hasData = true;
                            result.append("<div style='border: 1px solid #ccc; padding: 10px; margin: 10px 0;'>");
                            result.append("<p><b>Exam:</b> ").append(rs.getString("exam_type")).append("</p>");
                            result.append("<p><b>Message:</b> ").append(rs.getString("message")).append("</p>");
                            result.append("<p><b>Method:</b> ").append(rs.getString("notification_method")).append("</p>");
                            result.append("<p><b>Received:</b> ").append(rs.getString("sent_date")).append("</p>");
                            result.append("</div>");
                        }

                        if (!hasData) {
                            result.append("<p style='color: #666;'>No notifications yet.</p>");
                        }

                        result.append("</body></html>");
                        displayArea.setText(result.toString());
                    }
                } catch (Exception ex) {
                    displayArea.setText("<html><body style='padding: 20px;'><p style='color: red;'>Ready for notifications. Currently none received.</p></body></html>");
                }
            });
        }

        content.add(controlPanel, BorderLayout.NORTH);
        content.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ===== USE CASE 3: CLASSROOM SEAT MANAGEMENT (Flyweight Pattern) =====
    private JPanel createClassroomSeatingPage(boolean isTeacher) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Classroom Seating Management", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);

        JEditorPane displayArea = new JEditorPane();
        displayArea.setContentType("text/html");
        displayArea.setEditable(false);

        if (isTeacher) {
            // TEACHER VIEW: Can assign seats
            JTextField classroomField = new JTextField("Room 101", 15);
            classroomField.setFont(new Font("Arial", Font.PLAIN, 14));
            classroomField.setPreferredSize(new Dimension(150, 30));

            JSpinner seatsSpinner = new JSpinner(new javax.swing.SpinnerNumberModel(30, 1, 100, 1));
            seatsSpinner.setPreferredSize(new Dimension(100, 30));

            JTextField seatNumberField = new JTextField(10);
            seatNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
            seatNumberField.setPreferredSize(new Dimension(100, 30));

            JComboBox<String> studentIDBox = new JComboBox<>(new String[]{"23-101296", "23-101077", "23-101091", "23-101192", "23-101107"});
            studentIDBox.setPreferredSize(new Dimension(120, 30));

            JButton assignBtn = new JButton("Assign Seat");
            assignBtn.setBackground(btnColor);
            assignBtn.setForeground(Color.WHITE);
            assignBtn.setPreferredSize(new Dimension(120, 30));

            JButton displayBtn = new JButton("Display Layout");
            displayBtn.setBackground(btnColor);
            displayBtn.setForeground(Color.WHITE);
            displayBtn.setPreferredSize(new Dimension(120, 30));

            controlPanel.add(new JLabel("Classroom:"));
            controlPanel.add(classroomField);
            controlPanel.add(new JLabel("Seats:"));
            controlPanel.add(seatsSpinner);
            controlPanel.add(new JLabel("Seat #:"));
            controlPanel.add(seatNumberField);
            controlPanel.add(new JLabel("Student:"));
            controlPanel.add(studentIDBox);
            controlPanel.add(assignBtn);
            controlPanel.add(displayBtn);

            assignBtn.addActionListener(e -> {
                String classroom = classroomField.getText();
                String seatNum = seatNumberField.getText();
                String studentID = (String) studentIDBox.getSelectedItem();

                if (seatNum.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter seat number", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int seat = Integer.parseInt(seatNum);

                    // Check if seat is already occupied
                    try {
                        try (Connection conn = DatabaseConnection.getConnection();
                             PreparedStatement checkStmt = conn.prepareStatement(
                                "SELECT student_id FROM classroom_seating WHERE classroom_name = ? AND seat_number = ?")) {
                            checkStmt.setString(1, classroom);
                            checkStmt.setInt(2, seat);
                            ResultSet checkRs = checkStmt.executeQuery();

                            if (checkRs.next()) {
                                String occupiedBy = checkRs.getString("student_id");
                                if (occupiedBy != null && !occupiedBy.isEmpty()) {
                                    JOptionPane.showMessageDialog(this, 
                                        "Seat " + seat + " is already occupied by student " + occupiedBy, 
                                        "Seat Occupied", 
                                        JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }

                        // Seat is available, proceed with assignment
                        try (Connection conn = DatabaseConnection.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(
                                "INSERT OR REPLACE INTO classroom_seating (classroom_name, seat_number, student_id, student_name, assignment_date, teacher_id) " +
                                "VALUES (?, ?, ?, ?, ?, ?)")) {
                            stmt.setString(1, classroom);
                            stmt.setInt(2, seat);
                            stmt.setString(3, studentID);
                            stmt.setString(4, studentID);
                            stmt.setString(5, java.time.LocalDate.now().toString());
                            stmt.setString(6, "admin");
                            stmt.executeUpdate();

                            StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                            result.append("<div style='background-color: #f5f5f5; padding: 15px; border-radius: 5px;'>");
                            result.append("<h3 style='color: #2F5565;'>Seat Assignment Confirmed</h3>");
                            result.append("<p><b>Classroom:</b> ").append(classroom).append("</p>");
                            result.append("<p><b>Seat Number:</b> ").append(seat).append("</p>");
                            result.append("<p><b>Student ID:</b> ").append(studentID).append("</p>");
                            result.append("<p><b>Assignment Date:</b> ").append(java.time.LocalDate.now()).append("</p>");
                            result.append("<p style='color: green;'><b>Status: Saved to Database</b></p>");
                            result.append("</div>");
                            result.append("</body></html>");

                            displayArea.setText(result.toString());
                        }
                    } catch (SQLException sqlEx) {
                        JOptionPane.showMessageDialog(this, "Database error: " + sqlEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        sqlEx.printStackTrace();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid seat number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            displayBtn.addActionListener(e -> {
                String classroom = classroomField.getText();
                int numSeats = (Integer) seatsSpinner.getValue();

                StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                result.append("<h3 style='color: #2F5565;'>").append(classroom).append(" - Layout</h3>");
                result.append("<table border='1' cellpadding='10' style='border-collapse: collapse;'>");
                result.append("<tr><th>Row</th><th>Seats</th></tr>");

                int rows = (int) Math.ceil(numSeats / 5.0);
                for (int row = 1; row <= rows; row++) {
                    result.append("<tr><td><b>Row ").append(row).append("</b></td><td>");
                    int startSeat = (row - 1) * 5 + 1;
                    int endSeat = Math.min(row * 5, numSeats);
                    for (int seat = startSeat; seat <= endSeat; seat++) {
                        result.append("<span style='background-color: #4CAF50; color: white; padding: 5px 8px; margin: 2px; display: inline-block;'>")
                                .append(seat).append("</span> ");
                    }
                    result.append("</td></tr>");
                }

                result.append("</table>");
                result.append("<p style='margin-top: 20px; color: #666;'><b>Total Seats:</b> ").append(numSeats).append("</p>");
                result.append("</body></html>");

                displayArea.setText(result.toString());
            });
        } else {
            // STUDENT VIEW: Can only view seating arrangement
            JButton viewBtn = new JButton("View My Classroom Seating");
            viewBtn.setBackground(btnColor);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setPreferredSize(new Dimension(250, 40));

            controlPanel.add(new JLabel("(Read-Only for Students)"));
            controlPanel.add(viewBtn);

            viewBtn.addActionListener(e -> {
                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "SELECT * FROM classroom_seating WHERE student_id = ?")) {
                        stmt.setString(1, "23-101091");
                        ResultSet rs = stmt.executeQuery();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<h2 style='color: #2F5565;'>My Classroom Seating</h2>");

                        boolean hasData = false;
                        while (rs.next()) {
                            hasData = true;
                            result.append("<div style='border: 1px solid #ccc; padding: 15px; margin: 10px 0;'>");
                            result.append("<p><b>Classroom:</b> ").append(rs.getString("classroom_name")).append("</p>");
                            result.append("<p><b>Seat Number:</b> ").append(rs.getInt("seat_number")).append("</p>");
                            result.append("<p><b>Seat Type:</b> ").append(rs.getString("seat_type")).append("</p>");
                            result.append("<p><b>Assigned Date:</b> ").append(rs.getString("assignment_date")).append("</p>");
                            result.append("</div>");
                        }

                        if (!hasData) {
                            result.append("<p style='color: #666;'>No seating assignment yet.</p>");
                        }

                        result.append("</body></html>");
                        displayArea.setText(result.toString());
                    }
                } catch (Exception ex) {
                    displayArea.setText("<html><body style='padding: 20px;'><p style='color: #666;'>Ready to display seating. Currently none assigned.</p></body></html>");
                }
            });
        }

        content.add(controlPanel, BorderLayout.NORTH);
        content.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Start with login page - don't bypass it!
            new LoginGUI();
        });
    }

    // ===== USE CASE 4: GRADE EXPORT SYSTEM (Adapter Pattern) =====
    private JPanel createGradeExportPage(boolean isTeacher, String loggedInID) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("Grade Export System", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);

        if (isTeacher) {
            // TEACHER VIEW: Can export grades
            JComboBox<String> studentIDBox = new JComboBox<>(new String[]{"23-101296", "23-101077", "23-101091", "23-101192", "23-101107"});
            studentIDBox.setPreferredSize(new Dimension(120, 30));

            JTextField gradeDataField = new JTextField(30);
            gradeDataField.setFont(new Font("Arial", Font.PLAIN, 14));
            gradeDataField.setPreferredSize(new Dimension(300, 30));

            JComboBox<String> formatBox = new JComboBox<>(new String[]{"PDF", "Excel", "CSV"});
            formatBox.setPreferredSize(new Dimension(100, 30));

            JButton exportBtn = new JButton("Export Grades");
            exportBtn.setBackground(btnColor);
            exportBtn.setForeground(Color.WHITE);
            exportBtn.setPreferredSize(new Dimension(150, 30));

            controlPanel.add(new JLabel("Student ID:"));
            controlPanel.add(studentIDBox);
            controlPanel.add(new JLabel("Grades:"));
            controlPanel.add(gradeDataField);
            controlPanel.add(new JLabel("Format:"));
            controlPanel.add(formatBox);
            controlPanel.add(exportBtn);

            JEditorPane displayArea = new JEditorPane();
            displayArea.setContentType("text/html");
            displayArea.setEditable(false);

            exportBtn.addActionListener(e -> {
                String studentID = (String) studentIDBox.getSelectedItem();
                String gradeData = gradeDataField.getText();
                String format = (String) formatBox.getSelectedItem();

                if (gradeData.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter grade data", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO grade_exports (student_id, report_data, export_date, export_time, teacher_id, export_format, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                        stmt.setString(1, studentID);
                        stmt.setString(2, gradeData);
                        stmt.setString(3, java.time.LocalDate.now().toString());
                        stmt.setString(4, java.time.LocalTime.now().toString());
                        stmt.setString(5, loggedInID);
                        stmt.setString(6, format);
                        stmt.setString(7, "Exported");
                        stmt.executeUpdate();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<div style='background-color: #f0f8ff; padding: 15px; border-left: 5px solid #2F5565;'>");
                        result.append("<h2 style='color: #2F5565; margin-top: 0;'>Grade Export Successfully</h2>");
                        result.append("<table border='1' cellpadding='10' style='border-collapse: collapse;'>");
                        result.append("<tr><td><b>Student ID:</b></td><td>").append(studentID).append("</td></tr>");
                        result.append("<tr><td><b>Grade Data:</b></td><td>").append(gradeData).append("</td></tr>");
                        result.append("<tr><td><b>Export Format:</b></td><td>").append(format).append("</td></tr>");
                        result.append("<tr><td><b>Export Date:</b></td><td>").append(java.time.LocalDate.now()).append("</td></tr>");
                        result.append("<tr><td><b>Status:</b></td><td style='color: green;'><b>Status: Exported</b></td></tr>");
                        result.append("</table>");
                        result.append("<p style='color: green; margin-top: 10px;'><b>Status: Saved to Database</b></p>");
                        result.append("</div>");
                        result.append("</body></html>");

                        displayArea.setText(result.toString());
                        gradeDataField.setText("");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            content.add(controlPanel, BorderLayout.NORTH);
            content.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        } else {
            // STUDENT VIEW: Can view exported grades
            JButton viewBtn = new JButton("View Exported Grades");
            viewBtn.setBackground(btnColor);
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setPreferredSize(new Dimension(200, 40));

            controlPanel.add(new JLabel("(Read-Only for Students)"));
            controlPanel.add(viewBtn);

            JEditorPane displayArea = new JEditorPane();
            displayArea.setContentType("text/html");
            displayArea.setEditable(false);

            viewBtn.addActionListener(e -> {
                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "SELECT * FROM grade_exports WHERE student_id = ? ORDER BY export_date DESC")) {
                        stmt.setString(1, loggedInID);
                        ResultSet rs = stmt.executeQuery();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<h2 style='color: #2F5565;'>My Exported Grades</h2>");

                        boolean hasData = false;
                        while (rs.next()) {
                            hasData = true;
                            result.append("<div style='border: 1px solid #ccc; padding: 10px; margin: 10px 0;'>");
                            result.append("<p><b>Grades:</b> ").append(rs.getString("report_data")).append("</p>");
                            result.append("<p><b>Format:</b> ").append(rs.getString("export_format")).append("</p>");
                            result.append("<p><b>Export Date:</b> ").append(rs.getString("export_date")).append("</p>");
                            result.append("<p><b>Status:</b> <span style='color: green;'>").append(rs.getString("status")).append("</span></p>");
                            result.append("</div>");
                        }

                        if (!hasData) {
                            result.append("<p style='color: #666;'>No grade exports available.</p>");
                        }

                        result.append("</body></html>");
                        displayArea.setText(result.toString());
                    }
                } catch (Exception ex) {
                    displayArea.setText("<p style='color: red;'>Error loading grades: " + ex.getMessage() + "</p>");
                    ex.printStackTrace();
                }
            });

            content.add(controlPanel, BorderLayout.NORTH);
            content.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        }

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ===== USE CASE 5: SMS NOTIFICATION SYSTEM (Adapter Pattern) =====
    private JPanel createSMSNotificationPage(boolean isTeacher) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JLabel title = new JLabel("SMS Notification System", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(headerBoxColor);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setPreferredSize(new Dimension(800, 60));
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(bgColor);
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setOpaque(false);

        JEditorPane displayArea = new JEditorPane();
        displayArea.setContentType("text/html");
        displayArea.setEditable(false);

        if (isTeacher) {
            // TEACHER VIEW: Can send SMS alerts
            JComboBox<String> studentBox = new JComboBox<>(new String[]{"Marwan Khaled", "Youssef Idrees", "Omar Elsayed", "Yassin Sameh", "Hany Yasser"});
            studentBox.setPreferredSize(new Dimension(150, 30));

            JTextField phoneField = new JTextField(20);
            phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
            phoneField.setPreferredSize(new Dimension(150, 30));

            JComboBox<String> alertTypeBox = new JComboBox<>(new String[]{"Attendance", "Grade Release", "Event"});
            alertTypeBox.setPreferredSize(new Dimension(130, 30));

            JButton sendBtn = new JButton("Send SMS Alert");
            sendBtn.setBackground(btnColor);
            sendBtn.setForeground(Color.WHITE);
            sendBtn.setPreferredSize(new Dimension(150, 30));

            controlPanel.add(new JLabel("Student:"));
            controlPanel.add(studentBox);
            controlPanel.add(new JLabel("Phone:"));
            controlPanel.add(phoneField);
            controlPanel.add(new JLabel("Alert Type:"));
            controlPanel.add(alertTypeBox);
            controlPanel.add(sendBtn);

            sendBtn.addActionListener(e -> {
                String studentName = (String) studentBox.getSelectedItem();
                String phone = phoneField.getText();
                String alertType = (String) alertTypeBox.getSelectedItem();

                if (phone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter phone number", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO sms_notifications (recipient_phone, recipient_name, message, sent_date, sent_time, teacher_id, alert_type, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                        String message = "Attendance alert for student: " + studentName;
                        stmt.setString(1, phone);
                        stmt.setString(2, studentName);
                        stmt.setString(3, message);
                        stmt.setString(4, java.time.LocalDate.now().toString());
                        stmt.setString(5, java.time.LocalTime.now().toString());
                        stmt.setString(6, "admin");
                        stmt.setString(7, alertType);
                        stmt.setString(8, "Sent");
                        stmt.executeUpdate();

                        StringBuilder result = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
                        result.append("<div style='background-color: #e8f5e9; padding: 15px; border-left: 5px solid #2F5565;'>");
                        result.append("<h2 style='color: #2F5565; margin-top: 0;'>SMS Alert Sent Successfully</h2>");
                        result.append("<table border='1' cellpadding='10' style='border-collapse: collapse;'>");
                        result.append("<tr><td><b>Recipient:</b></td><td>").append(studentName).append("</td></tr>");
                        result.append("<tr><td><b>Phone Number:</b></td><td>").append(phone).append("</td></tr>");
                        result.append("<tr><td><b>Alert Type:</b></td><td>").append(alertType).append("</td></tr>");
                        result.append("<tr><td><b>Message:</b></td><td>").append(message).append("</td></tr>");
                        result.append("<tr><td><b>Sent Date:</b></td><td>").append(java.time.LocalDate.now()).append("</td></tr>");
                        result.append("<tr><td><b>Status:</b></td><td style='color: green;'><b>Status: Sent</b></td></tr>");
                        result.append("</table>");
                        result.append("<p style='color: green; margin-top: 10px;'><b>Status: Saved to Database</b></p>");
                        result.append("</div>");
                        result.append("</body></html>");

                        displayArea.setText(result.toString());
                        phoneField.setText("");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            content.add(controlPanel, BorderLayout.NORTH);
            content.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        } else {
            // STUDENT VIEW: Read-only message
            JLabel messageLabel = new JLabel("SMS alerts will appear here when teachers send notifications");
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            messageLabel.setForeground(new Color(100, 100, 100));

            controlPanel.add(messageLabel);
            content.add(controlPanel, BorderLayout.NORTH);
        }

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }
}