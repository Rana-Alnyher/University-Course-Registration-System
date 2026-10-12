
package my.finalproject;

public class Student extends User{

private String studentId;


public Student(int id, String username, String password, String studentId) {
super(id, username, password, "STUDENT");
this.studentId = studentId;
}

//  GETTERS 
public String getStudentId() { return studentId; }


//  SETTERS 

@Override
public String toString() {
return "Student{" +
"studentId='" + studentId + '\'' +
'}';
}

}
