import java.util.Arrays;

public class Ex07_MD5 {
	private static final int[][] S = {
		{ 7, 12, 17, 22 },
		{ 5,  9, 14, 20 },
		{ 4, 11, 16, 23 },
		{ 6, 10, 15, 21 }
	};
	private static final int[] T;
	static {
		T = new int[64];
		for(int i = 0; i < 64; i++) 
			T[i] = (int) (long) ((1L << 32) * Math.abs(Math.sin(i + 1)));
	}

	private static final int F(int x, int y, int z) { return (x & y) | (~x & z); }
	private static final int G(int x, int y, int z) { return (x & z) | (y & ~z); }
	private static final int H(int x, int y, int z) { return (x ^ y ^ z); }
	private static final int I(int x, int y, int z) { return y ^ (x | ~z); }

	private static final int R(int n, int i) { return (n << i) | (n >>> (32 - i)); }

	public static String digest(String msg) {
		//req. a bit array of length which is a multiple of 512
		//From that need to have a byte array of length which is a multiple of (512 / 8) = 64
		//So need to append remainder of bytes to the message
		//Finally a 32 bit word has 4 bytes so divide the required length by 4
		int[] words = new int[( msg.length() + (64 - msg.length() % 64)) / 4];

		// for example adding [0x01, 0x02, 0x03, 0x04] to get 0x01020304
		// word = 0x00000000 | 0x01000000 (0x01000000 = 0x01 << 24 - 0)
		// word = 0x01000000 | 0x00020000 (0x01000000 = 0x01 << 24 - 8)
		// word = 0x01020000 | 0x00000300 (0x01000000 = 0x01 << 24 - 16)
		// word = 0x01020300 | 0x00000004 (0x01000000 = 0x01 << 24 - 24) since 0x01 = 8 bytes
		// (i / 4) is done since we need to map (0,1,2,3 => 0), (4,5,6,7 => 1) and so on
		for (int i = 0; i < msg.length(); i++)
			words[i / 4] |= msg.charAt(i) << (24 - (i % 4) * 8);

		//At the end you need to add a 1 followed by zeros till the end
		//Since array would be defaulted to 0 on creation
		//Just add 0b1000000 = 0x80 to the current word
		words[msg.length() / 4] |= 0x80 << (24 - (msg.length() % 4) * 8);

		//MD5 requires operations in little endian but java is big endian
		//So reverse the bytes to get little endian representation
		for (int i = 0; i < words.length; i++)
			words[i] = Integer.reverseBytes(words[i]);

		// word[-2], word[-1] = lower 32 bit of no of bits in msg, higher 32 bits of no of bits in msg 
		words[words.length - 2] = (int) (msg.length() * 8L);
		words[words.length - 1] = (int) ((msg.length() * 8L) >>> 32);

		//Initialize the buffers and for little-endian reverse the bytes
		int a = Integer.reverseBytes(0x01234567);
		int b = Integer.reverseBytes(0x89abcdef);
		int c = Integer.reverseBytes(0xfedcba98);
		int d = Integer.reverseBytes(0x76543210);

		//Take 16 words at a time 16 * 32 = 512 bits at a time
		for (int i = 0; i < words.length / 16; i += 16) {
			int[] word = Arrays.copyOfRange(words, i, i + 16);

			//Store the values of buffers
			int aa = a;
			int bb = b;
			int cc = c;
			int dd = d;

			int count = -1;

			//[ABCD  0 7  1] [DABC  1 12  2] [CDAB  2 17  3] [BCDA  3 22  4]
			//[ABCD  4 7  5] [DABC  5 12  6] [CDAB  6 17  7] [BCDA  7 22  8]
			//[ABCD  8 7  9] [DABC  9 12 10] [CDAB 10 17 11] [BCDA 11 22 12]
			//[ABCD 12 7 13] [DABC 13 12 14] [CDAB 14 17 15] [BCDA 15 22 16]
			//pattern [ABCD, k, s, i] = [ D ABC, (k + 1) % 16, s[i % 4], i++] and k = -1 to start from 0
			for (int j = 0, inc = -1; j < 4; j++) {
				a = b + R((a + F(b, c, d) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][0]);
				d = a + R((d + F(a, b, c) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][1]);
				c = d + R((c + F(d, a, b) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][2]);
				b = c + R((b + F(c, d, a) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][3]);
			}

			//[ABCD  1 5 17] [DABC  6 9 18] [CDAB 11 14 19] [BCDA  0 20 20]
			//[ABCD  5 5 21] [DABC 10 9 22] [CDAB 15 14 23] [BCDA  4 20 24]
			//[ABCD  9 5 25] [DABC 14 9 26] [CDAB  3 14 27] [BCDA  8 20 28]
			//[ABCD 13 5 29] [DABC  2 9 30] [CDAB  7 14 31] [BCDA 12 20 32]
			//pattern [ABCD, k, s, i] = [D ABC, (k + 5) % 16, s[i % 4], i++] and k = -4 to start from 5
			for (int j = 0, inc = -4; j < 4; j++) {
				a = b + R((a + G(b, c, d) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][0]);
				d = a + R((d + G(a, b, c) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][1]);
				c = d + R((c + G(d, a, b) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][2]);
				b = c + R((b + G(c, d, a) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][3]);
			}

			//[ABCD  5 4 33] [DABC  8 11 34] [CDAB 11 16 35] [BCDA 14 23 36]
			//[ABCD  1 4 37] [DABC  4 11 38] [CDAB  7 16 39] [BCDA 10 23 40]
			//[ABCD 13 4 41] [DABC  0 11 42] [CDAB  3 16 43] [BCDA  6 23 44]
			//[ABCD  9 4 45] [DABC 12 11 46] [CDAB 15 16 47] [BCDA  2 23 48]
			//pattern [ABCD, k, s, i] = [D ABC, (k + 3) % 16, s[i % 4], i++] and k = 2 to start from 5
			for (int j = 0, inc = 2; j < 4; j++) {
				a = b + R((a + H(b, c, d) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][0]);
				d = a + R((d + H(a, b, c) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][1]);
				c = d + R((c + H(d, a, b) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][2]);
				b = c + R((b + H(c, d, a) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][3]);
			}

			//[ABCD  0 6 49] [DABC  7 10 50] [CDAB 14 15 51] [BCDA  5 21 52]
			//[ABCD 12 6 53] [DABC  3 10 54] [CDAB 10 15 55] [BCDA  1 21 56]
			//[ABCD  8 6 57] [DABC 15 10 58] [CDAB  6 15 59] [BCDA 13 21 60]
			//[ABCD  4 6 61] [DABC 11 10 62] [CDAB  2 15 63] [BCDA  9 21 64]
			//pattern [ABCD, k, s, i] = [D ABC, (k + 7) % 16, s[i % 4], i++] and k = -7 to start from 0
			for (int j = 0, inc = -7; j < 4; j++) {
				a = b + R((a + I(b, c, d) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][0]);
				d = a + R((d + I(a, b, c) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][1]);
				c = d + R((c + I(d, a, b) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][2]);
				b = c + R((b + I(c, d, a) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][3]);
			}

			//Add the buffers and stored values
			a = a + aa;
			b = b + bb;
			c = c + cc;
			d = d + dd;
		}

		//For our understanding convert little-endian to big-endian
		return String.format("%x%x%x%x", 
				Integer.reverseBytes(a),
				Integer.reverseBytes(b),
				Integer.reverseBytes(c),
				Integer.reverseBytes(d));
	}

	public static void main(String[] args) {
		String msg = "karthik";

		System.out.println("MESSAGE : " + msg);
		System.out.println("DIGEST  : " + digest(msg));
	}
}