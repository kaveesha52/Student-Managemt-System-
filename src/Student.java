import java.io.Serializable;

public class Student implements Serializable {

    private final String studentName;
    private final String studentID;
    private Module module_1 = new Module(0);
    private Module module_2 = new Module(0);
    private Module module_3 = new Module(0);
    private double total;
    private double average;
    private String grade;

    public Student(String studentID, String name) {
        this.studentID = studentID;
        this.studentName = name;
    }

    public String getStudentName() {
        return studentName;
    }

    public Module getModule_3() {
        return module_3;
    }

    public Module getModule_2() {
        return module_2;
    }

    public Module getModule_1() {
        return module_1;
    }

    public String getStudentID() {
        return studentID;
    }

    public double getAverage() {
        return average;
    }

    public double getTotal() {
        return total;
    }

    public String getGrade() {
        return grade;
    }

    public void setModule_1(Module module_1) {
        this.module_1 = module_1;
    }

    public void setModule_2(Module module_2) {
        this.module_2 = module_2;
    }

    public void setModule_3(Module module_3) {
        this.module_3 = module_3;
    }

    public void setTotal() {
        this.total = module_1.getModuleMark() + module_2.getModuleMark() + module_3.getModuleMark();
    }

    public void setAverage() {
        this.average = total / 3;
    }

    public void setGrade() {
        if (average >= 80) {
            this.grade = "Distinction";
        } else if (average >= 70) {
            this.grade = "Merit";
        } else if (average >= 40) {
            this.grade = "Pass";
        } else {
            this.grade = "Fail";
        }
    }
}