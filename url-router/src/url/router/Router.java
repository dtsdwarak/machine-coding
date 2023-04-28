package url.router;

interface Router {

	public void add (String route, String data);
	public void withRoute (String route, String data);
	public String get(String route);
	public String route(String route);
	
}
