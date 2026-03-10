import java.util.*;

public class MultiLevelCacheSystem {

    // Video structure
    static class VideoData {
        String videoId;
        String content;

        VideoData(String videoId, String content) {
            this.videoId = videoId;
            this.content = content;
        }
    }

    // LRU Cache using LinkedHashMap
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private int capacity;

        LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    // L1 Cache (Memory)
    private LRUCache<String, VideoData> L1 = new LRUCache<>(10000);

    // L2 Cache (SSD simulation)
    private LRUCache<String, VideoData> L2 = new LRUCache<>(100000);

    // L3 Database simulation
    private HashMap<String, VideoData> database = new HashMap<>();

    // Access counters
    private HashMap<String, Integer> accessCount = new HashMap<>();

    // Statistics
    private int l1Hits = 0;
    private int l2Hits = 0;
    private int l3Hits = 0;

    public MultiLevelCacheSystem() {

        // Simulated database
        database.put("video_123", new VideoData("video_123", "Movie A"));
        database.put("video_456", new VideoData("video_456", "Movie B"));
        database.put("video_999", new VideoData("video_999", "Movie C"));
    }

    // Fetch video
    public VideoData getVideo(String videoId) {

        long start = System.nanoTime();

        // L1 lookup
        if (L1.containsKey(videoId)) {

            l1Hits++;

            System.out.println("L1 Cache HIT (0.5ms)");

            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 lookup
        if (L2.containsKey(videoId)) {

            l2Hits++;

            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2.get(videoId);

            promoteToL1(video);

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database lookup
        if (database.containsKey(videoId)) {

            l3Hits++;

            System.out.println("L3 Database HIT (150ms)");

            VideoData video = database.get(videoId);

            promoteToL2(video);

            return video;
        }

        System.out.println("Video not found");

        return null;
    }

    // Promote to L1
    private void promoteToL1(VideoData video) {

        L1.put(video.videoId, video);
    }

    // Promote to L2
    private void promoteToL2(VideoData video) {

        L2.put(video.videoId, video);

        accessCount.put(video.videoId,
                accessCount.getOrDefault(video.videoId, 0) + 1);
    }

    // Cache invalidation
    public void invalidate(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);
        database.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    // Statistics
    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        double l1Rate = total == 0 ? 0 : (l1Hits * 100.0) / total;
        double l2Rate = total == 0 ? 0 : (l2Hits * 100.0) / total;
        double l3Rate = total == 0 ? 0 : (l3Hits * 100.0) / total;

        System.out.println("\nCache Statistics:");

        System.out.println("L1 Hit Rate: " + String.format("%.1f", l1Rate) + "%");
        System.out.println("L2 Hit Rate: " + String.format("%.1f", l2Rate) + "%");
        System.out.println("L3 Hit Rate: " + String.format("%.1f", l3Rate) + "%");
    }

    public static void main(String[] args) {

        MultiLevelCacheSystem cache = new MultiLevelCacheSystem();

        System.out.println("\ngetVideo(\"video_123\")");

        cache.getVideo("video_123");

        System.out.println("\ngetVideo(\"video_123\") [second request]");

        cache.getVideo("video_123");

        System.out.println("\ngetVideo(\"video_999\")");

        cache.getVideo("video_999");

        cache.getStatistics();
    }
}
