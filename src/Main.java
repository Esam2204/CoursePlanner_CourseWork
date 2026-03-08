import java.util.*;

public class Main {

    static HashMap<String, HashSet<String>> prereqs = new HashMap<>();
    static HashMap<String, HashSet<String>> completed = new HashMap<>();

    public static void main(String[] args) {
        int N = 1000;

        long start = System.nanoTime();

        for (int i = 0; i < N; i++) {
            String student = "Student" + i;
            completed.putIfAbsent(student, new HashSet<>());
            completed.get(student).add("CS101");
        }

        long end = System.nanoTime();

        System.out.println("Average time to add " + N + " completions: " + (end - start) + " ns");
        Scanner sc = new Scanner(System.in);

        printHelp();


        while (true) {

            System.out.print("> ");
            String line = sc.nextLine().trim();

            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toUpperCase();

            switch (command) {

                case "HELP":
                    printHelp();
                    break;

                case "ADD_COURSE":
                    if (parts.length < 2) {
                        System.out.println("Missing course name");
                        break;
                    }
                    addCourse(parts[1]);
                    break;

                case "ADD_PREREQ":
                    if (parts.length < 3) {
                        System.out.println("Missing arguments");
                        break;
                    }
                    addPrereq(parts[1], parts[2]);
                    break;

                case "PREREQS":
                    if (parts.length < 2) {
                        System.out.println("Missing course name");
                        break;
                    }
                    printPrereqs(parts[1]);
                    break;

                case "COMPLETE":
                    if (parts.length < 3) {
                        System.out.println("Missing arguments");
                        break;
                    }
                    completeCourse(parts[1], parts[2]);
                    break;

                case "DONE":
                    if (parts.length < 2) {
                        System.out.println("Missing student name");
                        break;
                    }
                    printCompleted(parts[1]);
                    break;

                case "CAN_TAKE":
                    if (parts.length < 3) {
                        System.out.println("Missing arguments");
                        break;
                    }
                    canTake(parts[1], parts[2]);
                    break;

                case "EXIT":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Unknown command. Type HELP.");
            }
        }
    }

    static void printHelp() {
        System.out.println("Course Enrollment Planner — Commands:");
        System.out.println("HELP");
        System.out.println("ADD_COURSE <C>");
        System.out.println("ADD_PREREQ <C> <P>");
        System.out.println("PREREQS <C>");
        System.out.println("COMPLETE <student> <C>");
        System.out.println("DONE <student>");
        System.out.println("CAN_TAKE <student> <C>");
        System.out.println("EXIT");
    }

    static void addCourse(String c) {
        prereqs.putIfAbsent(c, new HashSet<>());
        System.out.println("Added course: " + c);
    }

    static void addPrereq(String c, String p) {

        if (c.equals(p)) {
            System.out.println("A course cannot be its own prerequisite");
            return;
        }

        prereqs.putIfAbsent(c, new HashSet<>());
        prereqs.putIfAbsent(p, new HashSet<>());

        prereqs.get(c).add(p);

        if (prereqs.containsKey(p) && prereqs.get(p).contains(c)) {
            System.out.println("Warning: Potential cycle");
        }

        System.out.println("Added prereq: " + p + " -> " + c);
    }

    static void printPrereqs(String c) {

        if (!prereqs.containsKey(c)) {
            System.out.println("Course not found");
            return;
        }

        System.out.println("Prereqs for " + c + ": " + prereqs.get(c));
    }

    static void completeCourse(String student, String course) {

        if (!canTakeCheck(student, course)) {
            System.out.println("Cannot complete " + course + " because prerequisites are missing");
            return;
        }

        completed.putIfAbsent(student, new HashSet<>());
        completed.get(student).add(course);

        System.out.println(student + " completed " + course);
    }

    static void printCompleted(String student) {

        if (!completed.containsKey(student)) {
            System.out.println("No record");
            return;
        }

        System.out.println(student + " completed: " + completed.get(student));
    }

    static void canTake(String student, String course) {

        if (canTakeCheck(student, course)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }

    static boolean canTakeCheck(String student, String course) {

        if (!prereqs.containsKey(course) || prereqs.get(course).isEmpty()) {
            return true;
        }

        HashSet<String> studentCourses = completed.getOrDefault(student, new HashSet<>());
        HashSet<String> coursePrereqs = prereqs.get(course);

        for (String p : coursePrereqs) {
            if (!studentCourses.contains(p)) {
                return false;
            }
        }

        return true;
    }
}