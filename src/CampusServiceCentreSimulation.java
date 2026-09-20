import java.util.Scanner;

public class CampusServiceCentreSimulation {

    private static final int MAX_REQUESTS = 200;

    private final StudentRequest[] requests = new StudentRequest[MAX_REQUESTS];
    private int requestCount = 0;

    private final ServiceQueue queue = new ServiceQueue();
    private final ServiceHistoryLinkedList serviceHistory = new ServiceHistoryLinkedList();

    public static void main(String[] args) {
        CampusServiceCentreSimulation simulation = new CampusServiceCentreSimulation();
        simulation.seedSampleData();
        simulation.runMenu();
    }

    private void runMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt(scanner, "Choose an option: ");
            switch (choice) {
                case 1:
                    addRequest(scanner);
                    break;
                case 2:
                    serveNextRequest();
                    break;
                case 3:
                    queue.display();
                    break;
                case 4:
                    displayAllRequests();
                    break;
                case 5:
                    searchByStudentNumber(scanner);
                    break;
                case 6:
                    sortingMenu(scanner);
                    break;
                case 7:
                    serviceHistory.display();
                    break;
                case 8:
                    runStackExercise(scanner);
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Campus Service Centre Simulation ===");
        System.out.println("1. Add student request");
        System.out.println("2. Serve next student (Queue)");
        System.out.println("3. View waiting queue");
        System.out.println("4. View all requests (Array)");
        System.out.println("5. Search request by student number");
        System.out.println("6. Sort requests by estimated service time");
        System.out.println("7. View served history (Linked List)");
        System.out.println("8. Stack exercise (balanced brackets)");
        System.out.println("0. Exit");
    }

    private void addRequest(Scanner scanner) {
        if (requestCount >= MAX_REQUESTS) {
            System.out.println("Storage full: cannot add more requests.");
            return;
        }

        long studentNo = readLong(scanner, "Student No: ");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Service Type: ");
        String serviceType = scanner.nextLine().trim();
        int estimatedMinutes = readInt(scanner, "Estimated Service Time (min): ");

        StudentRequest request = new StudentRequest(studentNo, name, serviceType, estimatedMinutes);
        requests[requestCount++] = request;
        queue.enqueue(request);
        System.out.println("Request added to queue.");
    }

    private void serveNextRequest() {
        StudentRequest served = queue.dequeue();
        if (served == null) {
            System.out.println("No students waiting.");
            return;
        }

        serviceHistory.add(served);
        System.out.println("Serving: " + served);
    }

    private void displayAllRequests() {
        if (requestCount == 0) {
            System.out.println("No requests recorded.");
            return;
        }

        System.out.println("\nAll Requests (array order):");
        for (int i = 0; i < requestCount; i++) {
            System.out.println((i + 1) + ". " + requests[i]);
        }
    }

    private void searchByStudentNumber(Scanner scanner) {
        if (requestCount == 0) {
            System.out.println("No requests available.");
            return;
        }

        long studentNo = readLong(scanner, "Enter student number to search: ");
        int index = linearSearchByStudentNo(studentNo);
        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Found at array index " + index + ": " + requests[index]);
    }

    private int linearSearchByStudentNo(long studentNo) {
        for (int i = 0; i < requestCount; i++) {
            if (requests[i].studentNo == studentNo) {
                return i;
            }
        }
        return -1;
    }

    private void sortingMenu(Scanner scanner) {
        if (requestCount == 0) {
            System.out.println("No requests to sort.");
            return;
        }

        StudentRequest[] copy = copyRequests();

        System.out.println("\nSort by Estimated Service Time:");
        System.out.println("1. Selection Sort");
        System.out.println("2. Insertion Sort");
        System.out.println("3. Merge Sort");
        System.out.println("4. Quick Sort");

        int choice = readInt(scanner, "Choose sorting algorithm: ");
        long start = System.nanoTime();

        switch (choice) {
            case 1:
                selectionSort(copy);
                break;
            case 2:
                insertionSort(copy);
                break;
            case 3:
                mergeSort(copy, 0, copy.length - 1);
                break;
            case 4:
                quickSort(copy, 0, copy.length - 1);
                break;
            default:
                System.out.println("Invalid sorting option.");
                return;
        }

        long elapsedNanos = System.nanoTime() - start;
        System.out.println("\nSorted Requests:");
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
        System.out.println("Time taken: " + elapsedNanos + " ns");
    }

    private StudentRequest[] copyRequests() {
        StudentRequest[] copy = new StudentRequest[requestCount];
        for (int i = 0; i < requestCount; i++) {
            StudentRequest original = requests[i];
            copy[i] = new StudentRequest(
                    original.studentNo,
                    original.name,
                    original.serviceType,
                    original.estimatedServiceTime
            );
        }
        return copy;
    }

