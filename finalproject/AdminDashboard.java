package my.finalproject;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    JPanel contentPanel, menu;

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(600,500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0,20,600,30);

        add(title);

        menu = new JPanel(new GridLayout(5,1,10,10));
        menu.setBounds(210,100,180,250);

        JButton addBtn = new JButton("Add Course");
        JButton updateBtn = new JButton("Update Course");
        JButton deleteBtn = new JButton("Delete Course");
        JButton viewBtn = new JButton("View Courses");
        JButton logoutBtn = new JButton("Logout");

        menu.add(addBtn);
        menu.add(updateBtn);
        menu.add(deleteBtn);
        menu.add(viewBtn);
        menu.add(logoutBtn);

        add(menu);

        contentPanel = new JPanel();
        contentPanel.setVisible(false);

        add(contentPanel);

        addBtn.addActionListener(e -> {

            menu.setVisible(false);
            showAdd();

        });

        updateBtn.addActionListener(e -> {

            menu.setVisible(false);
            showUpdate();

        });

        deleteBtn.addActionListener(e -> {

            menu.setVisible(false);
            showDelete();

        });

        viewBtn.addActionListener(e -> {

            menu.setVisible(false);
            showView();

        });

        logoutBtn.addActionListener(e -> {

            new LoginFrame();
            dispose();

        });

        setVisible(true);
    }

    void resizePanel(int width,int height){

        contentPanel.setBounds(
                (600-width)/2,
                100,
                width,
                height
        );

    }

    void showMain(){

        menu.setVisible(true);

        contentPanel.removeAll();

        contentPanel.setVisible(false);

        resizePanel(400,300);

        refresh();
    }

    void showAdd(){

        resizePanel(400,300);

        contentPanel.setVisible(true);

        contentPanel.removeAll();

        JPanel p =
                new JPanel(
                        new GridLayout(
                                8,
                                2,
                                10,
                                10
                        )
                );

        JTextField code =new JTextField();

        JTextField name =new JTextField();

        JTextField instructor =new JTextField();

        JTextField capacity =new JTextField();

        JTextField schedule =new JTextField();

        JTextField prereq =new JTextField();

        JButton save =new JButton("Save");

        JButton back =new JButton("Back");

        p.add(new JLabel("Code"));
        p.add(code);

        p.add(new JLabel("Name"));
        p.add(name);

        p.add(new JLabel("Instructor"));
        p.add(instructor);

        p.add(new JLabel("Capacity"));
        p.add(capacity);

        p.add(new JLabel("Schedule"));
        p.add(schedule);

        p.add(new JLabel("Prerequisite"));
        p.add(prereq);

        p.add(save);
        p.add(back);

        save.addActionListener(e -> {

            try{

                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =con.prepareStatement(
                                "INSERT INTO courses(course_code,course_name,instructor,capacity,schedule) VALUES(?,?,?,?,?)"
                        );

                ps.setString(1,code.getText());

                ps.setString(2,name.getText());

                ps.setString(3,instructor.getText());

                ps.setInt(4,Integer.parseInt(capacity.getText()));

                ps.setString(5, schedule.getText());

                ps.executeUpdate();
                
                JOptionPane.showMessageDialog(
                        this,
                        "Course Added"
                );

            }

            catch(Exception ex){

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage()
                );

            }

        });

        back.addActionListener(
                e -> showMain()
        );

        contentPanel.add(p);

        refresh();
    }

    void showUpdate(){

        resizePanel(350,250);

        contentPanel.setVisible(true);

        contentPanel.removeAll();

        JPanel p =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                10,
                                10
                        )
                );

        JTextField id =
                new JTextField();

        JTextField instructor =
                new JTextField();

        JTextField capacity =
                new JTextField();

        JTextField schedule =
                new JTextField();

        JButton update =
                new JButton("Update");

        JButton back =
                new JButton("Back");

        p.add(new JLabel("Course ID"));
        p.add(id);

        p.add(new JLabel("Instructor"));
        p.add(instructor);

        p.add(new JLabel("Capacity"));
        p.add(capacity);

        p.add(new JLabel("Schedule"));
        p.add(schedule);

        p.add(update);
        p.add(back);

        update.addActionListener(e -> {

            try{

                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(

                                """
                                UPDATE courses
                                SET instructor=?,
                                capacity=?,
                                schedule=?
                                WHERE course_id=?
                                """
                        );

                ps.setString(1,instructor.getText());

                ps.setInt(2, Integer.parseInt(capacity.getText()));

                ps.setString(3,schedule.getText());

                ps.setInt(4,Integer.parseInt(id.getText()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Updated");

            }

            catch(Exception ex){

                JOptionPane.showMessageDialog(this,ex.getMessage());
            }
        });

        back.addActionListener(
                e -> showMain()
        );

        contentPanel.add(p);

        refresh();
    }

    void showDelete(){

        resizePanel(250,140);

        contentPanel.setVisible(true);

        contentPanel.removeAll();

        JPanel p =new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                10
                        )
                );

        JTextField id =new JTextField();

        JButton delete =new JButton("Delete");

        JButton back =new JButton("Back");

        p.add(new JLabel("Course ID"));
        p.add(id);

        p.add(new JLabel());
        p.add(new JLabel());

        p.add(delete);
        p.add(back);

        delete.addActionListener(e -> {

            try{

                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement("DELETE FROM courses WHERE course_id=?");

                ps.setInt(1,Integer.parseInt(id.getText()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Deleted");

            }

            catch(Exception ex){

                JOptionPane.showMessageDialog(this,ex.getMessage());

            }

        });

        back.addActionListener(e -> showMain());
        contentPanel.add(p);

        refresh();
    }

    void showView(){

        resizePanel(450,300);

        contentPanel.setVisible(true);

        contentPanel.removeAll();

        try{

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM courses");

            ResultSet rs =ps.executeQuery();

            StringBuilder s =new StringBuilder();

            while(rs.next()){

                s.append(rs.getInt("course_id"))

                .append(" | ").append(rs.getString("course_name"))

                .append(" | ").append(rs.getString("schedule")).append("\n");

            }

            JTextArea area =new JTextArea(s.toString());

            JButton back =new JButton("Back");

            back.addActionListener(e -> showMain());

            contentPanel.setLayout(new BorderLayout());

            contentPanel.add(
                    new JScrollPane(area),
                    BorderLayout.CENTER
            );

            contentPanel.add(
                    back,
                    BorderLayout.SOUTH
            );

        }

        catch(Exception ex){

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );

        }

        refresh();

    }

    void refresh(){

        contentPanel.revalidate();
        contentPanel.repaint();

    }

}