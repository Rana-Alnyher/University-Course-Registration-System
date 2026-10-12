package my.finalproject;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    String selectedRole = "";

    JTextField usernameField;
    JPasswordField passwordField;

    public LoginFrame() {

        setTitle("University System");
        setSize(400,300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Choose Login Type");

        title.setFont(new Font("Arial",Font.BOLD,18));

        title.setBounds(100,20,250,30);

        add(title);

        JButton studentBtn = new JButton("Student");
        JButton adminBtn = new JButton("Admin");

        studentBtn.setBounds(100,80,180,40);
        adminBtn.setBounds(100,140,180,40);

        add(studentBtn);
        add(adminBtn);

        //  ROLE 

        studentBtn.addActionListener(e -> {
            selectedRole = "STUDENT";
            openLogin();
        });

        adminBtn.addActionListener(e -> {
            selectedRole = "ADMIN";
            openLogin();
        });

        setVisible(true);
    }

    //  LOGIN UI 

    void openLogin() {

        getContentPane().removeAll();

        JLabel u = new JLabel("Username");
        u.setBounds(50,50,100,30);

        JLabel p = new JLabel("Password");
        p.setBounds(50,100,100,30);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        usernameField.setBounds(150,50,150,30);
        passwordField.setBounds(150,100,150,30);

        JButton login = new JButton("Login");
        login.setBounds(130,170,100,30);

        add(u);
        add(p);
        add(usernameField);
        add(passwordField);
        add(login);

        login.addActionListener(e -> login());

        revalidate();
        repaint();
    }

    //  LOGIN LOGIC 

    void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (selectedRole == null || selectedRole.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select Student or Admin first",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        UserDAO dao = new UserDAO();

        User user = dao.login(username,password);

        //  USER NOT FOUND 

        if (user == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Wrong username or password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        //  ROLE CHECK 

        if (user.getRole() == null ||
                !user.getRole().trim().equalsIgnoreCase(selectedRole)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Wrong username or password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        //  SUCCESS 

        JOptionPane.showMessageDialog(
                this,
                "Welcome " + user.getUsername()
        );

        if (user.getRole().equalsIgnoreCase("ADMIN")) {

            new AdminDashboard();

        } else {

            new StudentDashboard(user.getId());
        }

        dispose();
    }

    public static void main(String[] args) {

        new LoginFrame();
    }
}