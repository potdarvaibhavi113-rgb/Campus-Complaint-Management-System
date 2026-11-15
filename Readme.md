🛡️ Campus Complaint Management System

A desktop-based Campus Complaint Management System developed using Java, Java Swing, JDBC, and MySQL.

The application allows students to submit and track campus complaints, while administrators can manage complaints, update their status, and provide remarks.

📌 Features

👨‍🎓 Student Module

- Student registration and login.
- Submit campus complaints.
- Select complaint categories.
- Track personal complaints.
- View complaint status.
- View admin remarks.
- Search complaints by category.

🛡️ Admin Module

- Secure admin login (role-based access).
- View all student complaints.
- Search complaints by student name.
- View complaint descriptions and dates.
- Update complaint status.
- Add administrative remarks.

📷 Application Screenshots

🔐 1. Login & Student Registration

System Login| Student Registration
""System Login" (assets/login.png)" (assets/login.png)| ""Student Registration" (assets/register.png)" (assets/register.png)

🏠 2. Student Dashboard

""Student Dashboard" (assets/student-dashboard.png)" (assets/student-dashboard.png)

"🔗 View Student Dashboard Output" (assets/student-dashboard.png)

📊 3. Admin Dashboard

Track & Search Complaints

""Admin Dashboard - Track Complaints" (assets/admin-track-complaints.png)" (assets/admin-track-complaints.png)

"🔗 View Admin Track Complaints Output" (assets/admin-track-complaints.png)

Manage Ticket Status & Remarks

""Admin Dashboard - Manage Ticket Status" (assets/admin-manage-status.png)" (assets/admin-manage-status.png)

"🔗 View Admin Manage Status Output" (assets/admin-manage-status.png)

🛠️ Technologies Used

- Java
- Java Swing (GUI)
- JDBC
- MySQL
- MySQL Connector/J
- Eclipse IDE

📂 Complaint Categories

- Infrastructure
- IT & Network
- Hostel & Mess
- Library
- E-Waste / Devices
- Other

📋 Complaint Status

- Pending
- In Progress
- Resolved
- Rejected

🗄️ Database

Database Name: "complaint_db"

Tables:

- "users" – Stores student and admin login details.
- "complaints" – Stores complaint details, status, remarks, and user ID.

⚙️ How to Run

1. Clone or download this repository.
2. Open the project in Eclipse IDE.
3. Create a MySQL database named "complaint_db".
4. Configure the database connection in "DBConnection.java".
5. Import MySQL Connector/J into the project.
6. Create the required database tables.
7. Configure the database password using the "DB_PASSWORD" environment variable.
8. Run "LoginFrame.java".

🔑 Login System

The application redirects users based on their database role:

- Student → Student Dashboard
- Admin → Admin Dashboard

Student registration automatically assigns the Student role.

📁 Project Structure

ComplaintManagementSystem/
│
├── assets/
│   ├── login.png
│   ├── register.png
│   ├── student-dashboard.png
│   ├── admin-track-complaints.png
│   └── admin-manage-status.png
│
├── src/
│   └── com/
│       └── cms/
│           ├── DBConnection.java
│           ├── LoginFrame.java
│           ├── RegisterFrame.java
│           ├── StudentDashboardFrame.java
│           └── AdminDashboardFrame.java
│
├── .gitignore
└── README.md

💡 Future Enhancements

1. Password encryption using BCrypt.
2. Complaint status notifications.
3. Complaint priority management.
4. Image attachments for complaints.
5. Dashboard analytics and reports.

👩‍💻 Author

Developed as a Java-based academic project.

📜 License

This project is available for educational purposes.
