import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

class FIFOvsLIFO {

    public static void main(String[] args) {

        // String to demonstrate
        String word = "madam";

        // Create Queue and Stack
        Queue<Character> queue = new LinkedList<>();
        Stack<Character> stack = new Stack<>();

        // Insert characters into both data structures
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            queue.add(ch);   // Enqueue
            stack.push(ch);  // Push
        }

        // Compare dequeue and pop outputs
        boolean isPalindrome = true;

        while (!queue.isEmpty()) {
            char fromQueue = queue.remove(); // Dequeue (FIFO)
            char fromStack = stack.pop();    // Pop (LIFO)

            if (fromQueue != fromStack) {
                isPalindrome = false;
                break;
            }
        }

        // Display result
        if (isPalindrome) {
            System.out.println("The string \"" + word + "\" is a Palindrome.");
        } else {
            System.out.println("The string \"" + word + "\" is NOT a Palindrome.");
        }
    }
}
