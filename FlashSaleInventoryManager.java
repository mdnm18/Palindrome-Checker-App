import java.util.*;

public class FlashSaleInventoryManager {

    // productId -> stockCount
    private HashMap<String, Integer> inventory = new HashMap<>();

    // productId -> waiting list (FIFO)
    private HashMap<String, LinkedHashMap<Integer, Integer>> waitingLists = new HashMap<>();

    // Add product with stock
    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingLists.put(productId, new LinkedHashMap<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {
            inventory.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            LinkedHashMap<Integer, Integer> waitingList = waitingLists.get(productId);
            int position = waitingList.size() + 1;
            waitingList.put(userId, position);

            return "Added to waiting list, position #" + position;
        }
    }

    // Show waiting list
    public void showWaitingList(String productId) {
        LinkedHashMap<Integer, Integer> waitingList = waitingLists.get(productId);

        System.out.println("Waiting List:");
        for (Map.Entry<Integer, Integer> entry : waitingList.entrySet()) {
            System.out.println("User " + entry.getKey() + " → Position #" + entry.getValue());
        }
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        // Add product with limited stock
        manager.addProduct("IPHONE15_256GB", 100);

        // Check stock
        System.out.println("checkStock(\"IPHONE15_256GB\") → " +
                manager.checkStock("IPHONE15_256GB") + " units available");

        // Simulate purchases
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock running out
        for (int i = 0; i < 98; i++) {
            manager.purchaseItem("IPHONE15_256GB", 20000 + i);
        }

        // Next purchase goes to waiting list
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));

        // Show waiting list
        manager.showWaitingList("IPHONE15_256GB");
    }
}
