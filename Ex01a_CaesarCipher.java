public class Ex01a_CaesarCipher {

	public static String crypt(String input, int key, boolean encrypt) {
		StringBuilder cipher = new StringBuilder("");
		for (char i: input.toCharArray()) {
			cipher.append((char)(((i - 'a' + (encrypt ? 1 : -1) * key) % 26 + 26) % 26 + 'a'));
		}
		return cipher.toString();
	}
	
	public static void main(String[] args) {
		String plain = "hello";
		int key = 20;

    	System.out.println("PLAIN  TEXT : " + plain);
    	System.out.println("KEY    TEXT : " + key);
		System.out.println("CIPHER TEXT : " + crypt(plain, key, true).toUpperCase());
		System.out.println("PLAIN  TEXT : " + crypt(crypt(plain, key, true), key, false).toUpperCase());
	}
}