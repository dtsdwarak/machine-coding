package in.dwarak.sampleapplication;

import java.util.HashMap;

public class Driver {
	
	private static final int TRANSACTIONS_ALLOWED = 10;
	private static final int UNIT_OF_TIME_IN_SECONDS = 1;
	
	private HashMap<String, RateLimiter> clientRateLimiterMap = new HashMap<>();
	
	void checkRequestAllowed(String userId, String time) {
		
		if (!clientRateLimiterMap.containsKey(userId)) {
			RateLimiter rateLimiter = new SlidingWindowRateLimiter(TRANSACTIONS_ALLOWED, UNIT_OF_TIME_IN_SECONDS);
			clientRateLimiterMap.put(userId, rateLimiter);
		}
		
		if (clientRateLimiterMap.get(userId).rateLimit()) {
			System.out.println("request for " + userId + "at time = "+ time + " ALLOWED");
		} else {
			System.out.println("request for " + userId + "at time = "+ time + " NOT ALLOWED");
		}
		
	}

}
