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
		int[] words = new int[(int) (((long) msg.length() + (64 - msg.length() % 64)) / 4)];
		
		for (int i = 0; i < msg.length(); i++)
			words[i >>> 2] |= msg.charAt(i) << (24 - (i % 4) * 8);
		words[msg.length() >>> 2] |= 0x80 << (24 - (msg.length() % 4) * 8);
		
		for (int i = 0; i < words.length; i++)
			words[i] = Integer.reverseBytes(words[i]);
		
		words[words.length - 2] = msg.length() * 8;
		words[words.length - 1] = (int) ((msg.length() * 8) / (1L << 32)); 
		
		int a = Integer.reverseBytes(0x01234567);
		int b = Integer.reverseBytes(0x89abcdef);
		int c = Integer.reverseBytes(0xfedcba98);
		int d = Integer.reverseBytes(0x76543210);
		
		for (int i = 0; i < words.length / 16; i += 16) {
			int[] word = Arrays.copyOfRange(words, i, i + 16);

			int aa = a;
			int bb = b;
			int cc = c;
			int dd = d;

			int count = -1;
			
			for (int j = 0, inc = -1; j < 4; j++) {
				a = b + R((a + F(b, c, d) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][0]);
				d = a + R((d + F(a, b, c) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][1]);
				c = d + R((c + F(d, a, b) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][2]);
				b = c + R((b + F(c, d, a) + word[inc = ((inc + 1) % 16)] + T[count += 1] ), S[0][3]);
			}
			
			for (int j = 0, inc = -4; j < 4; j++) {
				a = b + R((a + G(b, c, d) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][0]);
				d = a + R((d + G(a, b, c) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][1]);
				c = d + R((c + G(d, a, b) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][2]);
				b = c + R((b + G(c, d, a) + word[inc = ((inc + 5) % 16)] + T[count += 1] ), S[1][3]);
			}
			
			for (int j = 0, inc = 2; j < 4; j++) {
				a = b + R((a + H(b, c, d) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][0]);
				d = a + R((d + H(a, b, c) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][1]);
				c = d + R((c + H(d, a, b) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][2]);
				b = c + R((b + H(c, d, a) + word[inc = ((inc + 3) % 16)] + T[count += 1] ), S[2][3]);
			}
			
			for (int j = 0, inc = -7; j < 4; j++) {
				a = b + R((a + I(b, c, d) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][0]);
				d = a + R((d + I(a, b, c) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][1]);
				c = d + R((c + I(d, a, b) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][2]);
				b = c + R((b + I(c, d, a) + word[inc = ((inc + 7) % 16)] + T[count += 1] ), S[3][3]);
			}
			
			a = a + aa;
			b = b + bb;
			c = c + cc;
			d = d + dd;
		}

		return String.format("%x%x%x%x", 
				Integer.reverseBytes(a),
				Integer.reverseBytes(b),
				Integer.reverseBytes(c),
				Integer.reverseBytes(d));
	}
	
	public static void main(String[] args) {
		String msg = "hello";
		
		System.out.println("MESSAGE : " + msg);
		System.out.println("DIGEST  : " + digest(msg));
	}
}