package in.dwarak.memory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

class InMemoryDatabaseTest {
	
	private InMemoryDatabase database;
	
	@BeforeEach
	void setUp() throws Exception {
		database = new InMemoryDatabase();
		database.initialize();
	}
	
	@Test
	void testInitialize() {
		assertTrue(database.fetchNumberOfAttributesAvailable() == 0);
	}

	@Test
	void testPut() {
		final Map<String, Object> attributeValue = ImmutableMap.of("pollution_level", "very high", "population", "10 Million");
		database.put("Delhi", attributeValue);
	}
	
	@Test
	void testPut_whenNullKeyIsPassed_expectException() {
		final Map<String, Object> attributeValue = ImmutableMap.of("pollution_level", "very high", "population", "10 Million");
		assertThrows(IllegalArgumentException.class, () -> database.put(null, attributeValue));
	}

	@Test
	void testPut_whenNullValueIsPassed_expectException() {
		assertThrows(IllegalArgumentException.class, () -> database.put("Delhi", null));
	}
	
	@Test
	void testPut_whenTypeOfAVariableIsAlreadySet_verifyIfExceptionIsThrownWhenAnotherTypeIsIntroduced() {
		final Map<String, Object> attributeValue = ImmutableMap.of("pollution_level", "very high", "population", "10 Million");
		database.put("Delhi", attributeValue);
		final Map<String, Object> attributeValueNext = ImmutableMap.of("pollution_level", 1.0, "population", "10 Million");
		assertThrows(IllegalArgumentException.class, () -> database.put("Chennai", attributeValueNext));
	}


	@Test
	void testDelete() {
		final Map<String, Object> attributeValue = ImmutableMap.of("pollution_level", "very high", "population", "10 Million");
		database.put("Delhi", attributeValue);
		database.delete("Delhi");
	}

	@Test
	void testStretch() {
		
		final Map<String, Object> attributeValue = ImmutableMap.of("pollution_level", "very high", "population", "10 Million");
		final Map<String, Object> attributeValueNext = ImmutableMap.of("pollution_level", "very high", "hdi", "11.0");
		database.put("Delhi", attributeValue);
		database.put("Mumbai", attributeValueNext);
		
		final List<String> response = database.stretch("pollution_level", "very high");
		equals(Arrays.asList("Delhi", "Mumbai") == response);
		
		
	}



}
