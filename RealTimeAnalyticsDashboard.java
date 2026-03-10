import java.util.*;

public class RealTimeAnalyticsDashboard {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public synchronized void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Count traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Get top 10 pages
    private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        for (int i = 0; i < 10 && !pq.isEmpty(); i++) {
            result.add(pq.poll());
        }

        return result;
    }

    // Display dashboard
    public synchronized void getDashboard() {

        System.out.println("\n===== REAL-TIME ANALYTICS DASHBOARD =====\n");

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(rank + ". " + page +
                    " - " + views + " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int totalTraffic = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            double percent = (entry.getValue() * 100.0) / totalTraffic;

            System.out.println(entry.getKey() + ": " +
                    String.format("%.1f", percent) + "%");
        }
    }

    public static void main(String[] args) throws Exception {

        RealTimeAnalyticsDashboard dashboard = new RealTimeAnalyticsDashboard();

        // Simulated incoming traffic
        String[] pages = {
                "/article/breaking-news",
                "/sports/championship",
                "/tech/ai-future",
                "/world/economy"
        };

        String[] sources = {"google", "facebook", "direct", "twitter"};

        Random random = new Random();

        // Simulate streaming events
        Thread eventGenerator = new Thread(() -> {

            int userCounter = 1;

            while (true) {

                String page = pages[random.nextInt(pages.length)];
                String source = sources[random.nextInt(sources.length)];

                String userId = "user_" + userCounter++;

                dashboard.processEvent(page, userId, source);

                try {
                    Thread.sleep(50); // simulate high traffic
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        eventGenerator.setDaemon(true);
        eventGenerator.start();

        // Update dashboard every 5 seconds
        while (true) {

            Thread.sleep(5000);

            dashboard.getDashboard();
        }
    }
}
