package ua.rozipp;

public class Main {

	static public MyProperties my;
	static public Window window;
	static public WebModul webModul;
	
	public static void main(String[] args) {
		try {
			my = new MyProperties();
			window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
