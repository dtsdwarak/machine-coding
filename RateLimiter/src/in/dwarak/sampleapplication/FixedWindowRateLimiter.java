package in.dwarak.sampleapplication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter extends RateLimiter {
	
	// Maintains a map at second windows.
	private final ConcurrentMap<Long, AtomicInteger> window = new ConcurrentHashMap<>();
	
	public FixedWindowRateLimiter(int transactionsAllowed, int unitOfTimeInSeconds) {
		super(transactionsAllowed, unitOfTimeInSeconds);
	}

	@Override
	public boolean rateLimit() {
		final long timeStampInSecondWindows = System.currentTimeMillis()/1000 * 1000;
		window.putIfAbsent(timeStampInSecondWindows, new AtomicInteger(0));
		return window.get(timeStampInSecondWindows).incrementAndGet() <= (transactionsAllowed/unitOfTimeInSeconds);
	}

}
