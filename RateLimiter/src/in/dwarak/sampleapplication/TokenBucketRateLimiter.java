package in.dwarak.sampleapplication;

import java.util.concurrent.TimeUnit;

public class TokenBucketRateLimiter extends RateLimiter {
	
	private int tokens;

	public TokenBucketRateLimiter(int transactionsAllowed, int unitOfTimeInSeconds) {
		super(transactionsAllowed, unitOfTimeInSeconds);
		this.tokens = (int) transactionsAllowed/unitOfTimeInSeconds;
		new Thread(() -> {
			while(true) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException exception) {
					exception.printStackTrace();
				}
				refillTokens(transactionsAllowed/unitOfTimeInSeconds);
			}
		}).start();
	}

	@Override
	boolean rateLimit() {
		synchronized(this) {
			if (tokens == 0) {
				return false;
			}
			tokens--;
			return true;
		}
	}
	
	private void refillTokens(int count) {
		synchronized(this) {
			tokens = Math.min(tokens+count, transactionsAllowed/unitOfTimeInSeconds);
			notifyAll();
		}
	}

}
