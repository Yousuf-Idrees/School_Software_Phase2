## Design Patterns Implementation Guide

This document outlines the five design patterns integrated into the school software project.

---

## Use Case 1: Manage User Accounts (Abstract Factory Pattern)

### Location
`src/com/school/accounts/`

### Files Created
- **UserAccountFactory.java** - Abstract factory interface and implementations

### Purpose
Creates families of related objects (profiles and permissions) for different user types (students and teachers) without specifying concrete classes.

### Usage Example
```java
UserAccountFactory studentFactory = new StudentAccountFactory();
UserProfile profile = studentFactory.createProfile();
UserPermissions permissions = studentFactory.createPermissions();
```

### Benefits
- Families of objects created together
- Easy to switch between user types
- Clean, organized structure
- Encapsulates object creation logic

---

## Use Case 2: Assignment Submission System (Bridge Pattern)

### Location
`src/com/school/assignments/`

### Files Created
- **Storage.java** - Implementor interface
- **LocalStorage.java** - Concrete implementor for local storage
- **CloudStorage.java** - Concrete implementor for cloud storage
- **Assignment.java** - Abstraction class
- **Homework.java** - Refined abstraction for homework
- **Project.java** - Refined abstraction for projects
- **AssignmentDemo.java** - Demo usage example

### Purpose
Decouples assignment types from storage mechanisms. Allows flexible combination of different assignment types (Homework, Project) with different storage implementations (Local, Cloud).

### Usage Example
```java
Storage localStorage = new LocalStorage();
Assignment homework = new Homework(localStorage);
homework.submit("math_homework.pdf");
```

### Benefits
- Easy to add new storage types without modifying existing assignment classes
- Easy to add new assignment types without modifying storage implementations
- Flexible runtime binding of abstractions and implementations

---

## Use Case 3: Exam Notification System (Bridge Pattern)

### Location
`src/com/school/notifications/`

### Files Created
- **NotificationSender.java** - Implementor interface
- **EmailSender.java** - Concrete implementor for email
- **SMSSender.java** - Concrete implementor for SMS
- **ExamNotification.java** - Abstraction class
- **MidtermNotification.java** - Refined abstraction for midterm exams
- **FinalNotification.java** - Refined abstraction for final exams
- **ExamNotificationDemo.java** - Demo usage example

### Purpose
Decouples exam notification types from notification delivery mechanisms. Allows sending different exam notifications (Midterm, Final) via different channels (Email, SMS).

### Usage Example
```java
NotificationSender emailSender = new EmailSender();
ExamNotification midterm = new MidtermNotification(emailSender);
midterm.notifyStudent();
```

### Benefits
- Easily add new notification types without changing senders
- Easily add new notification channels without changing notification types
- Switch notification channels at runtime
- Can support multiple notifications to multiple channels

### Potential Extensions
- Push notification sender
- Slack/Teams integration
- Quiz notifications, Practical exam notifications

---

## Use Case 4: View Grades (Singleton Pattern)

### Location
`src/com/school/services/`

### Files Created
- **GradeViewerService.java** - Singleton service for grade viewing

### Purpose
Ensure only one grade viewer service instance accesses the grade database at a time. This avoids conflicts and reduces redundant connections.

### Usage Example
```java
GradeViewerService.getInstance().viewGrades("23-101091");
```

### Benefits
- Only one DB reader needed
- Prevent multiple connections
- Improve performance
- Centralized control over grade viewing access

---

## Use Case 5: Classroom Seat Management (Flyweight Pattern)

### Location
`src/com/school/classroom/`

### Files Created
- **SeatType.java** - Flyweight class
- **SeatFactory.java** - Flyweight factory for creating/retrieving shared instances
- **Seat.java** - Individual seat with intrinsic and extrinsic state
- **Classroom.java** - Classroom containing multiple seats
- **ClassroomSeatDemo.java** - Demo usage example

### Purpose
Optimizes memory usage by sharing seat type objects across multiple seat instances. Reduces memory overhead when managing large numbers of seats.

### Intrinsic vs Extrinsic State
- **Intrinsic (Shared)**: Seat type (Standard, Premium, Accessible)
- **Extrinsic**: Seat number, student assignment

### Usage Example
```java
Classroom classroom = new Classroom("Room 101", 30);
classroom.assignStudentToSeat(1, "Alice Johnson");
classroom.displayClassroomLayout();
```

### Benefits
- Efficient memory usage: Multiple seats share the same type object
- Scalable to handle large numbers of seats/classrooms
- Easy to add new seat types dynamically

### How It Works
When creating multiple classrooms with 30 and 25 seats respectively (55 total seats):
- Only ONE "Standard" SeatType object is created and shared
- Each seat maintains its own extrinsic state (number, student)
- Result: Significant memory savings with 55+ seats

---

## Running the Demos

To run the demonstration classes:

### Assignment Submission Demo
```bash
javac src/com/school/assignments/*.java
java -cp src com.school.assignments.AssignmentDemo
```

### Exam Notification Demo
```bash
javac src/com/school/notifications/*.java
java -cp src com.school.notifications.ExamNotificationDemo
```

### Classroom Seat Demo
```bash
javac src/com/school/classroom/*.java
java -cp src com.school.classroom.ClassroomSeatDemo
```

---

## Integration with Existing Code

### Suggested Integration Points

1. **In StudentDAO/GradeController**
   - Use `AssignmentDemo` pattern to handle student submissions
   - Integrate with existing database to store submission records

2. **In NotificationSystem/AnnouncementCreator**
   - Extend current notification system with exam notifications
   - Use `ExamNotificationDemo` for sending exam reminders

3. **In SchoolSystemGUI/TeacherView**
   - Add classroom management UI using `ClassroomSeatDemo`
   - Display seat assignments and student seating arrangements

---

## Design Pattern Summary

### Abstract Factory Pattern (Use Case 1)
- **Problem**: Need to create families of related objects for different user types
- **Solution**: Abstract factory creates related objects together without specifying concrete classes
- **Benefit**: Easy to switch between user types and maintain clean structure

### Bridge Pattern (Use Cases 2 & 3)
- **Problem**: Class explosion from combining abstractions with implementations
- **Solution**: Separate abstraction from implementation via a bridge interface
- **Benefit**: Independent variations in both abstractions and implementations

### Singleton Pattern (Use Case 4)
- **Problem**: Multiple service instances may access the grades database simultaneously
- **Solution**: Use a single shared service instance for grade viewing
- **Benefit**: Prevents connection conflicts and reduces resource usage

### Flyweight Pattern (Use Case 5)
- **Problem**: Too many similar objects consuming excessive memory
- **Solution**: Share intrinsic state via factory, manage extrinsic state separately
- **Benefit**: Significant memory savings for large object collections

---

## Files Summary

```
src/com/school/
├── accounts/
│   └── UserAccountFactory.java
├── assignments/
│   ├── Storage.java
│   ├── LocalStorage.java
│   ├── CloudStorage.java
│   ├── Assignment.java
│   ├── Homework.java
│   ├── Project.java
│   └── AssignmentDemo.java
├── notifications/
│   ├── NotificationSender.java
│   ├── EmailSender.java
│   ├── SMSSender.java
│   ├── ExamNotification.java
│   ├── MidtermNotification.java
│   ├── FinalNotification.java
│   └── ExamNotificationDemo.java
├── services/
│   └── GradeViewerService.java
└── classroom/
    ├── SeatType.java
    ├── SeatFactory.java
    ├── Seat.java
    ├── Classroom.java
    └── ClassroomSeatDemo.java
```

