import java.util.*;

public class TransactionAnalyzer {

    // Transaction structure
    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time;

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }

        public String toString() {
            return "(id:" + id + ", amount:" + amount + ")";
        }
    }

    // Classic Two-Sum
    public static List<List<Transaction>> findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();
        List<List<Transaction>> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                result.add(Arrays.asList(map.get(complement), t));
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Two-Sum with 1 hour time window
    public static List<List<Transaction>> findTwoSumWithWindow(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();
        List<List<Transaction>> result = new ArrayList<>();

        long oneHour = 3600 * 1000;

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= oneHour) {
                    result.add(Arrays.asList(prev, t));
                }
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // Duplicate detection
    public static Map<String, List<Transaction>> detectDuplicates(List<Transaction> transactions) {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        Map<String, List<Transaction>> duplicates = new HashMap<>();

        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {

            if (entry.getValue().size() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }

        return duplicates;
    }

    // K-Sum (recursive)
    public static List<List<Transaction>> findKSum(List<Transaction> transactions, int k, int target) {

        List<List<Transaction>> result = new ArrayList<>();

        kSumHelper(transactions, k, target, 0, new ArrayList<>(), result);

        return result;
    }

    private static void kSumHelper(List<Transaction> transactions, int k, int target,
                                   int start, List<Transaction> current,
                                   List<List<Transaction>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (k == 0 || start >= transactions.size()) return;

        for (int i = start; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            current.add(t);

            kSumHelper(transactions, k - 1, target - t.amount, i + 1, current, result);

            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        long now = System.currentTimeMillis();

        transactions.add(new Transaction(1, 500, "Store A", "acc1", now));
        transactions.add(new Transaction(2, 300, "Store B", "acc2", now + 1000));
        transactions.add(new Transaction(3, 200, "Store C", "acc3", now + 2000));
        transactions.add(new Transaction(4, 500, "Store A", "acc4", now + 3000));

        // Two-Sum
        System.out.println("findTwoSum(target=500) → " +
                findTwoSum(transactions, 500));

        // Two-Sum with time window
        System.out.println("findTwoSumWithWindow(target=500) → " +
                findTwoSumWithWindow(transactions, 500));

        // Duplicate detection
        System.out.println("\nDuplicate Transactions:");

        Map<String, List<Transaction>> duplicates = detectDuplicates(transactions);

        for (Map.Entry<String, List<Transaction>> entry : duplicates.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }

        // K-Sum
        System.out.println("\nfindKSum(k=3, target=1000) → " +
                findKSum(transactions, 3, 1000));
    }
}
