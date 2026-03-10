class PalindromeChecker {

    // Method to check whether a string is palindrome
    public boolean checkPalindrome(String text) {

        // Normalize the string
        String normalized = text.replaceAll("\\s+", "").toLowerCase();

        // Convert to character array
        char[] arr = normalized.toCharArray();

        int start = 0;
        int end = arr.length - 1;

        // Compare characters
        while (start < end) {
            if (arr[start] != arr[end]) {
                return false;
            }
            start++;
            end--;
        }

        return true;
    }
}

class MainApp {

    public static void main(String[] args) {

        String text = "Madam";

        // Create object of PalindromeChecker
        PalindromeChecker checker = new PalindromeChecker();

        boolean result = checker.checkPalindrome(text);

        if (result) {
            System.out.println("The text \"" + text + "\" is a Palindrome.");
        } else {
            System.out.println("The text \"" + text + "\" is NOT a Palindrome.");
        }
    }
}
