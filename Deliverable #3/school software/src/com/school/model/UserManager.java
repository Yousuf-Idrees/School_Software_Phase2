package com.school.model;

/**
 * User Account Management
 *
 * Manages creation of user accounts for Students and Teachers.
 * Handles profile setup and permission assignment.
 */

// 1. Interfaces
interface UserProfile { void setupProfile(); }
interface UserPermissions { void assignPermissions(); }

public interface UserManager {
    UserProfile createProfile();
    UserPermissions createPermissions();
}

// 2. Student Implementation
class StudentProfile implements UserProfile { 
    public void setupProfile() { System.out.println("Student Profile Set."); }
}
class StudentPermissions implements UserPermissions { 
    public void assignPermissions() { System.out.println("Student Permissions: Restricted."); }
}

class StudentAccountManager implements UserManager {
    public UserProfile createProfile() { return new StudentProfile(); }
    public UserPermissions createPermissions() { return new StudentPermissions(); }
}

// 3. Teacher Implementation
class TeacherProfile implements UserProfile { 
    public void setupProfile() { System.out.println("Teacher Profile Set."); }
}
class TeacherPermissions implements UserPermissions { 
    public void assignPermissions() { System.out.println("Teacher Permissions: Full Access."); }
}

class TeacherAccountManager implements UserManager {
    public UserProfile createProfile() { return new TeacherProfile(); }
    public UserPermissions createPermissions() { return new TeacherPermissions(); }
}