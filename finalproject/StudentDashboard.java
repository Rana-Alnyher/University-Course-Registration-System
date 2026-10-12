package my.finalproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StudentDashboard extends JFrame {

    int studentId;

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    JPanel menuPanel = new JPanel(null);
    JPanel registerPanel = new JPanel(null);
    JPanel dropPanel = new JPanel(null);
    JPanel coursesPanel = new JPanel(new BorderLayout());
    JPanel allCoursesPanel = new JPanel(new BorderLayout());

    JTextField registerField = new JTextField();
    JButton registerConfirmBtn = new JButton("Register");
    JButton registerBackBtn = new JButton("Back");

    JTextField dropField = new JTextField();
    JButton dropConfirmBtn = new JButton("Drop");
    JButton dropBackBtn = new JButton("Back");

    JTable table = new JTable();
    JTable allCoursesTable = new JTable();

    public StudentDashboard(int studentId) {

        this.studentId = studentId;

        setTitle("Student Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initMenu();
        initRegister();
        initDrop();
        initCourses();
        initAllCourses();

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(dropPanel, "drop");
        mainPanel.add(coursesPanel, "courses");
        mainPanel.add(allCoursesPanel, "allCourses");

        add(mainPanel);

        cardLayout.show(mainPanel, "menu");

        setVisible(true);
    }

    //  MENU 
    void initMenu() {

    menuPanel.setLayout(new GridBagLayout());

    JButton registerBtn = new JButton("Register");
    JButton dropBtn = new JButton("Drop");
    JButton viewBtn = new JButton("My Courses");
    JButton allCoursesBtn = new JButton("All Courses");
    JButton logoutBtn = new JButton("Logout");

    JPanel btnPanel = new JPanel(new GridLayout(5, 1, 8, 8));
    btnPanel.setPreferredSize(new Dimension(180, 220));

    btnPanel.add(registerBtn);
    btnPanel.add(dropBtn);
    btnPanel.add(viewBtn);
    btnPanel.add(allCoursesBtn);
    btnPanel.add(logoutBtn);

    menuPanel.add(btnPanel);

    registerBtn.addActionListener(e ->
            cardLayout.show(mainPanel, "register")
    );

    dropBtn.addActionListener(e ->
            cardLayout.show(mainPanel, "drop")
    );

    viewBtn.addActionListener(e -> {
        loadCourses();
        cardLayout.show(mainPanel, "courses");
    });

    allCoursesBtn.addActionListener(e -> {
        loadAllCourses();
        cardLayout.show(mainPanel, "allCourses");
    });

    logoutBtn.addActionListener(e -> {
        new LoginFrame();
        dispose();
    });
}

    //  REGISTER 
    void initRegister() {

        JLabel label = new JLabel("Course ID:");
        label.setBounds(50, 50, 100, 30);

        registerField.setBounds(150, 50, 150, 30);

        registerConfirmBtn.setBounds(150, 100, 100, 30);
        registerBackBtn.setBounds(260, 100, 80, 30);

        registerPanel.add(label);
        registerPanel.add(registerField);
        registerPanel.add(registerConfirmBtn);
        registerPanel.add(registerBackBtn);

        registerConfirmBtn.addActionListener(e -> registerCourse());
        registerBackBtn.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
    }

    void registerCourse() {

        String input = registerField.getText().trim();
        if (input.isEmpty()) return;

        int courseId = Integer.parseInt(input);

        boolean ok = CourseService.registerCourse(studentId, courseId);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Registered Successfully");
            registerField.setText("");
        }
    }

    //  DROP 
    void initDrop() {JLabel label = new JLabel("Course ID:");
        label.setBounds(50, 50, 100, 30);

        dropField.setBounds(150, 50, 150, 30);

        dropConfirmBtn.setBounds(150, 100, 100, 30);
        dropBackBtn.setBounds(260, 100, 80, 30);

        dropPanel.add(label);
        dropPanel.add(dropField);
        dropPanel.add(dropConfirmBtn);
        dropPanel.add(dropBackBtn);

        dropConfirmBtn.addActionListener(e -> dropCourse());
        dropBackBtn.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
    }

    void dropCourse() {

        String input = dropField.getText().trim();
        if (input.isEmpty()) return;

        int courseId = Integer.parseInt(input);

        boolean ok = CourseService.dropCourse(studentId, courseId);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Dropped Successfully");
            dropField.setText("");
        }
    }

    //  MY COURSES 
    void initCourses() {

        JScrollPane scroll = new JScrollPane(table);
        JButton backBtn = new JButton("Back");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(backBtn);

        coursesPanel.add(top, BorderLayout.NORTH);
        coursesPanel.add(scroll, BorderLayout.CENTER);

        backBtn.addActionListener(e ->
                cardLayout.show(mainPanel, "menu")
        );
    }

    void loadCourses() {

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT c.course_id,c.course_code,c.course_name,c.schedule " +
                            "FROM courses c JOIN registrations r ON c.course_id=r.course_id " +
                            "WHERE r.student_id=?"
            );

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("ID");
            model.addColumn("Code");
            model.addColumn("Name");
            model.addColumn("Schedule");

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("schedule")
                });
            }

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    //  ALL COURSES 
    void initAllCourses() {

        JScrollPane scroll = new JScrollPane(allCoursesTable);
        JButton backBtn = new JButton("Back");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(backBtn);

        allCoursesPanel.add(top, BorderLayout.NORTH);
        allCoursesPanel.add(scroll, BorderLayout.CENTER);

        backBtn.addActionListener(e ->
                cardLayout.show(mainPanel, "menu")
        );
    }

    void loadAllCourses() {

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM courses"
            );

            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("ID");
            model.addColumn("Code");
            model.addColumn("Name");
            model.addColumn("Instructor");
            model.addColumn("Schedule");

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("instructor"),
                        rs.getString("schedule")
                });
            }

            allCoursesTable.setModel(model);

        } catch (Exception e) {e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }
}