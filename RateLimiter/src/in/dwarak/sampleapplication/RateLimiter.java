package in.dwarak.sampleapplication;

public abstract class RateLimiter {
	
	final int transactionsAllowed;
	final int unitOfTimeInSeconds;
	
	protected RateLimiter(int transactionsAllowed, int unitOfTimeInSeconds) {
		this.transactionsAllowed = transactionsAllowed;
		this.unitOfTimeInSeconds = unitOfTimeInSeconds;
	}
	
	abstract boolean rateLimit();

}
