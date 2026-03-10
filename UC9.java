class PalindromeRecursion {

    // Recursive method to check palindrome
    static boolean isPalindrome(String str, int start, int end) {

        // Base condition: if pointers cross or meet
        if (start >= end) {
            return true;
        }

        // If characters do not match
        if (str.charAt(start) != str.charAt(end)) {
            return false;
        }

        // Recursive call with updated indices
        return isPalindrome(str, start + 1, end - 1);
    }

    public static void main(String[] args) {

        String word = "level";

        boolean result = isPalindrome(word, 0, word.length() - 1);

        if (result) {
            System.out.println("The string \"" + word + "\" is a Palindrome.");
        } else {
            System.out.println("The string \"" + word + "\" is NOT a Palindrome.");
        }
    }
}
