package in.dwarak.inmemorydb;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import in.dwarak.config.Config;
import in.dwarak.memory.Database;
import in.dwarak.memory.InMemoryDatabase;

@SpringBootApplication
public class InMemoryDbApplication {
	
	public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        final Database database = context.getBean(InMemoryDatabase.class);
        database.initialize();
        context.close();
	}
	

}
