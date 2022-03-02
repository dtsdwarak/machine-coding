package in.dwarak.sampleapplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RateLimiterApplication {
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println(getCurrentTime());
				
		Driver driver = new Driver();
		driver.checkRequestAllowed("user1", getCurrentTime());
		Thread.sleep(995);
		
		for(int i=0; i<10; i++) {
			driver.checkRequestAllowed("user1", getCurrentTime());
		}
		
		Thread.sleep(500);
		System.out.println("");
		
		for(int i=0; i<10; i++) {
			driver.checkRequestAllowed("user2", getCurrentTime());
		}
		
		
	}
	
	static String getCurrentTime() {
		final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		return LocalDateTime.now().format(timeFormatter);
	}

}
