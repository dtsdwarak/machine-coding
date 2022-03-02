package in.dwarak.sampleapplication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowRateLimiter extends RateLimiter {
	
	private final ConcurrentMap<Long, AtomicInteger> window = new ConcurrentHashMap<>();
	
	public SlidingWindowRateLimiter(int transactionsAllowed, int unitOfTimeInSeconds) {
		super(transactionsAllowed, unitOfTimeInSeconds);
	}

	@Override
	public boolean rateLimit() {
		final long currentTimeStampInMilliSeconds = System.currentTimeMillis();
		final long currentTimeStampInSecondWindows = currentTimeStampInMilliSeconds/1000*1000;
		
		window.putIfAbsent(currentTimeStampInSecondWindows, new AtomicInteger(0));
		
		long previousTimeStampInSecondWindows = currentTimeStampInSecondWindows - 1000;
		AtomicInteger transactionsAllowedInPreviousWindow = window.get(previousTimeStampInSecondWindows);
		
		if (transactionsAllowedInPreviousWindow == null)
			return window.get(currentTimeStampInSecondWindows).incrementAndGet() <= transactionsAllowed/unitOfTimeInSeconds;
		
		final double previousWindowWeight = 1 - (currentTimeStampInMilliSeconds - currentTimeStampInSecondWindows)/1000;
		final long transactionsInSlidingWindow = (long) (transactionsAllowedInPreviousWindow.get() * previousWindowWeight + 
				window.get(currentTimeStampInSecondWindows).incrementAndGet());
		return transactionsInSlidingWindow <= transactionsAllowed/unitOfTimeInSeconds;
	}

}
