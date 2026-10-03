    -- =====================================================
    -- SCHOOL MANAGEMENT SYSTEM - COMPLETE DATABASE SCHEMA
    -- =====================================================
    -- This file contains all tables and sample data for the School Management System
    -- Includes 5 Design Patterns: Bridge (2 uses), Flyweight (1), Adapter (2)
    -- 
    -- To import: Open SQLite and run: .read database_schema.sql
    -- =====================================================

    -- =====================================================
    -- CORE TABLES
    -- =====================================================

    -- Users Table - Authentication and Role Management
    CREATE TABLE IF NOT EXISTS users (
        id TEXT PRIMARY KEY,
        username TEXT UNIQUE NOT NULL,
        password TEXT NOT NULL,
        role TEXT NOT NULL CHECK(role IN ('TEACHER', 'STUDENT')),
        name TEXT NOT NULL,
        created_date TEXT DEFAULT CURRENT_DATE,
        updated_date TEXT DEFAULT CURRENT_DATE
    );

    -- Grades Table - Student Performance Records
    CREATE TABLE IF NOT EXISTS grades (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        student_id TEXT NOT NULL,
        subject TEXT NOT NULL,
        grade TEXT NOT NULL,
        created_date TEXT DEFAULT CURRENT_DATE,
        FOREIGN KEY (student_id) REFERENCES users(username)
    );

    -- Attendance Table - Daily Attendance Tracking
    CREATE TABLE IF NOT EXISTS attendance (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        student_id TEXT NOT NULL,
        attendance_date TEXT NOT NULL,
        status TEXT NOT NULL CHECK(status IN ('Present', 'Absent', 'Late')),
        remarks TEXT,
        recorded_by TEXT,
        created_date TEXT DEFAULT CURRENT_DATE,
        FOREIGN KEY (student_id) REFERENCES users(username),
        FOREIGN KEY (recorded_by) REFERENCES users(username)
    );

    -- =====================================================
    -- DESIGN PATTERN 1: BRIDGE PATTERN - USE CASE 1
    -- Assignment Submission System
    -- Bridges: Assignment Type (Homework/Project) <-> Storage Method (Local/Cloud)
    -- =====================================================

    CREATE TABLE IF NOT EXISTS assignments (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        student_id TEXT NOT NULL,
        assignment_type TEXT NOT NULL CHECK(assignment_type IN ('Homework', 'Project')),
        file_name TEXT NOT NULL,
        storage_method TEXT NOT NULL CHECK(storage_method IN ('Local Storage', 'Cloud Storage')),
        file_path TEXT,
        submission_date TEXT NOT NULL,
        submission_time TEXT NOT NULL,
        teacher_id TEXT,
        status TEXT DEFAULT 'Submitted',
        grade TEXT,
        feedback TEXT,
        created_date TEXT DEFAULT CURRENT_DATE,
        FOREIGN KEY (student_id) REFERENCES users(username),
        FOREIGN KEY (teacher_id) REFERENCES users(username)
    );

    -- =====================================================
    -- DESIGN PATTERN 2: BRIDGE PATTERN - USE CASE 2
    -- Exam Notification System
    -- Bridges: Exam Type (Midterm/Final) <-> Notification Method (Email/SMS)
    -- =====================================================

    CREATE TABLE IF NOT EXISTS notifications (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        student_id TEXT NOT NULL,
        exam_type TEXT NOT NULL CHECK(exam_type IN ('Midterm', 'Final')),
        message TEXT NOT NULL,
        notification_method TEXT NOT NULL CHECK(notification_method IN ('Email', 'SMS')),
        sent_date TEXT NOT NULL,
        sent_time TEXT NOT NULL,
        teacher_id TEXT,
        status TEXT DEFAULT 'Sent',
        read_status TEXT DEFAULT 'Unread',
        created_date TEXT DEFAULT CURRENT_DATE,
        FOREIGN KEY (student_id) REFERENCES users(username),
        FOREIGN KEY (teacher_id) REFERENCES users(username)
    );

    -- =====================================================
    -- DESIGN PATTERN 3: FLYWEIGHT PATTERN - USE CASE 3
    -- Classroom Seat Management
    -- Flyweight: SeatType objects cached and shared (Standard, Premium, etc.)
    -- =====================================================

    CREATE TABLE IF NOT EXISTS seat_types (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        seat_type TEXT UNIQUE NOT NULL,
        capacity INTEGER DEFAULT 1,
        amenities TEXT,
        created_date TEXT DEFAULT CURRENT_DATE
    );

    CREATE TABLE IF NOT EXISTS classroom_seating (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        classroom_name TEXT NOT NULL,
        seat_number INTEGER NOT NULL,
        student_id TEXT,
        student_name TEXT,
        assignment_date TEXT,
        teacher_id TEXT,
        seat_type TEXT DEFAULT 'Standard',
        floor_number INTEGER,
        is_occupied INTEGER DEFAULT 0,
        created_date TEXT DEFAULT CURRENT_DATE,
        updated_date TEXT DEFAULT CURRENT_DATE,
        UNIQUE(classroom_name, seat_number),
        FOREIGN KEY (student_id) REFERENCES users(username),
        FOREIGN KEY (teacher_id) REFERENCES users(username),
        FOREIGN KEY (seat_type) REFERENCES seat_types(seat_type)
    );

    -- =====================================================
    -- DESIGN PATTERN 4: ADAPTER PATTERN - USE CASE 4
    -- Grade Export System
    -- Adapts: Legacy Report Printer Interface <-> Modern Grade Exporter Interface
    -- =====================================================

    CREATE TABLE IF NOT EXISTS grade_exports (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        student_id TEXT NOT NULL,
        report_data TEXT NOT NULL,
        export_date TEXT NOT NULL,
        export_time TEXT NOT NULL,
        teacher_id TEXT,
        export_format TEXT NOT NULL CHECK(export_format IN ('PDF', 'Excel', 'CSV')),
        file_path TEXT,
        status TEXT DEFAULT 'Exported',
        download_count INTEGER DEFAULT 0,
        created_date TEXT DEFAULT CURRENT_DATE,
        FOREIGN KEY (student_id) REFERENCES users(username),
        FOREIGN KEY (teacher_id) REFERENCES users(username)
    );

    -- =====================================================
    -- DESIGN PATTERN 5: ADAPTER PATTERN - USE CASE 5
    -- SMS Notification System
    -- Adapts: Third-Party SMS Gateway <-> Internal Notification Sender Interface
    -- =====================================================

    CREATE TABLE IF NOT EXISTS sms_notifications (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        recipient_phone TEXT NOT NULL,
        recipient_name TEXT NOT NULL,
        message TEXT NOT NULL,
        sent_date TEXT NOT NULL,
        sent_time TEXT NOT NULL,
        teacher_id TEXT,
        alert_type TEXT NOT NULL CHECK(alert_type IN ('Attendance', 'Grade Release', 'Event')),
        status TEXT DEFAULT 'Sent',
        delivery_status TEXT DEFAULT 'Pending',
        created_date TEXT DEFAULT CURRENT_DATE,
        FOREIGN KEY (teacher_id) REFERENCES users(username)
    );

    -- =====================================================
    -- REPORTS TABLE - For storing generated reports
    -- =====================================================

    CREATE TABLE IF NOT EXISTS reports (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        report_type TEXT NOT NULL CHECK(report_type IN ('Attendance Report', 'Grade Report', 'Performance Report', 'Comprehensive Report')),
        title TEXT NOT NULL,
        description TEXT,
        generated_by TEXT NOT NULL,
        generated_date TEXT NOT NULL,
        generated_time TEXT NOT NULL,
        file_format TEXT DEFAULT 'PDF',
        file_path TEXT,
        content TEXT,
        is_archive INTEGER DEFAULT 0,
        created_date TEXT DEFAULT CURRENT_DATE,
        FOREIGN KEY (generated_by) REFERENCES users(username)
    );

    -- =====================================================
    -- AUDIT LOG TABLE - Track all system activities
    -- =====================================================

    CREATE TABLE IF NOT EXISTS audit_logs (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        user_id TEXT NOT NULL,
        action TEXT NOT NULL,
        table_name TEXT,
        record_id INTEGER,
        changes TEXT,
        timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(username)
    );

    -- =====================================================
    -- SAMPLE DATA - USERS
    -- =====================================================

    INSERT OR IGNORE INTO users (id, username, password, role, name) VALUES 
    ('T001', 'admin', 'teacher123', 'TEACHER', 'Principal Admin'),
    ('S001', '23-101296', 'student123', 'STUDENT', 'Marwan Khaled'),
    ('S002', '23-101077', 'student123', 'STUDENT', 'Youssef Idrees'),
    ('S003', '23-101091', 'student123', 'STUDENT', 'Omar Elsayed'),
    ('S004', '23-101192', 'student123', 'STUDENT', 'Yassin Sameh'),
    ('S005', '23-101107', 'student123', 'STUDENT', 'Hany Yasser');

    -- =====================================================
    -- SAMPLE DATA - SEAT TYPES (Flyweight Pool)
    -- =====================================================

    INSERT OR IGNORE INTO seat_types (seat_type, capacity, amenities) VALUES 
    ('Standard', 1, 'Basic desk and chair'),
    ('Premium', 1, 'Desk with computer access'),
    ('Accessible', 1, 'Wheelchair accessible, wider space');

    -- =====================================================
    -- SAMPLE DATA - GRADES
    -- =====================================================

    INSERT OR IGNORE INTO grades (student_id, subject, grade, semester) VALUES 
    ('23-101296', 'Software Engineering', 'A', 'Fall 2025'),
    ('23-101296', 'Database Systems', 'B+', 'Fall 2025'),
    ('23-101296', 'Mathematics', 'A-', 'Fall 2025'),
    ('23-101077', 'Software Engineering', 'B+', 'Fall 2025'),
    ('23-101077', 'Data Structures', 'A', 'Fall 2025'),
    ('23-101077', 'Math', 'B', 'Fall 2025'),
    ('23-101091', 'Software Engineering', 'B', 'Fall 2025'),
    ('23-101091', 'Database', 'A-', 'Fall 2025'),
    ('23-101091', 'Technical Writing', 'A', 'Fall 2025'),
    ('23-101192', 'Software Engineering', 'A', 'Fall 2025'),
    ('23-101192', 'Data Analysis', 'B', 'Fall 2025'),
    ('23-101192', 'Critical Thinking', 'A-', 'Fall 2025'),
    ('23-101107', 'Software Engineering', 'A-', 'Fall 2025'),
    ('23-101107', 'Algorithm And Design', 'B+', 'Fall 2025'),
    ('23-101107', 'Physics', 'A', 'Fall 2025');

    -- =====================================================
    -- SAMPLE DATA - ATTENDANCE
    -- =====================================================

    INSERT OR IGNORE INTO attendance (student_id, attendance_date, status, recorded_by) VALUES 
    ('23-101296', '2025-12-01', 'Present', 'admin'),
    ('23-101296', '2025-12-05', 'Present', 'admin'),
    ('23-101296', '2025-12-10', 'Present', 'admin'),
    ('23-101077', '2025-12-01', 'Present', 'admin'),
    ('23-101077', '2025-12-05', 'Absent', 'admin'),
    ('23-101077', '2025-12-10', 'Present', 'admin'),
    ('23-101091', '2025-12-01', 'Absent', 'admin'),
    ('23-101091', '2025-12-05', 'Present', 'admin'),
    ('23-101091', '2025-12-10', 'Present', 'admin'),
    ('23-101192', '2025-12-01', 'Present', 'admin'),
    ('23-101192', '2025-12-05', 'Present', 'admin'),
    ('23-101192', '2025-12-10', 'Present', 'admin'),
    ('23-101107', '2025-12-01', 'Present', 'admin'),
    ('23-101107', '2025-12-05', 'Present', 'admin'),
    ('23-101107', '2025-12-10', 'Absent', 'admin');

    -- =====================================================
    -- SAMPLE DATA - ASSIGNMENTS (Bridge Pattern)
    -- =====================================================

    INSERT OR IGNORE INTO assignments (student_id, assignment_type, file_name, storage_method, submission_date, submission_time, teacher_id, status, grade) VALUES 
    ('23-101296', 'Homework', 'assignment1.pdf', 'Local Storage', '2025-12-08', '14:30:00', 'admin', 'Submitted', 'A-'),
    ('23-101077', 'Project', 'project1.zip', 'Cloud Storage', '2025-12-09', '10:15:00', 'admin', 'Submitted', 'B+'),
    ('23-101091', 'Homework', 'hw2.docx', 'Local Storage', '2025-12-07', '16:45:00', 'admin', 'Submitted', 'B'),
    ('23-101192', 'Project', 'final_project.zip', 'Cloud Storage', '2025-12-06', '09:20:00', 'admin', 'Submitted', 'A'),
    ('23-101107', 'Homework', 'assignment3.pdf', 'Local Storage', '2025-12-10', '13:15:00', 'admin', 'Submitted', 'A-');

    -- =====================================================
    -- SAMPLE DATA - EXAM NOTIFICATIONS (Bridge Pattern)
    -- =====================================================

    INSERT OR IGNORE INTO notifications (student_id, exam_type, message, notification_method, sent_date, sent_time, teacher_id, status) VALUES 
    ('23-101296', 'Midterm', 'Midterm exam scheduled', 'Email', '2025-11-20', '09:00:00', 'admin', 'Sent'),
    ('23-101077', 'Final', 'Final exam scheduled', 'SMS', '2025-11-25', '10:30:00', 'admin', 'Sent'),
    ('23-101091', 'Midterm', 'Midterm exam scheduled', 'Email', '2025-11-20', '09:00:00', 'admin', 'Sent'),
    ('23-101192', 'Final', 'Final exam scheduled', 'SMS', '2025-11-25', '10:30:00', 'admin', 'Sent'),
    ('23-101107', 'Midterm', 'Midterm exam scheduled', 'Email', '2025-11-20', '09:00:00', 'admin', 'Sent');

    -- =====================================================
    -- SAMPLE DATA - CLASSROOM SEATING (Flyweight Pattern)
    -- =====================================================

    INSERT OR IGNORE INTO classroom_seating (classroom_name, seat_number, student_id, student_name, assignment_date, teacher_id, seat_type, floor_number, is_occupied) VALUES 
    ('Room 101', 1, '23-101296', '23-101296', '2025-12-01', 'admin', 'Standard', 1, 1),
    ('Room 101', 2, '23-101077', '23-101077', '2025-12-01', 'admin', 'Standard', 1, 1),
    ('Room 101', 3, '23-101091', '23-101091', '2025-12-01', 'admin', 'Standard', 1, 1),
    ('Room 101', 4, NULL, NULL, NULL, NULL, 'Standard', 1, 0),
    ('Room 101', 5, NULL, NULL, NULL, NULL, 'Standard', 1, 0),
    ('Room 102', 1, '23-101192', '23-101192', '2025-12-01', 'admin', 'Premium', 2, 1),
    ('Room 102', 2, '23-101107', '23-101107', '2025-12-01', 'admin', 'Premium', 2, 1),
    ('Room 102', 3, NULL, NULL, NULL, NULL, 'Premium', 2, 0),
    ('Room 102', 4, NULL, NULL, NULL, NULL, 'Accessible', 2, 0);

    -- =====================================================
    -- SAMPLE DATA - GRADE EXPORTS (Adapter Pattern)
    -- =====================================================

    INSERT OR IGNORE INTO grade_exports (student_id, report_data, export_date, export_time, teacher_id, export_format, status) VALUES 
    ('23-101296', 'Math: A+, English: A, Science: B+', '2025-12-05', '13:20:00', 'admin', 'PDF', 'Exported'),
    ('23-101077', 'Math: B, English: A, Science: A-', '2025-12-06', '14:15:00', 'admin', 'Excel', 'Exported'),
    ('23-101091', 'Math: A, English: B+, Science: A', '2025-12-07', '11:45:00', 'admin', 'CSV', 'Exported'),
    ('23-101192', 'Math: A+, English: A+, Science: A', '2025-12-08', '15:30:00', 'admin', 'PDF', 'Exported'),
    ('23-101107', 'Math: A-, English: A, Science: A+', '2025-12-09', '10:00:00', 'admin', 'Excel', 'Exported');

    -- =====================================================
    -- SAMPLE DATA - SMS NOTIFICATIONS (Adapter Pattern)
    -- =====================================================

    INSERT OR IGNORE INTO sms_notifications (recipient_phone, recipient_name, message, sent_date, sent_time, teacher_id, alert_type, status) VALUES 
    ('+201001234567', 'Ahmed Kamal', 'Attendance alert for student: Ahmed Kamal', '2025-12-08', '15:30:00', 'admin', 'Attendance', 'Sent'),
    ('+201001234568', 'Fatima Hassan', 'Attendance alert for student: Fatima Hassan', '2025-12-08', '15:35:00', 'admin', 'Attendance', 'Sent'),
    ('+201001234569', 'Mohamed Ali', 'Attendance alert for student: Mohamed Ali', '2025-12-08', '15:40:00', 'admin', 'Attendance', 'Sent'),
    ('+201001234570', 'Marwan Khaled', 'Grade Release - Your grades are ready for review', '2025-12-09', '09:00:00', 'admin', 'Grade Release', 'Sent'),
    ('+201001234571', 'Youssef Idrees', 'Event Notification - School Assembly on Friday at 10 AM', '2025-12-10', '08:00:00', 'admin', 'Event', 'Sent');

    -- =====================================================
    -- SAMPLE DATA - REPORTS
    -- =====================================================

    INSERT OR IGNORE INTO reports (report_type, title, description, generated_by, generated_date, generated_time, file_format, content) VALUES 
    ('Attendance Report', 'December Attendance Summary', 'Monthly attendance report for December 2025', 'admin', '2025-12-10', '16:00:00', 'PDF', 'Attendance statistics for all students'),
    ('Grade Report', 'Semester Grades - Fall 2025', 'Complete grade report for Fall 2025 semester', 'admin', '2025-12-08', '14:30:00', 'PDF', 'Final grades for all courses'),
    ('Performance Report', 'Top Performers - Fall 2025', 'Report highlighting top performing students', 'admin', '2025-12-09', '11:20:00', 'Excel', 'Performance metrics and rankings'),
    ('Comprehensive Report', 'Complete School Report - December 2025', 'Comprehensive report including all metrics', 'admin', '2025-12-10', '17:00:00', 'PDF', 'Overall school performance and statistics');

    -- =====================================================
    -- VERIFY DATA INSERTION
    -- =====================================================

    -- Count tables
    SELECT 'Users' as table_name, COUNT(*) as record_count FROM users
    UNION ALL
    SELECT 'Grades', COUNT(*) FROM grades
    UNION ALL
    SELECT 'Attendance', COUNT(*) FROM attendance
    UNION ALL
    SELECT 'Assignments', COUNT(*) FROM assignments
    UNION ALL
    SELECT 'Notifications', COUNT(*) FROM notifications
    UNION ALL
    SELECT 'Classroom Seating', COUNT(*) FROM classroom_seating
    UNION ALL
    SELECT 'Grade Exports', COUNT(*) FROM grade_exports
    UNION ALL
    SELECT 'SMS Notifications', COUNT(*) FROM sms_notifications
    UNION ALL
    SELECT 'Reports', COUNT(*) FROM reports;
    =
    -- SAMPLE DATA FOR TESTING
    -- ====================================================

    -- Sample assignment submission
    INSERT INTO assignments (student_id, assignment_type, file_name, storage_method, submission_date, submission_time, teacher_id, status)
    VALUES ('23-101091', 'Homework', 'math_homework.pdf', 'Cloud Storage', '2026-03-27', '14:30:00', 'admin', 'Submitted');

    -- Sample notification sent
    INSERT INTO notifications (student_id, exam_type, message, notification_method, sent_date, sent_time, teacher_id, status)
    VALUES ('23-101091', 'Midterm', 'Midterm exam scheduled', 'Email', '2026-03-27', '10:00:00', 'admin', 'Sent');

    -- Sample classroom seating
    INSERT INTO classroom_seating (classroom_name, seat_number, student_id, student_name, assignment_date, teacher_id, seat_type)
    VALUES ('Room 101', 5, '23-101091', '23-101091', '2026-03-27', 'admin', 'Standard');
