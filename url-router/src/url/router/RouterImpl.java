package url.router;

import java.util.HashMap;
import java.util.Map;

public class RouterImpl implements Router {
	
	static class TrieNode {
		
		
		Map<String, TrieNode> children;
		String value;
		boolean endOfWord;
			
		public TrieNode() {
			
			children = new HashMap<>();
			value = "";
			endOfWord = false;
			
		}
		
	}
	
	static TrieNode root = new TrieNode();

	@Override
	public void add(String route, String data) {
		
		if (route.isEmpty()) {
			throw new RuntimeException("route should not be empty");
		}
			
		TrieNode crawl = root;
		String[] urlComponents = route.split("/");
		
		for(String component: urlComponents) {
			
			if (!crawl.children.containsKey(component)) {
				crawl.children.put(component, new TrieNode());
			}
			crawl = crawl.children.get(component);
		}
		
		crawl.endOfWord = true;
		crawl.value = data;
		
	}
	
	@Override
	public void withRoute(String path, String result) {
		this.add(path, result);
	}
	
	private String searchTrie(String[] urlComponents, int index, TrieNode node) {
		
		int size = urlComponents.length;
		if (index == size) {
			
			if (node != null && node.endOfWord) {
				return node.value;
			}
			else {
				return null;
			}
		}
		TrieNode crawl = node;
		
		String component = urlComponents[index];
		
		if ("*".equals(component)) {
			String result = null;
			for(Map.Entry<String, TrieNode>entry:crawl.children.entrySet()) {
				TrieNode temp = entry.getValue();
				result = searchTrie(urlComponents, index + 1, temp);
				if (result != null) {
					return result;
				}
			}
			return result;
		}
		else if (crawl != null && crawl.children != null && crawl.children.containsKey(component)) {
			crawl = crawl.children.get(component);
			String result = searchTrie(urlComponents, index+1, crawl);
			return result;
		}
		else {
			return null;
		}
		
		
	}

	@Override
	public String get(String route) {
		
		if (route.isEmpty()) {
			throw new RuntimeException("route should not be empty");
		}
		
		TrieNode crawl = root;
		String[] urlComponents = route.split("/");
		String result = searchTrie(urlComponents, 0, root);
		if (result == null) {
			result = "notFound";
		}
		return result;
	}
	
	@Override
	public String route(String path) {
		return this.get(path);
	}

}
