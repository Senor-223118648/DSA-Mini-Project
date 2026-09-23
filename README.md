# DSA-Mini-Project
DSA Group Assignment
import java.util.Scanner;
import java.util.Arrays;

    //--Part D---
    public class CampusServiceCentre {

    private static Queue waitingQueue = new Queue(20);
    private static StudentLinkedList recordList = new StudentLinkedList();
    private static ArrayStatistics dailyStats = new ArrayStatistics(50);
    private static Scanner scanner = new Scanner(System.in);

    // pre-loads a few arrivals so the menu isn't empty on first run
    public static void main(String[] args) {
        loadDemoData(); 
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt();

            switch (choice) {
                case 1: handleAddToQueue(); break;
                case 2: handleServeNext(); break;
                case 3: waitingQueue.displayQueue(); break;
                case 4: handleAddRecord(); break;
                case 5: recordList.displayStudents(); break;
                case 6: handleSearchRecord(); break;
                case 7: handleRemoveRecord(); break;
                case 8: dailyStats.displayStatistics(); break;
                case 9: handleSortServiceTimes(); break;
                case 10: SortExperiment.run(); break;
                case 11:
                    running = false;
                    System.out.println("Exiting Campus Service Centre. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-11.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println(" CAMPUS SERVICE CENTRE");
        System.out.println("========================================");
        System.out.println("1. Add student to waiting queue");
        System.out.println("2. Serve next student (remove from queue)");
        System.out.println("3. Display waiting students");
        System.out.println("4. Add student service record (Linked List)");
        System.out.println("5. Display student service records");
        System.out.println("6. Search for student record");
        System.out.println("7. Remove student record (by position - see option 5)");
        System.out.println("8. Display daily statistics");
        System.out.println("9. Sort service times");
        System.out.println("10. Run sorting experiment");
        System.out.println("11. Exit");
        System.out.print("Select option: ");
    }

    // ---- Option 1: Queue - enqueue ----
    private static void handleAddToQueue() {
        System.out.print("Student No: ");
        String no = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Service Type: ");
        String type = scanner.nextLine();
        System.out.print("Estimated Service Time (min): ");
        int time = readInt();

        waitingQueue.enqueue(new Student(no, name, type, time));
        System.out.println("Added to queue.");
    }

    // ---- Option 2: Queue - dequeue, then log into LinkedList (A2) and ArrayStatistics (A4) ----
    private static void handleServeNext() {
        Student served = waitingQueue.dequeue();
        if (served != null) {
            System.out.println("Now serving: " + served);
            recordList.insertAtEnd(served);                       // keep a permanent record
            dailyStats.addServiceTime(served.estimatedServiceTime); // track for daily stats
        }
    }

    // ---- Option 4: Linked List - insertion ----
    private static void handleAddRecord() {
        System.out.print("Student No: ");
        String no = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Service Type: ");
        String type = scanner.nextLine();
        System.out.print("Estimated Service Time (min): ");
        int time = readInt();

        recordList.insertAtEnd(new Student(no, name, type, time));
        System.out.println("Record added.");
    }

    //---- Option 6: Linked List - search ----
    private static void handleSearchRecord() {
        System.out.print("Enter Student No to search: ");
        String no = scanner.nextLine();
        Student found = recordList.searchStudent(no);
        if (found != null) {
            System.out.println("Found: " + found);
        } else {
            System.out.println("No record found for " + no);
        }
    }

    // ---- Option 7: Linked List - deletion (by position, matching the team's deleteStudent(int)) ----
    private static void handleRemoveRecord() {
        System.out.println("Current records:");
        recordList.displayStudents();
        System.out.print("Enter position to remove (1st, 2nd, ...): ");
        int position = readInt();
        recordList.deleteStudent(position);
        System.out.println("Done. Updated list:");
        recordList.displayStudents();
    }

    // ---- Option 9: Sorting algorithm(s) on today's recorded service times ----
    private static void handleSortServiceTimes() {
        int total = dailyStats.getTotalStudentsServed();
        if (total == 0) {
            System.out.println("No service times recorded yet. Serve some students first (option 2).");
            return;
        }

        int[] times = dailyStats.getServiceTimesSnapshot();
        System.out.println("Before sorting: " + Arrays.toString(times));
        System.out.println("Choose sorting algorithm: 1) Selection 2) Insertion 3) Merge 4) Quick");
        int algo = readInt();

        int[] toSort = times.clone();
        switch (algo) {
            case 1: SelectionSort.sort(toSort, true); break;
            case 2: InsertionSort.sort(toSort, true); break;
            case 3: MergeSort.sort(toSort, true); break;
            case 4: QuickSort.sort(toSort, true); break;
            default:
                System.out.println("Invalid choice, defaulting to Quick Sort.");
                QuickSort.sort(toSort, true);
        }
        System.out.println("After sorting: " + Arrays.toString(toSort));
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return value;
    }

    private static void loadDemoData() {
        waitingQueue.enqueue(new Student("221045678", "Maria", "Registration", 12));
        waitingQueue.enqueue(new Student("222034512", "Tomas", "Student Card", 5));
        waitingQueue.enqueue(new Student("223041876", "Ndapewa", "Fees", 8));
        waitingQueue.enqueue(new Student("221067341", "Simon", "Documents", 4));
        waitingQueue.enqueue(new Student("224012345", "Helena", "Academic Enquiry", 10));
        waitingQueue.enqueue(new Student("225098765", "Petrus", "Document Collection", 6));
    }
}
