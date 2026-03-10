import java.util.*;

public class PlagiarismDetector {

    // n-gram → set of document IDs
    private HashMap<String, Set<String>> index = new HashMap<>();

    private int n = 5; // 5-gram window

    // Break document into n-grams
    private List<String> generateNGrams(String text) {

        List<String> ngrams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < n; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add document to index
    public void addDocument(String docId, String content) {

        List<String> ngrams = generateNGrams(content);

        for (String gram : ngrams) {

            index.putIfAbsent(gram, new HashSet<>());

            index.get(gram).add(docId);
        }
    }

    // Analyze document similarity
    public void analyzeDocument(String docId, String content) {

        List<String> ngrams = generateNGrams(content);

        HashMap<String, Integer> matchCounts = new HashMap<>();

        for (String gram : ngrams) {

            if (index.containsKey(gram)) {

                for (String otherDoc : index.get(gram)) {

                    if (!otherDoc.equals(docId)) {

                        matchCounts.put(otherDoc,
                                matchCounts.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCounts.entrySet()) {

            String otherDoc = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + otherDoc + "\"");

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED\n");
            } else if (similarity > 10) {
                System.out.println("Suspicious similarity\n");
            }
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "machine learning improves computer systems automatically "
                + "machine learning allows computers to learn from data";

        String essay2 = "machine learning allows computers to learn from data "
                + "deep learning is a subset of machine learning";

        String essay3 = "the history of art includes painting sculpture and architecture";

        // Add previous essays
        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_050.txt", essay3);

        // Analyze new submission
        String newEssay = "machine learning allows computers to learn from data "
                + "machine learning improves computer systems automatically";

        System.out.println("analyzeDocument(\"essay_123.txt\")");

        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}
