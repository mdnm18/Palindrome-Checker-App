import java.util.*;

public class UsernameAvailabilitySystem {

    // username -> userId mapping
    private HashMap<String, Integer> users = new HashMap<>();

    // username -> attempt frequency
    private HashMap<String, Integer> attempts = new HashMap<>();

    // Check if username is available
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !users.containsKey(username);
    }

    // Register a username
    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    // Suggest similar usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;
            if (!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        String dotVersion = username.replace("_", ".");
        if (!users.containsKey(dotVersion)) {
            suggestions.add(dotVersion);
        }

        return suggestions;
    }

    // Get the most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + maxAttempts + " attempts)";
    }

    // Main method for testing
    public static void main(String[] args) {

        UsernameAvailabilitySystem system = new UsernameAvailabilitySystem();

        // Pre-existing users
        system.registerUser("john_doe", 101);
        system.registerUser("admin", 102);

        // Username checks
        System.out.println("checkAvailability(\"john_doe\") → " +
                system.checkAvailability("john_doe"));

        System.out.println("checkAvailability(\"jane_smith\") → " +
                system.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("suggestAlternatives(\"john_doe\") → " +
                system.suggestAlternatives("john_doe"));

        // Simulate attempts
        for (int i = 0; i < 5; i++) {
            system.checkAvailability("admin");
        }

        for (int i = 0; i < 3; i++) {
            system.checkAvailability("john_doe");
        }

        // Most attempted username
        System.out.println("getMostAttempted() → " +
                system.getMostAttempted());
    }
}
