package my.finalproject;

import java.sql.*;
import javax.swing.*;

public class CourseService {

    public static boolean registerCourse(int studentId, int courseId) {

        try (
                Connection con = DBConnection.getConnection()
        ) {

            //  COURSE INFO 
            PreparedStatement course =
                    con.prepareStatement(
                            "SELECT schedule,capacity,enrolled FROM courses WHERE course_id=?"
                    );

            course.setInt(1, courseId);

            ResultSet rs = course.executeQuery();

            if (!rs.next()) {
            throw new Exception("Course not found");
}

            String newSchedule = rs.getString("schedule");

            int capacity = rs.getInt("capacity");

            int enrolled = rs.getInt("enrolled");

            //  FULL 
            if (enrolled >= capacity) {
            throw new Exception("Course Full");
}

            //  DUPLICATE 
            PreparedStatement dup =
                    con.prepareStatement(
                            "SELECT * FROM registrations WHERE student_id=? AND course_id=?"
                    );

            dup.setInt(1, studentId);
            dup.setInt(2, courseId);

            ResultSet r2 = dup.executeQuery();

            if (r2.next()) {
            throw new Exception("Already Registered");
}

            //  PREREQUISITE 
            PreparedStatement prereq =
                    con.prepareStatement(
                            """
                            SELECT prereq_course_id
                            FROM prerequisites
                            WHERE course_id=?
                            """
                    );

            prereq.setInt(1, courseId);

            ResultSet pre = prereq.executeQuery();

            while (pre.next()) {

                int prereqId = pre.getInt("prereq_course_id");

                PreparedStatement check =
                        con.prepareStatement(
                                """
                                SELECT *
                                FROM registrations
                                WHERE student_id=?
                                AND course_id=?
                                """
                        );

                check.setInt(1, studentId);
                check.setInt(2, prereqId);

                ResultSet done = check.executeQuery();

                if (!done.next()) {
                throw new Exception(
               "You must register prerequisite course first");
               }
            }

            //  CONFLICT 
            PreparedStatement conflict =
                    con.prepareStatement(
                            """
                            SELECT c.schedule
                            FROM registrations r
                            JOIN courses c
                            ON r.course_id=c.course_id
                            WHERE r.student_id=?
                            """
                    );

            conflict.setInt(1, studentId);

            ResultSet r3 = conflict.executeQuery();

            while (r3.next()) {String old = r3.getString("schedule");

                if (old.equalsIgnoreCase(newSchedule)) {
                throw new Exception("Schedule Conflict");
}
            }

            //  REGISTER 
            PreparedStatement insert =
                    con.prepareStatement(
                            "INSERT INTO registrations(student_id,course_id) VALUES(?,?)"
                    );

            insert.setInt(1, studentId);
            insert.setInt(2, courseId);

            insert.executeUpdate();

            PreparedStatement update =
                    con.prepareStatement(
                            "UPDATE courses SET enrolled=enrolled+1 WHERE course_id=?"
                    );

            update.setInt(1, courseId);

            update.executeUpdate();

            return true;

        } catch (Exception e) {

    String msg = e.getMessage();

    if ("Schedule Conflict".equals(msg)
            || "Already Registered".equals(msg)) {

        JOptionPane.showMessageDialog(
                null,
                msg,
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );

    } else {

        JOptionPane.showMessageDialog(
                null,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    return false;
}
    }

    //  DROP 

    public static boolean dropCourse(int studentId, int courseId) {

        try (
                Connection con = DBConnection.getConnection()
        ) {

            PreparedStatement ps =
                    con.prepareStatement(
                            """
                            DELETE FROM registrations
                            WHERE student_id=?
                            AND course_id=?
                            """
                    );

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            int rows = ps.executeUpdate();

            if (rows == 0) {

                JOptionPane.showMessageDialog(
                        null,
                        "Not Registered",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

            PreparedStatement update =
                    con.prepareStatement(
                            "UPDATE courses SET enrolled=enrolled-1 WHERE course_id=?"
                    );

            update.setInt(1, courseId);

            update.executeUpdate();

            return true;

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }

}