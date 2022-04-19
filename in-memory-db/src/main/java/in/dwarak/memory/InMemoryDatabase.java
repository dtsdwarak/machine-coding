package in.dwarak.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import lombok.NonNull;


/**
 * @author dwarak
 *
 */
@Service
public class InMemoryDatabase implements Database {

	private Map<String, Map<String, Object>> keyValueStore;
	private Map<String, Class<?>> attributeTypeMap;
	
	@Override
	public void put(final String key, final Map<String, Object> attributes) {
		
		verifyInput(key, attributes);
		
		keyValueStore.put(key, attributes);
		
		attributes.entrySet().stream()
		.filter(entry -> attributeTypeMap.get(entry.getKey()) == null)
		.forEach(attribute -> 
		attributeTypeMap.put(
				attribute.getKey(), 
				fetchClassTypeForAttributeValue(attribute.getValue())));
	}

	/**
	 * Verify the inputs
	 * 
	 * @param key
	 * @param attributes
	 */
	private void verifyInput(final String key, final Map<String, Object> attributes) {
		
		Preconditions.checkArgument(key != null, "Null key passed");
		Preconditions.checkArgument(MapUtils.isNotEmpty(attributes), "AttrbuteMap is Empty");
		
		final Optional<Boolean> foundAny = attributes.entrySet().stream().map(entry -> {
			if (attributeTypeMap.get(entry.getKey()) != null) {
				final Class<?> classType = attributeTypeMap.get(entry.getKey());
				if (classType.isInstance(entry.getValue()))
					return true;
				else
					return false;
			}
			return true;
		}).filter(x -> x == false).findAny();
		final Boolean foundResult = Optional.ofNullable(foundAny).filter(x -> x.isPresent()).map(x -> x.get()).orElse(true);
		
		Preconditions.checkArgument(foundResult, "Type validation failed");
	}
	
	private Class<?> fetchClassTypeForAttributeValue(final Object attributeValue) {
		
		if (Boolean.class.isInstance(attributeValue))
			return Boolean.class;
		else if (Double.class.isInstance(attributeValue))
			return Double.class;
		else if (Integer.class.isInstance(attributeValue))
			return Integer.class;
		else if (String.class.isInstance(attributeValue))
			return String.class;
		else
			throw new IllegalArgumentException("Type attribution not possible");
	}

	@Override
	public Optional<Map<String, Object>> get(String key) {
		
		return Optional.ofNullable(keyValueStore.get(key));
	}

	@Override
	public void delete(String key) {
		
		final Set<String> attributesForKey = keyValueStore.get(key).keySet();
		final Set<String> attributeKeysNotContainedAnywhere = keyValueStore
		.entrySet()
		.stream()
		.filter(entry -> entry.getKey() != key)
		.flatMap(entry -> entry.getValue().keySet().stream())
		.filter(attributeKey -> !attributesForKey.contains(attributeKey))
		.collect(Collectors.toSet());
		
		keyValueStore.remove(key);
		attributeKeysNotContainedAnywhere.stream().forEach(x -> attributeTypeMap.remove(x));
	}

	@Override
	public List<String> stretch(String attributeKey, @NonNull String attributeValue) {
		
		return keyValueStore
		.entrySet()
		.stream()
		.filter(entry -> entry.getValue().get(attributeKey) == attributeValue)
		.map(entry -> entry.getKey())
		.collect(Collectors.toUnmodifiableList());
		
	}

	@Override
	public void initialize() {
		keyValueStore = new HashMap<>();
		attributeTypeMap = new HashMap<>();
	}
	
	
	/**
	 * Package private method
	 * 
	 * @return
	 */
	int fetchNumberOfAttributesAvailable() {
		if (keyValueStore == null)
			return 0;
		return keyValueStore.size();
	}
	

}
