import java.math.BigInteger;

public class Ex02a_HillCipher {
	private static int coFact(int[][] mat) {
		return mat[0][0] * mat[1][1] - mat[1][0] * mat[0][1];
	}

	private static int[][] adjMat(int[][] mat) {
		int[][] adj = new int[3][3];

		//Find the matrix of co-factors in transposed state
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				adj[j][i] = coFact(new int[][]{
						{mat[(i + 1) % 3][(j + 1) % 3], mat[(i + 1) % 3][(j + 2) % 3]}, 
						{mat[(i + 2) % 3][(j + 1) % 3], mat[(i + 2) % 3][(j + 2) % 3]}
				});

		return adj;
	}

	private static int[][] invMat(int[][] mat) {
		int[][] adj = mat.length == 3 ? adjMat(mat) : new int[][] {
				{mat[1][1], -mat[1][0]},
				{-mat[0][1], mat[0][0]}
		};

		// det is the actual determinant mod inverse 26
		int det = 0;
		for (int i = 0; i < mat.length; i++) det += mat[0][i] * adj[i][0];
		det = (new BigInteger(det + "")).modInverse(new BigInteger("26")).intValue();

		// inv[i][j] = adj[i][j] * invDet mod 26
		int [][] inv = new int[adj.length][adj[0].length];
		for (int i = 0; i < mat.length; i++)
			for (int j = 0; j < mat[i].length; j++) {
				inv[i][j] = ((adj[i][j] * det) % 26 + 26) % 26;
			}

		return inv;
	}

	private static int[][] matMult(int[][] a, int[][] b) {
		int[][] c = new int[a.length][b[0].length];

		//multiply matrix in n^3 fashion
		for (int i = 0; i < c.length; i++) 
			for (int j = 0; j < c[i].length; j++) {
				for (int k = 0; k < a.length; k++)
					c[i][j] += (a[i][k] * b[k][j]);
				c[i][j] = (c[i][j] % 26 + 26) % 26; 
			}

		return c;
	}

	private static String crypt(String plain, String key, boolean encrypt) {
		int[][] keyMat = new int[key.length() == 9 ? 3 : 2][key.length() == 9 ? 3 : 2];
		int[][] texMat = new int[keyMat.length][plain.length() / keyMat.length + (plain.length() % keyMat.length % 2)];

		//Form the key matrix in 0 - 26 indexed a - z
		for (int i = 0; i < keyMat.length; i++)
			for (int j = 0; j < keyMat[i].length; j++)
				keyMat[i][j] = key.charAt(i * keyMat[i].length + j) - 'a';

		//Form text matrix in a 2 or 3 rowed matrix
		for (int i = 0; i < texMat[0].length; i++) 
			for (int j = 0; j < texMat.length; j++)
				texMat[j][i] = (i * texMat.length + j < plain.length() ? plain.charAt(i * texMat.length + j) : 'x') - 'a';

		//multiply the key and text for encryption
		//multiply the invKey and text for decryption
		texMat = matMult((encrypt ? keyMat : invMat(keyMat)), texMat);

		//Map 0 - 26 indexed matrix to a - z string
		char[] res = new char[texMat.length * texMat[0].length];
		for (int i = 0; i < texMat[0].length; i++) 
			for (int j = 0; j < texMat.length; j++) {
				res[i * texMat.length + j] = (char) (texMat[j][i] + 'a');
			} 

		return new String(res);
	}

	public static void main(String[] args) {	

		String key = "gybnqkurp", plain = "karthik";

		System.out.println("KEY   : " + key);
		System.out.println("PLAIN : " + plain);
		System.out.println("CRYPT : " + crypt(plain, key, true));
		System.out.println("PLAIN : " + crypt(crypt(plain, key, true), key, false));
	}

}
