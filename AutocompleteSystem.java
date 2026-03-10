import java.util.*;

public class AutocompleteSystem {

    // Trie Node
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfQuery = false;
        String query;
    }

    // Root of Trie
    private TrieNode root = new TrieNode();

    // Global frequency map
    private HashMap<String, Integer> frequencyMap = new HashMap<>();

    // Insert query into Trie
    public void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEndOfQuery = true;
        node.query = query;

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }

    // DFS to collect queries
    private void collectQueries(TrieNode node, List<String> results) {

        if (node == null) return;

        if (node.isEndOfQuery) {
            results.add(node.query);
        }

        for (TrieNode child : node.children.values()) {
            collectQueries(child, results);
        }
    }

    // Search prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        collectQueries(node, results);

        // Min heap for top 10 results
        PriorityQueue<String> pq =
                new PriorityQueue<>((a, b) -> frequencyMap.get(a) - frequencyMap.get(b));

        for (String query : results) {

            pq.offer(query);

            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> suggestions = new ArrayList<>();

        while (!pq.isEmpty()) {
            suggestions.add(pq.poll());
        }

        Collections.reverse(suggestions);

        return suggestions;
    }

    // Update frequency for new searches
    public void updateFrequency(String query) {

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);

        insertQuery(query);

        System.out.println("Updated frequency of \"" + query + "\" → " + frequencyMap.get(query));
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        // Simulated popular queries
        system.insertQuery("java tutorial");
        system.insertQuery("javascript");
        system.insertQuery("java download");
        system.insertQuery("java interview questions");
        system.insertQuery("java tutorial");
        system.insertQuery("javascript");
        system.insertQuery("java tutorial");

        System.out.println("search(\"jav\") →");

        List<String> suggestions = system.search("jav");

        int rank = 1;

        for (String s : suggestions) {
            System.out.println(rank + ". " + s +
                    " (" + system.frequencyMap.get(s) + " searches)");
            rank++;
        }

        // Update frequency
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}
