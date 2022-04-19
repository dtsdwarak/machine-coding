package in.dwarak.memory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.NonNull;

public interface Database {

	void initialize();
	
	void put(String key, Map<String, Object> attributes);
	
	Optional<Map<String, Object>> get(String key);
	
	void delete(String key);
	
	List<String> stretch(String attributeKey, @NonNull String attributeValue);
	
}
