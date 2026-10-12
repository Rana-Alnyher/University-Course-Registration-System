package my.finalproject;

public class Course {

    private int courseId;
    private String courseCode;
    private String courseName;
    private String instructor;
    private int capacity;
    private int enrolled;
    private String schedule;

    public Course(
            int courseId,
            String courseCode,
            String courseName,
            String instructor,
            int capacity,
            String schedule
    ) {

        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.capacity = capacity;
        this.enrolled = 0;
        this.schedule = schedule;
    }


    public int getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public String getSchedule() {
        return schedule;
    }


    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {

        return courseCode + " - " + courseName +
                " | " + instructor +
                " | " + schedule +
                " | " + enrolled + "/" + capacity + " seats";
    }
}