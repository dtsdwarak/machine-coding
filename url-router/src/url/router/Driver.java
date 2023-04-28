package url.router;

public class Driver {

	public static void main(String[] args) {
	
		Router router = new RouterImpl();
		router.add("abc.com/bar", "foo1");
		System.out.println(router.get("abc.com/*"));
		System.out.println(router.get("abc.com/*/*"));
		
		router.withRoute("abc.com/bar/abc", "foo2");
		System.out.println(router.route("abc.com/bar/*"));
		System.out.println(router.route("abc.com/bar/abc"));
		
		router.add("abc.com/bar/abc/dd", "foo3");
		router.add("abc.com/abc/abc/cba/dd", "foo4");
		System.out.println(router.route("abc.com/bar/abc/dd"));
		System.out.println(router.route("abc.com/abc/*/dd"));
		System.out.println(router.route("abc.com/abc/*/dd1"));
		
		

	}

}
