public class Ex01a_CaesarCipher {

	public static String crypt(String input, int key, boolean encrypt) {
		//Using string builder lets us manipulate strings at the character level
		StringBuilder cipher = new StringBuilder("");

		//For each character enforce the cipher algorithm
		//First subtract 'a' from the original character to get 0 - 25 index for a - z 
		//For encryption add the key and for decryption subtract the key
		//Now we need to round it up so do (result % 26 + 26) % 26
		for (char i: input.toCharArray()) {
			cipher.append((char)(((i - 'a' + (encrypt ? 1 : -1) * key) % 26 + 26) % 26 + 'a'));
		}

		//Convert to string and return the cipher
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