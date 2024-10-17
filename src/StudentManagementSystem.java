import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentManagementSystem {
    private final int maxStudentSize;
    private Student[] studentList;
    private String[][] studentDetails;
    private String[] temp;
    private int seats;
    private int numberOfStudents;
    private int passedStudents = 0;


    public StudentManagementSystem() {
        this.maxStudentSize = 100;
        this.studentDetails = new String[this.maxStudentSize][];
        this.studentList = new Student[this.maxStudentSize];
    }


    private boolean isValidID(String enteredStudentID) {
        if (enteredStudentID.isEmpty()) {
            System.out.println("Student ID cannot be blank. Please enter a Student ID.");
            return false;
        } else if (enteredStudentID.length() != 8) {
            System.out.println("Student ID must be exactly 8 characters long.");
            return false;
        } else if (!enteredStudentID.startsWith("w")) {
            System.out.println("Student ID must be in the format of 'w1234567'");
            return false;
        }
        return true;
    }

    // Validate the format of student ID
    private boolean isValidName(String enteredStudentName) {
        if (enteredStudentName.isEmpty()) {
            System.out.println("Student Name cannot be blank.Please enter a name.");
            return false;
        } else if (enteredStudentName.length() < 3) {
            System.out.println("Name must contain at least 3 characters.");
            return false;
        }
        return true;
    }

    // Get the index of a student by ID
    private int getIndexOfID(String id) {
        int i = 0;
        while (studentDetails[i] != null) {
            if (studentDetails[i][0].equals(id)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    // Save student marks to a file
    private void saveStudentMarks() {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream("marks.ser"))) {
            writer.writeObject(studentList);
        } catch (IOException ioException) {
            System.out.println("\n" + ioException.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("Something went wrong!" + e.getMessage() + "/n");
        }
    }

    // Load student marks from a file
    private void loadStudentMarks() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("marks.ser"))) {
            studentList = (Student[]) inputStream.readObject();
            int i = 0;
            for (; i < studentList.length; i++) {
                Student student = studentList[i];
                if (student == null) break;
            }
            numberOfStudents = i;

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("\n Marks file not found!\n");
        } catch (IOException ioException) {
            System.out.println("\n" + ioException.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\nSomething went wrong!\n" + e.getMessage() + "\n");
        }
    }


        // Check available seats for registration
    private void checkAvailableSeats() {
        System.out.println("\nAvailable seats: " + (maxStudentSize - seats) + "\n");
    }

    // Register a new student with ID and name
    private void registerStudentWithID() {
        if (seats == maxStudentSize) {
            System.out.println("\nRegistration limit reached. You can only register up to 100 students\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student ID for registration: ");
        String id = scanner.nextLine().trim();

        if (isValidID(id)) {
            if (getIndexOfID(id) != -1) {
                System.out.println("\nThe student ID '" + id + "' is already registered.\n");
                return;
            }

            System.out.print("Enter student Name for registration: ");
            String name = scanner.nextLine().trim();

            if (isValidName(name)) {
                studentDetails[seats] = new String[]{id, name};
                seats++;
                System.out.println("\nStudent registration successful\n" + "Name : " + name + "\n" + "ID: " + id + "\n");
            }
        }
    }

    // Delete a student from the system
    private void deleteStudent() {
        if (seats == 0) {
            System.out.println("\nThere are no students in the system to delete.\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        viewStudentsByName();

        System.out.print("Please enter the student ID to delete: ");
        String id = scanner.nextLine().trim();

        if (isValidID(id)) {
            if (getIndexOfID(id) != -1) {
                String[][] updatedStudentDetails = new String[maxStudentSize][];
                int i = 0;
                int counter = 0;

                while (studentDetails[i] != null) {
                    if (!studentDetails[i][0].equals(id)) {
                        updatedStudentDetails[counter++] = studentDetails[i];
                    }
                    i++;
                }

                System.out.println("\nStudent deletion successful!\n");

                studentDetails = updatedStudentDetails;
                seats--;
            } else {
                System.out.println("\nStudent ID " + id + " does not exist.\n");
            }
        }
    }

    // find a student from the system
    private void findStudentByID() {
        if (seats == 0) {
            System.out.println("\nThere are no students in the system.\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter the student ID you want to find: ");
        String id = scanner.nextLine().trim();

        if (isValidID(id)) {
            int index;
            if ((index = getIndexOfID(id)) != -1) {
                System.out.println("\nStudent has been found\nName : " + studentDetails[index][1] + "\nID: " + id + "\n");
            } else {
                System.out.println("\nStudent ID " + id + " does not exist.\n");
            }
        }
    }

    // Save student details to a file
    private void storeStudentDetailsToFile() {
        if (seats == 0) {
            System.out.println("\nThere are no students.\n");
            return;
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("studentsDetails.txt"))) {
            for (String[] studentDetail : studentDetails) {
                if (studentDetail == null) {
                    break;
                }
                System.out.println(studentDetail[0] + "," + studentDetail[1]);
                String data = studentDetail[0] + "," + studentDetail[1];
                bufferedWriter.write(data);
                bufferedWriter.newLine();
            }
            System.out.println("\nStudent details have been saved successfully!\n");
        } catch (IOException e) {
            System.out.println("\nSomething went wrong!\n" + e.getMessage());
        }

        saveStudentMarks();
    }

    // Load student details from a file
    private void loadStudentDetailsFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("studentsDetails.txt"))) {
            int counter = 0;
            while (bufferedReader.ready()) {
                String data = bufferedReader.readLine().trim();

                String[] studentDetail = data.split(",");

                studentDetails[counter++] = new String[]{studentDetail[0], studentDetail[1]};
                seats++;
            }
            System.out.println("\nFile has been loaded successfully!\n");

        } catch (FileNotFoundException fileNotFoundException) {
            File file1 = new File("studentsDetails.txt");
            try {
                boolean isCreated = file1.createNewFile();
                if (isCreated) {
                    System.out.println("\nFile not found. A new file has been successfully created!\n");
                }
            } catch (IOException e) {
                System.out.println("Something went wrong!\n" + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
        }

        loadStudentMarks();
    }

    // View all students sorted by name
    private void viewStudentsByName() {
        if (seats == 0) {
            System.out.println("There are no students.");
            return;
        }

        for (int i = 0; i < maxStudentSize; i++) {
            if (studentDetails[i] == null) {
                break;
            }

            boolean isSwapped = false;
            String name;
            String nextName;

            for (int j = 0; j < maxStudentSize; j++) {
                if (studentDetails[j] != null && studentDetails[j + 1] != null) {
                    name = studentDetails[j][1];
                    nextName = studentDetails[j + 1][1];
                } else {
                    continue;
                }

                if (name.compareTo(nextName) > 0) {
                    String tempName[] = studentDetails[j];
                    studentDetails[j] = studentDetails[j + 1];
                    studentDetails[j + 1] = tempName;
                    isSwapped = true;
                }
            }
            if (!isSwapped) {
                break;
            }
        }
        System.out.println("\nStudent ID  |   Student Name\n");
        for (String[] studentDetail : studentDetails) {
            if (studentDetail == null) {
                break;
            }
            System.out.println(studentDetail[0] + "    |   " + studentDetail[1]);
        }
        System.out.println();
    }


    // Sub menu
    private String addStudentResult() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("a) Add student Name :");
        System.out.println("b) Add module marks :");
        System.out.println("c) Generate summary report :");
        System.out.println("d) Generate complete report :");
        System.out.print("Please enter your choice: ");
        return scanner.next().trim().toLowerCase();
    }

    // Add student marks
    private void addStudentName() {
        if (numberOfStudents == maxStudentSize) {
            System.out.println("\nYou can only add up to " + maxStudentSize + " students!\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student Name : ");
        String name = scanner.nextLine().trim();

        for (Student student : studentList) {
            if (student != null && name.equals(student.getStudentName())) {
                System.out.println("This student's module marks has already being entered into the system");
                return;
            }
        }

        if (isValidName(name)) {
            int i = 0;
            int counter = -1;
            while (studentDetails[i] != null) {
                if (studentDetails[i][1].equalsIgnoreCase(name)) {
                    counter = i;
                    break;
                }
                i++;
            }

            if (counter == -1) {
                System.out.println("\nStudent '" + name + "' not found. Please register first.\n");
                return;
            }
            Student student = new Student(studentDetails[counter][0], name);

            for (i = 0; i < studentList.length; i++) {
                if (studentList[i] == null) {
                    studentList[i] = student;
                    break;
                }
            }
            System.out.println("\nStudent  added successfully!\nName : " + name
                    + "\nID: " + studentDetails[counter][0] + "\n");
            numberOfStudents++;
        }
    }

    // Calculate and display student average marks
    private void addModuleMarks() {
        if (numberOfStudents == 0) {
            System.out.println("There are no students in the result system. First add students to the result system");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        String studentID;
        System.out.print("1) Please enter the student ID to add module marks: ");
        studentID = scanner.nextLine().trim().toLowerCase();

        if (isValidID(studentID)) {
            int studentIndex = -1;
            for (int i = 0; i < studentList.length; i++) {
                if (studentList[i] == null) {
                    break;
                }

                if (studentList[i].getStudentID().equalsIgnoreCase(studentID)) {
                    studentIndex = i;
                    break;
                }
            }

            if (studentIndex == -1) {
                System.out.println("\nStudent ID " + studentID + " does not exist in the result system.\n");
                return;
            }


            System.out.print("Enter the marks of module 1: ");
            double mark1 = 0;

            try {
                mark1 = scanner.nextDouble();
                if (mark1 < 0 || mark1 > 100) {
                    System.out.println("\nThe module mark '" + mark1 + "' is invalid.\n");
                    return;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nThe module mark '" + mark1 + "' is invalid.\n");
                return;
            }

            System.out.print("Enter the marks of module 2: ");
            double mark2 = 0;

            try {
                mark2 = scanner.nextDouble();
                if (mark2 < 0 || mark2 > 100) {
                    System.out.println("\nThe module mark '" + mark2 + "' is invalid.\n");
                    return;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nThe module mark '" + mark2 + "' is invalid.\n");
                return;
            }

            System.out.print("Enter the marks of module 3: ");
            double mark3 = 0;

            try {
                mark3 = scanner.nextDouble();
                if (mark3 < 0 || mark3 > 100) {
                    System.out.println("\nThe module mark '" + mark3 + "' is invalid.\n");
                    return;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nThe module mark '" + mark3 + "' is invalid.\n");
                return;
            }

            Module module_1 = new Module(mark1);
            Module module_2 = new Module(mark2);
            Module module_3 = new Module(mark3);

            Student student = studentList[studentIndex];
            student.setModule_1(module_1);
            student.setModule_2(module_2);
            student.setModule_3(module_3);
            student.setTotal();
            student.setAverage();
            student.setGrade();

            System.out.println("\nStudent marks added successfully!\nName: " + student.getStudentName()
                    + "\nID : " + studentID + "\nmark 1: " + mark1 + "\nmark 2: " + mark2 + "\nmark 3: " + mark3 + "\n");
        }
    }

    // Create summary report
    private void generateSummaryReport() {
        if (numberOfStudents == 0) {
            System.out.println("\nThere are no students currently in the result system. First add students to the result system.\n");
            return;
        }


        System.out.println("+----------------------------------------------+");
        System.out.println("|                 Summary Report               |");
        System.out.println("+----------------------------------------------+");
        System.out.println(" Total student registration             | " + seats);

        passedStudents = 0;
        for (Student student : studentList) {
            if (student != null && student.getModule_1().getModuleMark() > 40 && student.getModule_2().getModuleMark() > 40 && student.getModule_3().getModuleMark() > 40) {
                passedStudents++;
            }
        }

        System.out.println(" Total student (module1,2,3 marks > 40) | " + passedStudents);
        System.out.println();

    }

    // Create complete report

    private void generateCompleteReport() {
        if (numberOfStudents == 0) {
            System.out.println("\nThere are no students currently in the result system. First add students to the result system\n");
            return;
        }

        for (int i = 0; i < studentList.length; i++) {
            if (studentList[i] == null) {
                break;
            }
            boolean isSwapped = false;
            int j = 0;
            while (true) {
                double currentAverageMark = studentList[j].getAverage();
                if (studentList[j + 1] == null) {
                    break;
                }
                double nextAverageMark = studentList[j + 1].getAverage();
                if (currentAverageMark < nextAverageMark) {
                    Student tempStudent = studentList[j];
                    studentList[j] = studentList[j + 1];
                    studentList[j + 1] = tempStudent;
                    isSwapped = true;
                }
                j++;
            }
            if (!isSwapped) {
                break;
            }
        }

        System.out.println("\n| No  | Student ID | Student Name               | Module 1 marks | Module 2 marks | Module 3 marks | Total Marks | Average | Grade       |");

        String leftAlignFormat = "| %-3d | %-10s | %-26s | %-14.2f | %-14.2f | %-14.2f | %-11.2f | %-7.2f | %-11s |%n";

        for (int i = 0; i < studentList.length; i++) {
            if (studentList[i] == null) break;

            String studentID = studentList[i].getStudentID();
            String studentName = studentList[i].getStudentName();
            double moduleMark1 = studentList[i].getModule_1().getModuleMark();
            double moduleMark2 = studentList[i].getModule_2().getModuleMark();
            double moduleMark3 = studentList[i].getModule_3().getModuleMark();
            double totalMarks = studentList[i].getTotal();
            double averageMarks = studentList[i].getAverage();
            String grade = studentList[i].getGrade();

            System.out.format(leftAlignFormat, (i + 1), studentID, studentName,
                    moduleMark1, moduleMark2, moduleMark3,
                    totalMarks, averageMarks, grade);
        }
        System.out.println();
    }

    // main menu
    public void mainMenuPrinter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n-----Welcome to the Student Management System-----\n");
        temp = new String[100];

        int option = 0;
        while (true) {
            String[] description = {
                    "To check available seats",
                    "To register student",
                    "To delete student",
                    "To find student",
                    "To save file",
                    "To load file",
                    "To view added students",
                    "To add students results",
                    "To exit the program"
            };

            for (int i = 0; i < description.length; i++) {
                System.out.println((i + 1) + ")." + description[i]);

            }

            System.out.print("\nPlease enter your choice: ");

            try {
                option = scanner.nextInt();

                if (option == 1) {
                    this.checkAvailableSeats();
                } else if (option == 2) {
                    this.registerStudentWithID();
                } else if (option == 3) {
                    this.deleteStudent();
                } else if (option == 4) {
                    this.findStudentByID();
                } else if (option == 5) {
                    this.storeStudentDetailsToFile();
                } else if (option == 6) {
                    this.loadStudentDetailsFromFile();
                } else if (option == 7) {
                    this.viewStudentsByName();
                } else if (option == 8) {
                    String choice = this.addStudentResult();
                    if (choice.equals("a")) {
                        this.addStudentName();
                    } else if (choice.equals("b")) {
                        this.addModuleMarks();
                    } else if (choice.equals("c")) {
                        this.generateSummaryReport();
                    } else if (choice.equals("d")) {
                        this.generateCompleteReport();
                    } else {
                        System.out.println("\nInvalid choice please try again!\n");
                    }
                } else if (option == 9) {
                    break;
                } else {
                    System.out.println("\nInvalid choice please try again!\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nPlease enter a valid choice!\n");
                System.out.println();
                scanner.next();
            }
        }
    }
}