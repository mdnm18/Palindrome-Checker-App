import java.util.Stack;
import java.util.Deque;
import java.util.LinkedList;

public class PalindromePerformance {

    // Method 1: Reverse String using loop
    public static boolean reverseMethod(String text) {
        String normalized = text.replaceAll("\\s+", "").toLowerCase();
        String reversed = "";

        for (int i = normalized.length() - 1; i >= 0; i--) {
            reversed += normalized.charAt(i);
        }

        return normalized.equals(reversed);
    }

    // Method 2: Two-pointer approach
    public static boolean twoPointerMethod(String text) {
        String normalized = text.replaceAll("\\s+", "").toLowerCase();
        int start = 0;
        int end = normalized.length() - 1;

        while (start < end) {
            if (normalized.charAt(start) != normalized.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }

        return true;
    }

    // Method 3: Stack approach
    public static boolean stackMethod(String text) {
        String normalized = text.replaceAll("\\s+", "").toLowerCase();
        Stack<Character> stack = new Stack<>();

        for (char c : normalized.toCharArray()) {
            stack.push(c);
        }

        for (int i = 0; i < normalized.length(); i++) {
            if (normalized.charAt(i) != stack.pop()) {
                return false;
            }
        }

        return true;
    }

    // Method 4: Deque approach
    public static boolean dequeMethod(String text) {
        String normalized = text.replaceAll("\\s+", "").toLowerCase();
        Deque<Character> deque = new LinkedList<>();

        for (char c : normalized.toCharArray()) {
            deque.addLast(c);
        }

        while (deque.size() > 1) {
            if (deque.removeFirst() != deque.removeLast()) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        String text = "A man a plan a canal Panama";

        long startTime, endTime;

        // Reverse Method
        startTime = System.nanoTime();
        boolean result1 = reverseMethod(text);
        endTime = System.nanoTime();
        System.out.println("Reverse Method Result: " + result1);
        System.out.println("Execution Time: " + (endTime - startTime) + " ns\n");

        // Two Pointer Method
        startTime = System.nanoTime();
        boolean result2 = twoPointerMethod(text);
        endTime = System.nanoTime();
        System.out.println("Two Pointer Method Result: " + result2);
        System.out.println("Execution Time: " + (endTime - startTime) + " ns\n");

        // Stack Method
        startTime = System.nanoTime();
        boolean result3 = stackMethod(text);
        endTime = System.nanoTime();
        System.out.println("Stack Method Result: " + result3);
        System.out.println("Execution Time: " + (endTime - startTime) + " ns\n");

        // Deque Method
        startTime = System.nanoTime();
        boolean result4 = dequeMethod(text);
        endTime = System.nanoTime();
        System.out.println("Deque Method Result: " + result4);
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");
    }
}
