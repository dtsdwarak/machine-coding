package in.dwarak.sampleapplication;

public class LeakyBucketRateLimiter extends RateLimiter {
	
	private long nextAllowedTimeStampInSecondWindows;

	public LeakyBucketRateLimiter(int transactionsAllowed, int unitOfTimeInSeconds) {
		super(transactionsAllowed, unitOfTimeInSeconds);
		nextAllowedTimeStampInSecondWindows = System.currentTimeMillis()/1000*1000;
	}

	@Override
	boolean rateLimit() {
		final long currentTimeStampInSecondWindows = System.currentTimeMillis();
		synchronized(this) {
			if (currentTimeStampInSecondWindows >= nextAllowedTimeStampInSecondWindows) {
				nextAllowedTimeStampInSecondWindows = currentTimeStampInSecondWindows + (transactionsAllowed / unitOfTimeInSeconds) * 1000;
				return true;
			}
			return false;
		}
	}

}
