import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DistributedRateLimiter {

    // Token Bucket structure
    static class TokenBucket {

        int maxTokens;
        double refillRate; // tokens per second
        double tokens;
        long lastRefillTime;

        TokenBucket(int maxTokens, int refillPeriodSeconds) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = (double) maxTokens / refillPeriodSeconds;
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on elapsed time
        synchronized void refill() {

            long now = System.currentTimeMillis();
            double elapsedSeconds = (now - lastRefillTime) / 1000.0;

            double tokensToAdd = elapsedSeconds * refillRate;

            tokens = Math.min(maxTokens, tokens + tokensToAdd);

            lastRefillTime = now;
        }

        // Try consuming a token
        synchronized boolean allowRequest() {

            refill();

            if (tokens >= 1) {
                tokens--;
                return true;
            }

            return false;
        }

        synchronized int remainingTokens() {
            return (int) tokens;
        }
    }

    // clientId → token bucket
    private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();

    private int maxRequests = 1000;
    private int windowSeconds = 3600; // 1 hour

    // Check rate limit
    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(maxRequests, windowSeconds));

        TokenBucket bucket = clients.get(clientId);

        boolean allowed = bucket.allowRequest();

        if (allowed) {
            System.out.println("Allowed (" + bucket.remainingTokens() + " requests remaining)");
        } else {
            System.out.println("Denied (0 requests remaining)");
        }

        return allowed;
    }

    // Get rate limit status
    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = maxRequests - bucket.remainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + maxRequests +
                ", remaining: " + bucket.remainingTokens() + "}");
    }

    public static void main(String[] args) {

        DistributedRateLimiter limiter = new DistributedRateLimiter();

        String clientId = "abc123";

        // Simulate API requests
        for (int i = 0; i < 5; i++) {
            limiter.checkRateLimit(clientId);
        }

        limiter.getRateLimitStatus(clientId);

        // Simulate many requests
        for (int i = 0; i < 1005; i++) {
            limiter.checkRateLimit(clientId);
        }
    }
}
