public class Ex03a_RailFence {
	public static String crypt(String msg, int key, boolean encrypt) {
		char[] res = new char[msg.length()];

		for (int i = 0, k = 0; i < key; i++) {
			int inc = 2 * (key - i - 1);

			//format to take chars is j....(j + inc)....(j + 2 * (key - 1))
			//	k-----k   	j............(j+2*key-1)            
			//	-a---i-m  	j...(j+inc)..(j+2*key-1)
			//	--r-h---a 	j...(j+inc)..(j+2*key-1)            
			//	---t-----m 	j...(j+2*key-1)     
			for (int j = i; j < msg.length(); j += 2 * (key - 1)) {
				res[encrypt ? k++ : j] = msg.charAt(encrypt ? j : k++);
				if (i != key - 1 && i != 0 && (j + inc) < msg.length()) 
					res[encrypt ? k++ : j + inc] = msg.charAt(encrypt ? j + inc : k++);
			}
		}

		return new String(res);
	}

	public static void main(String[] args) {
		String plain = "karthikmam";
		int key = 4;

		System.out.println("PLAIN  TEXT : " + plain);
		System.out.println("KEY    TEXT : " + key);
		System.out.println("CIPHER TEXT : " + crypt(plain, key, true).toUpperCase());
		System.out.println("PLAIN  TEXT : " + crypt(crypt(plain, key, true), key, false).toUpperCase());
	}
}
