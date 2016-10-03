public class Ex08_SHA1 {

	private static int R(int n, int i) {
		return (n << i) | (n >>> (32 - i));
	}

	public static String digest(String msg) {
		int[] words = new int[(int) (((long) msg.length() + (64 - msg.length() % 64)) / 4)];
		
		for (int i = 0; i < msg.length(); i++)
			words[i >>> 2] |= msg.charAt(i) << (24 - (i % 4) * 8);
		
		words[msg.length() >>> 2] |= 0x80 << (24 - (msg.length() % 4) * 8);
		words[words.length - 1] = msg.length() * 8;

		int[] w = new int[80];

		int h0 = Integer.reverseBytes(0x01234567);
		int h1 = Integer.reverseBytes(0x89abcdef);
		int h2 = Integer.reverseBytes(0xfedcba98);
		int h3 = Integer.reverseBytes(0x76543210);
		int h4 = Integer.reverseBytes(0xf0e1d2c3);

		for (int i = 0; i < words.length; i += 16) {
			int a = h0;
			int b = h1;
			int c = h2;
			int d = h3;
			int e = h4;

			for (int j = 0; j < 80; j++) {
				w[j] = (j < 16) ? words[i + j] : (R(w[j - 3]
						^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1));

				int t = R(a, 5) + e + w[j] + 
						( j < 20 ? 	(0x5a827999 + ((b & c) | ((~b) & d))) 
						: j < 40 ? 	(0x6ed9eba1 + (b ^ c ^ d)) 
						: j < 60 ? 	(0x8f1bbcdc + ((b & c) | (b & d) | (c & d))) 
						: 			(0xca62c1d6 + (b ^ c ^ d)));
				e = d;
				d = c;
				c = R(b, 30);
				b = a;
				a = t;
			}

			h0 += a;
			h1 += b;
			h2 += c;
			h3 += d;
			h4 += e;
		}

		return String.format("%x%x%x%x%x", h0, h1, h2, h3, h4);
	}

	public static void main(String args[]) {
		String msg = "hello";
		
		System.out.println("MESSAGE : " + msg);
		System.out.println("DIGEST  : " + digest(msg));
	}
}