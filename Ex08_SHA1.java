public class Ex08_SHA1 {

	private static int R(int n, int i) { return (n << i) | (n >>> (32 - i)); }

	public static String digest(String msg) {
		//req. a bit array of length which is a multiple of 512
		//From that need to have a byte array of length which is a multiple of (512 / 8) = 64
		//So need to append remainder of bytes to the message
		//Finally a 32 bit word has 4 bytes so divide the required length by 4
		int[] words = new int[(msg.length() + (64 - msg.length() % 64)) / 4];

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
		//Store the message length in the last word
		words[msg.length() / 4] |= 0x80 << (24 - (msg.length() % 4) * 8);
		words[words.length - 1] = msg.length() * 8;

		//This is the word for each round 
		// w[i] = w[i] if i < 16
		// w[i] = R(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1) otherwise
		int[] w = new int[80];

		//Initialize the md buffers
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
				//Here i represents ith block and j represents jth word in the ith block
				w[j] = (j < 16) ? words[i + j] : R(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);

				//Perform digest operations and shift the variables
				//e, d, c, b, a = d, c, R(b, 30), a, t
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

			//Add the values to the md buffers
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