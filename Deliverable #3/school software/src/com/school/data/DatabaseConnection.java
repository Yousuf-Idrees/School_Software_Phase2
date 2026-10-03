package com.school.data;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:school.db";
    
    // Get database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    // Initialize database and create tables
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id TEXT PRIMARY KEY," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL," +
                    "name TEXT NOT NULL" +
                    ");";
            
            // Create grades table
            String createGradesTable = "CREATE TABLE IF NOT EXISTS grades (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id TEXT NOT NULL," +
                    "subject TEXT NOT NULL," +
                    "grade TEXT NOT NULL," +
                    "FOREIGN KEY (student_id) REFERENCES users(username)" +
                    ");";
            
            // Create attendance table
            String createAttendanceTable = "CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id TEXT NOT NULL," +
                    "attendance_date TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "FOREIGN KEY (student_id) REFERENCES users(username)" +
                    ");";
            
            // Create assignments table (Bridge Pattern - Use Case 1)
            String createAssignmentsTable = "CREATE TABLE IF NOT EXISTS assignments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id TEXT NOT NULL," +
                    "assignment_type TEXT NOT NULL," +
                    "file_name TEXT NOT NULL," +
                    "storage_method TEXT NOT NULL," +
                    "submission_date TEXT NOT NULL," +
                    "submission_time TEXT NOT NULL," +
                    "teacher_id TEXT," +
                    "status TEXT DEFAULT 'Submitted'," +
                    "FOREIGN KEY (student_id) REFERENCES users(username)," +
                    "FOREIGN KEY (teacher_id) REFERENCES users(username)" +
                    ");";
            
            // Create notifications table (Bridge Pattern - Use Case 2)
            String createNotificationsTable = "CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id TEXT NOT NULL," +
                    "exam_type TEXT NOT NULL," +
                    "message TEXT NOT NULL," +
                    "notification_method TEXT NOT NULL," +
                    "sent_date TEXT NOT NULL," +
                    "sent_time TEXT NOT NULL," +
                    "teacher_id TEXT," +
                    "status TEXT DEFAULT 'Sent'," +
                    "FOREIGN KEY (student_id) REFERENCES users(username)," +
                    "FOREIGN KEY (teacher_id) REFERENCES users(username)" +
                    ");";
            
            // Create classroom_seating table (Flyweight Pattern - Use Case 3)
            String createSeatingTable = "CREATE TABLE IF NOT EXISTS classroom_seating (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "classroom_name TEXT NOT NULL," +
                    "seat_number INTEGER NOT NULL," +
                    "student_id TEXT," +
                    "student_name TEXT," +
                    "assignment_date TEXT," +
                    "teacher_id TEXT," +
                    "seat_type TEXT DEFAULT 'Standard'," +
                    "UNIQUE(classroom_name, seat_number)," +
                    "FOREIGN KEY (student_id) REFERENCES users(username)," +
                    "FOREIGN KEY (teacher_id) REFERENCES users(username)" +
                    ");";
            
            // Create grade_exports table (Adapter Pattern - Use Case 4)
            String createGradeExportsTable = "CREATE TABLE IF NOT EXISTS grade_exports (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id TEXT NOT NULL," +
                    "report_data TEXT NOT NULL," +
                    "export_date TEXT NOT NULL," +
                    "export_time TEXT NOT NULL," +
                    "teacher_id TEXT," +
                    "export_format TEXT DEFAULT 'PDF'," +
                    "status TEXT DEFAULT 'Exported'," +
                    "FOREIGN KEY (student_id) REFERENCES users(username)," +
                    "FOREIGN KEY (teacher_id) REFERENCES users(username)" +
                    ");";
            
            // Create sms_notifications table (Adapter Pattern - Use Case 5)
            String createSMSNotificationsTable = "CREATE TABLE IF NOT EXISTS sms_notifications (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "recipient_phone TEXT NOT NULL," +
                    "recipient_name TEXT NOT NULL," +
                    "message TEXT NOT NULL," +
                    "sent_date TEXT NOT NULL," +
                    "sent_time TEXT NOT NULL," +
                    "teacher_id TEXT," +
                    "alert_type TEXT DEFAULT 'Attendance'," +
                    "status TEXT DEFAULT 'Sent'," +
                    "FOREIGN KEY (teacher_id) REFERENCES users(username)" +
                    ");";
            
            // Create reports table (DocumentFactory Integration)
            String createReportsTable = "CREATE TABLE IF NOT EXISTS reports (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "report_type TEXT NOT NULL CHECK(report_type IN ('Attendance Report', 'Grade Report', 'Performance Report', 'Comprehensive Report'))," +
                    "title TEXT NOT NULL," +
                    "description TEXT," +
                    "generated_by TEXT NOT NULL," +
                    "generated_date TEXT NOT NULL," +
                    "generated_time TEXT NOT NULL," +
                    "file_format TEXT DEFAULT 'PDF'," +
                    "file_path TEXT," +
                    "content TEXT," +
                    "is_archive INTEGER DEFAULT 0," +
                    "created_date TEXT DEFAULT CURRENT_DATE," +
                    "FOREIGN KEY (generated_by) REFERENCES users(username)" +
                    ");";
            
            stmt.execute(createUsersTable);
            stmt.execute(createGradesTable);
            stmt.execute(createAttendanceTable);
            stmt.execute(createAssignmentsTable);
            stmt.execute(createNotificationsTable);
            stmt.execute(createSeatingTable);
            stmt.execute(createGradeExportsTable);
            stmt.execute(createSMSNotificationsTable);
            stmt.execute(createReportsTable);
            
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
    
    // Populate initial data
    public static void populateInitialData() {
        try (Connection conn = getConnection()) {
            // Check if data already exists
            String countQuery = "SELECT COUNT(*) FROM users;";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countQuery)) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Database already populated.");
                    return;
                }
            }
            
            // Insert users
            String insertUsers = "INSERT INTO users (id, username, password, role, name) VALUES (?, ?, ?, ?, ?);";
            try (PreparedStatement pstmt = conn.prepareStatement(insertUsers)) {
                // Teacher
                pstmt.setString(1, "T001");
                pstmt.setString(2, "admin");
                pstmt.setString(3, "teacher123");
                pstmt.setString(4, "TEACHER");
                pstmt.setString(5, "Principal Admin");
                pstmt.executeUpdate();
                
                // Students
                String[][] students = {
                    {"S001", "23-101296", "student123", "STUDENT", "Marwan Khaled"},
                    {"S002", "23-101077", "student123", "STUDENT", "Youssef Idrees"},
                    {"S003", "23-101091", "student123", "STUDENT", "Omar Elsayed"},
                    {"S004", "23-101192", "student123", "STUDENT", "Yassin Sameh"},
                    {"S005", "23-101107", "student123", "STUDENT", "Hany Yasser"}
                };
                
                for (String[] student : students) {
                    pstmt.setString(1, student[0]);
                    pstmt.setString(2, student[1]);
                    pstmt.setString(3, student[2]);
                    pstmt.setString(4, student[3]);
                    pstmt.setString(5, student[4]);
                    pstmt.executeUpdate();
                }
            }
            
            // Insert grades
            String insertGrades = "INSERT INTO grades (student_id, subject, grade) VALUES (?, ?, ?);";
            try (PreparedStatement pstmt = conn.prepareStatement(insertGrades)) {
                String[][] gradesData = {
                    {"23-101296", "Software Engineering", "A"},
                    {"23-101296", "Database Systems", "B+"},
                    {"23-101296", "Mathematics", "A-"},
                    {"23-101077", "Software Engineering", "B+"},
                    {"23-101077", "Data Structures", "A"},
                    {"23-101077", "Math", "B"},
                    {"23-101091", "Software Engineering", "B"},
                    {"23-101091", "Database", "A-"},
                    {"23-101091", "Technical Writing", "A"},
                    {"23-101192", "Software Engineering", "A"},
                    {"23-101192", "Data Analysis", "B"},
                    {"23-101192", "Critical Thinking", "A-"},
                    {"23-101107", "Software Engineering", "A-"},
                    {"23-101107", "Algorithm And Design", "B+"},
                    {"23-101107", "Physics", "A"}
                };
                
                for (String[] grade : gradesData) {
                    pstmt.setString(1, grade[0]);
                    pstmt.setString(2, grade[1]);
                    pstmt.setString(3, grade[2]);
                    pstmt.executeUpdate();
                }
            }
            
            // Insert attendance
            String insertAttendance = "INSERT INTO attendance (student_id, attendance_date, status) VALUES (?, ?, ?);";
            try (PreparedStatement pstmt = conn.prepareStatement(insertAttendance)) {
                String[][] attendanceData = {
                    {"23-101296", "2025-12-01", "Present"},
                    {"23-101296", "2025-12-05", "Present"},
                    {"23-101296", "2025-12-10", "Present"},
                    {"23-101077", "2025-12-01", "Present"},
                    {"23-101077", "2025-12-05", "Absent"},
                    {"23-101077", "2025-12-10", "Present"},
                    {"23-101091", "2025-12-01", "Absent"},
                    {"23-101091", "2025-12-05", "Present"},
                    {"23-101091", "2025-12-10", "Present"},
                    {"23-101192", "2025-12-01", "Present"},
                    {"23-101192", "2025-12-05", "Present"},
                    {"23-101192", "2025-12-10", "Present"},
                    {"23-101107", "2025-12-01", "Present"},
                    {"23-101107", "2025-12-05", "Present"},
                    {"23-101107", "2025-12-10", "Absent"}
                };
                
                for (String[] attendance : attendanceData) {
                    pstmt.setString(1, attendance[0]);
                    pstmt.setString(2, attendance[1]);
                    pstmt.setString(3, attendance[2]);
                    pstmt.executeUpdate();
                }
            }
            
            System.out.println("Initial data populated successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error populating database: " + e.getMessage());
        }
    }
    
    // Verify database was populated correctly
    public static void verifyDatabaseData() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("\n[DATABASE VERIFICATION]");
            
            // Check users
            ResultSet rs = stmt.executeQuery("SELECT username, password, role FROM users;");
            System.out.println("Users in database:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("username") + " | " + rs.getString("password") + " | " + rs.getString("role"));
            }
            
        } catch (SQLException e) {
            System.out.println("Error verifying database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