    private void selectionSort(StudentRequest[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j].estimatedServiceTime < array[minIndex].estimatedServiceTime) {
                    minIndex = j;
                }
            }
            StudentRequest temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    private void insertionSort(StudentRequest[] array) {
        for (int i = 1; i < array.length; i++) {
            StudentRequest key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j].estimatedServiceTime > key.estimatedServiceTime) {
                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = key;
        }
    }

    private void mergeSort(StudentRequest[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    private void merge(StudentRequest[] array, int left, int mid, int right) {
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        StudentRequest[] leftArray = new StudentRequest[leftSize];
        StudentRequest[] rightArray = new StudentRequest[rightSize];

        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftSize && j < rightSize) {
            if (leftArray[i].estimatedServiceTime <= rightArray[j].estimatedServiceTime) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }

        while (i < leftSize) {
            array[k++] = leftArray[i++];
        }

        while (j < rightSize) {
            array[k++] = rightArray[j++];
        }
    }

    private void quickSort(StudentRequest[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private int partition(StudentRequest[] array, int low, int high) {
        int pivot = array[high].estimatedServiceTime;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j].estimatedServiceTime <= pivot) {
                i++;
                StudentRequest temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        StudentRequest temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    private void runStackExercise(Scanner scanner) {
        System.out.print("Enter an expression to check balanced brackets: ");
        String expression = scanner.nextLine();
        boolean balanced = isBalanced(expression);
        System.out.println(balanced ? "Balanced" : "Not balanced");
    }

    private boolean isBalanced(String expression) {
        CharStack stack = new CharStack(expression.length());

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            } else if (ch == ')' || ch == '}' || ch == ']') {
                if (stack.isEmpty()) {
                    return false;
                }

                char top = stack.pop();
                if (!isMatchingPair(top, ch)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    private boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')')
                || (open == '{' && close == '}')
                || (open == '[' && close == ']');
    }

    private void seedSampleData() {
        addSample(221045678L, "Maria", "Registration", 12);
        addSample(222034512L, "Tomas", "Student Card", 5);
        addSample(223041876L, "Ndapewa", "Fees", 8);
        addSample(221067341L, "Simon", "Documents", 4);
    }

    private void addSample(long studentNo, String name, String serviceType, int estimatedTime) {
        StudentRequest request = new StudentRequest(studentNo, name, serviceType, estimatedTime);
        requests[requestCount++] = request;
        queue.enqueue(request);
    }

    private int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Long.parseLong(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static class StudentRequest {
        private final long studentNo;
        private final String name;
        private final String serviceType;
        private final int estimatedServiceTime;

        private StudentRequest(long studentNo, String name, String serviceType, int estimatedServiceTime) {
            this.studentNo = studentNo;
            this.name = name;
            this.serviceType = serviceType;
            this.estimatedServiceTime = estimatedServiceTime;
        }

        @Override
        public String toString() {
            return "StudentNo=" + studentNo
                    + ", Name='" + name + '\''
                    + ", Service='" + serviceType + '\''
                    + ", EstimatedTime=" + estimatedServiceTime + " min";
        }
    }

    private static class ServiceQueue {
        private QueueNode front;
        private QueueNode rear;

        private void enqueue(StudentRequest data) {
            QueueNode node = new QueueNode(data);
            if (rear == null) {
                front = rear = node;
                return;
            }
            rear.next = node;
            rear = node;
        }

        private StudentRequest dequeue() {
            if (front == null) {
                return null;
            }

            StudentRequest value = front.data;
            front = front.next;
            if (front == null) {
                rear = null;
            }

            return value;
        }

        private void display() {
            if (front == null) {
                System.out.println("Queue is empty.");
                return;
            }

            QueueNode current = front;
            int position = 1;
            System.out.println("\nQueue (arrival order):");
            while (current != null) {
                System.out.println(position + ". " + current.data);
                current = current.next;
                position++;
            }
        }

        private static class QueueNode {
            private final StudentRequest data;
            private QueueNode next;

            private QueueNode(StudentRequest data) {
                this.data = data;
            }
        }
    }

    private static class ServiceHistoryLinkedList {
        private HistoryNode head;

        private void add(StudentRequest request) {
            HistoryNode node = new HistoryNode(request);
            if (head == null) {
                head = node;
                return;
            }

            HistoryNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }

        private void display() {
            if (head == null) {
                System.out.println("No students have been served yet.");
                return;
            }

            HistoryNode current = head;
            int count = 1;
            System.out.println("\nServed History:");
            while (current != null) {
                System.out.println(count + ". " + current.data);
                current = current.next;
                count++;
            }
        }

        private static class HistoryNode {
            private final StudentRequest data;
            private HistoryNode next;

            private HistoryNode(StudentRequest data) {
                this.data = data;
            }
        }
    }

    private static class CharStack {
        private final char[] array;
        private int top;

        private CharStack(int capacity) {
            this.array = new char[Math.max(1, capacity)];
            this.top = -1;
        }

        private void push(char value) {
            if (top + 1 >= array.length) {
                return;
            }
            array[++top] = value;
        }

        private char pop() {
            if (isEmpty()) {
                return '\0';
            }
            return array[top--];
        }

        private boolean isEmpty() {
            return top == -1;
        }
    }
}
