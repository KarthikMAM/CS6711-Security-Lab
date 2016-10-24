import java.math.BigInteger;
import java.util.Arrays;


public class Ex04_DESAlt {

	private static final int[] PC1 = { 
		57, 49, 41, 33, 25, 17,  9, 
		 1, 58, 50, 42, 34, 26, 18,
		10,  2, 59, 51, 43, 35, 27, 
		19, 11,  3, 60, 52, 44, 36, 
		63, 55, 47, 39, 31, 23, 15, 
		 7, 62, 54, 46, 38, 30, 22, 
		14,  6, 61, 53, 45, 37, 29, 
		21, 13,  5, 28, 20, 12,  4 };
	private static final int[] PC2 = { 
		14, 17, 11, 24,  1,  5, 
		 3, 28, 15,  6, 21, 10, 
		23, 19, 12,  4, 26,  8, 
		16,  7, 27, 20, 13,  2, 
		41, 52, 31, 37, 47, 55, 
		30, 40, 51, 45, 33, 48, 
		44, 49, 39, 56, 34, 53, 
		46, 42, 50, 36, 29, 32 };
	private static final int[] R = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
	private static final int[] IP = { 
		58, 50, 42, 34, 26, 18, 10, 2, 
		60, 52, 44, 36, 28, 20, 12, 4, 
		62, 54, 46, 38, 30, 22, 14, 6, 
		64, 56, 48, 40, 32, 24, 16, 8, 
		57, 49, 41, 33, 25, 17,  9, 1, 
		59, 51, 43, 35, 27, 19, 11, 3, 
		61, 53, 45, 37, 29, 21, 13, 5, 
		63, 55, 47, 39, 31, 23, 15, 7 };
	private static int[] IP_1 = { 
		40, 8, 48, 16, 56, 24, 64, 32, 
		39, 7, 47, 15, 55, 23, 63, 31,
		38, 6, 46, 14, 54, 22, 62, 30, 
		37, 5, 45, 13, 53, 21, 61, 29, 
		36, 4, 44, 12, 52, 20, 60, 28, 
		35, 3, 43, 11, 51, 19, 59, 27, 
		34, 2, 42, 10, 50, 18, 58, 26, 
		33, 1, 41,  9, 49, 17, 57, 25 };
	private static final int[] E = { 
		32,  1,  2,  3,  4,  5, 
		 4,  5,  6,  7,  8,  9, 
		 8,  9, 10, 11, 12, 13, 
		12, 13, 14, 15, 16, 17, 
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25, 
		24, 25, 26, 27, 28, 29, 
		28, 29, 30, 31, 32,  1 };
	private static long[][] S = {
		{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 },
		{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 },
		{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 },
		{  7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 },
		{  2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 },
		{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 },
		{  4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 },
		{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };
	private static int[] P = { 
		16,  7, 20, 21, 
		29, 12, 28, 17, 
		 1, 15, 23, 26,
		 5, 18, 31, 10, 
		 2,  8, 24, 14, 
		32, 27,  3,  9, 
		19, 13, 30,  6, 
		22, 11,  4, 25 };


	private static String mutate(char[] input, int[] table) {
		char[] res = new char[table.length];

		//Using the table store the bits at respective positions
		for (int i = 0; i < table.length; i++) 
			res[i] = input[table[i] - 1];

		return new String(res);
	}

	private static String toBinString(long value, int nBytes) {
		char[] res = new char[nBytes]; Arrays.fill(res, '0');

		//Convert to binary string by following division remainder method
		for (int i = nBytes - 1; i >= 0 && value != 0; i--, value = value >>> 1) 
			res[i] = (value % 2 + 2) % 2 == 0 ? '0' : '1';

		return new String(res);
	}

	private static String xor(String a, String b) {
		char[] res = new char[a.length()];

		//Perform xor operation on each bit
		for (int i = 0; i < a.length(); i++) 
			res[i] = a.charAt(i) != b.charAt(i) ? '1' : '0';

		return new String(res);
	}

	private String[] keys = new String[16];
	public Ex04_DESAlt(String key) {
		//Use PC1 to get the 56 bit key
		String keyStr = mutate(key.toCharArray(), PC1);

		//Split into 2 halves of 28 bits each and use R table to left rotate req. times
		String c = keyStr.substring(0, keyStr.length() / 2);
		String d = keyStr.substring(keyStr.length() / 2, keyStr.length());
		for (int i = 0; i < 16; i++) {
			//c = right of r[i] and left of r[i] in string c
			//d = right of r[i] and left of r[i] in string d
			//In sense it symbolizes left rotate by r[i] times
			c = c.substring(R[i], c.length()) + c.substring(0, R[i]);
			d = d.substring(R[i], d.length()) + d.substring(0, R[i]);
			keys[i] = mutate((c + d).toCharArray(), PC2);
		}
	}

	public static String f(String r, String key) {
		//Expanding the r using E Table from 32 bit to 64 bits
		//XOR result with keys
		r = xor(mutate(r.toCharArray(), E), key);

		String res = "";
		for (int i = 0; i < 8; i++) {
			//Use 6 bits at a time
			String b = r.substring(i * 6, i * 6 + 6);

			//Find row and column (012345) row = 05 col = 1234 bits
			int row = Integer.parseInt(b.charAt(0) + "" + b.charAt(5), 2);
			int col = Integer.parseInt(b.substring(1, 5), 2);
			res = res + toBinString(S[i][row * 16 + col], 4);
		}

		//Permutate the result and return it
		return mutate(toBinString(Long.parseLong(res, 2), 32).toCharArray(), P);
	}

	public String crypt(String msg, boolean encrypt) {
		//Perform initial permutation using IP table
		msg = mutate(msg.toCharArray(), IP);

		//Split 64 bits into l(32) and r(32) bits
		//For 16 rounds repeat the operation
		String l = msg.substring(0, msg.length() / 2);
		String r = msg.substring(msg.length() / 2, msg.length());
		for (int i = 0; i < 16; i++) {
			String temp = r;
			r = xor(l, f(r, keys[encrypt ? i : 16 - i - 1]));
			l = temp;
		}

		//Perform inverse of the permutation using IP_INV matrix
		return mutate((r + l).toCharArray(), IP_1);
	}

	public static void main(String[] args) {
		String msg = toBinString(new BigInteger("Karthik".getBytes()).longValue(), 64);
		String key = toBinString(0x133457799BBCDFF1L, 64);

		Ex04_DESAlt x = new Ex04_DESAlt(key);

		System.out.println("PLAIN  : 0b" + msg);
		System.out.println("CIPHER : 0b" + x.crypt(msg, true));
		System.out.println("PLAIN  : 0b" + x.crypt(x.crypt(msg, true), false));
	}

}