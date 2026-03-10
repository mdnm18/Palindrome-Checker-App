import java.util.*;

public class DNSCacheSystem {

    // Entry class
    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, long ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // LRU Cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache;

    private int capacity;
    private int cacheHits = 0;
    private int cacheMisses = 0;

    public DNSCacheSystem(int capacity) {

        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCacheSystem.this.capacity;
            }
        };

        // Background thread to remove expired entries
        startCleanupThread();
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        long startTime = System.nanoTime();

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                cacheHits++;
                long endTime = System.nanoTime();
                System.out.println("Cache HIT → " + entry.ipAddress +
                        " (lookup " + (endTime - startTime) / 1000000.0 + " ms)");
                return entry.ipAddress;
            } else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED for " + domain);
            }
        }

        cacheMisses++;

        // Simulate upstream DNS query
        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 10)); // TTL 10 seconds

        System.out.println("Cache MISS → Query upstream → " + ip + " (TTL: 10s)");

        return ip;
    }

    // Simulated upstream DNS
    private String queryUpstreamDNS(String domain) {

        Random rand = new Random();

        return "172.217." + rand.nextInt(255) + "." + rand.nextInt(255);
    }

    // Background cleanup thread
    private void startCleanupThread() {

        Thread cleaner = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);

                    synchronized (DNSCacheSystem.this) {

                        Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

                        while (iterator.hasNext()) {
                            Map.Entry<String, DNSEntry> entry = iterator.next();
                            if (entry.getValue().isExpired()) {
                                iterator.remove();
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        cleaner.setDaemon(true);
        cleaner.start();
    }

    // Cache statistics
    public void getCacheStats() {

        int total = cacheHits + cacheMisses;
        double hitRate = total == 0 ? 0 : ((double) cacheHits / total) * 100;

        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }

    public static void main(String[] args) throws Exception {

        DNSCacheSystem dns = new DNSCacheSystem(5);

        dns.resolve("google.com");
        dns.resolve("google.com");

        Thread.sleep(11000); // wait for TTL expiration

        dns.resolve("google.com");

        dns.resolve("github.com");
        dns.resolve("openai.com");

        dns.getCacheStats();
    }
}
