import java.util.Arrays;

public class Ex03b_RowColumn {
	public static String crypt(String msg, int[] key, boolean encrypt) {
		char[] res = new char[msg.length()];

		//for each column in the key iterate adding no. of columns
		//Swap the sides for encryption and decrytion
		for (int i = 0, k =0; i < key.length; i++)
			for (int j = key[i]; j < msg.length(); j += key.length)
				res[encrypt ? k++ : j] = msg.charAt(encrypt ? j : k++);

		return new String(res);
	}

	public static void main(String[] args) {

		String plain = "KARTHIKMAM";
		int[] key = {1, 2, 0};

		System.out.println("PLAIN  TEXT : " + plain);
		System.out.println("KEY    TEXT : " + Arrays.toString(key));
		System.out.println("CIPHER TEXT : " + crypt(plain, key, true));
		System.out.println("PLAIN  TEXT : " + crypt(crypt(plain, key, true), key, false));


	}

}
