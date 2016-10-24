public class Ex01b_PlayFair {
	public static int[][] processKey(String key) {
		int[][] keyMat = new int[26][2];
		int l = 0;

		//Need to store for each character its corresponding row and column
		//First priority given to character in key and then use the rest of them
		//To avoid repeats check if the character not in key or is a character in key.
		for(char i: (key + "abcdefghiklmnopqrstuvwxyz").toCharArray()) { 
			if(key.indexOf(i + "") < 0 || l < key.length()) {
				keyMat[i - 'a'][0] = l / 5;
				keyMat[i - 'a'][1] = l++ % 5;
			}
		}

		//Make i and j have same value
		keyMat['j' - 'a'][0] = keyMat['i' - 'a'][0];
		keyMat['j' - 'a'][1] = keyMat['i' - 'a'][1];

		//Return the key matrix which stores for each character its row and column value
		return keyMat;
	}

	public static String crypt(String inputText, String key, boolean encrypt) {
		int[][] keyMat = processKey(key);

		//Now create a reverse map from row and column to character using data from keyMat
		char[][] indMat = new char[5][5];
		for(int i = keyMat.length - 1; i >= 0; i--) {
			indMat[keyMat[i][0]][keyMat[i][1]] = (char)('a' + i);
		}
		String cipherText = "";

		for(int i = 0; i < inputText.length(); i += 2) {
			//Take two characters at a time
			//If the length is not sufficient or if there is a repeat use 'x' instead of original character
			char first = inputText.charAt(i);
			char second = i + 1 == inputText.length() || first == inputText.charAt(i + 1) ?  'x' : inputText.charAt(i + 1);

			//Get row and column of the corresponding characters
			int fRow = keyMat[first - 'a'][0];
			int fCol = keyMat[first - 'a'][1];
			int sRow = keyMat[second - 'a'][0];
			int sCol = keyMat[second - 'a'][1];

			if(fRow == sRow) {
				//If row is equal then +/- one to each column
				fCol = ((fCol + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
				sCol = ((sCol + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
			} else if(fCol == sCol) {
				//If column is equal then +/- one to each row
				fRow = ((fRow + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
				sRow = ((sRow + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
			} else {
				//Otherwise swap columns
				int tCol = fCol; fCol = sCol; sCol = tCol; 
			}

			//Append the ciphered characters
			cipherText += indMat[fRow][fCol] + "" + indMat[sRow][sCol];
		}

		return cipherText;
	}

	public static void main(String[] args) {
		//Do not use x and j in text
		String plain = "karthik";
		String key = "monarchy";

		System.out.println("PLAIN  TEXT : " + plain);
		System.out.println("KEY    TEXT : " + key);
		System.out.println("CIPHER TEXT : " + crypt(plain, key, true).toUpperCase());
		System.out.println("PLAIN  TEXT : " + crypt(crypt(plain, key, true), key, false).toUpperCase());
	}
}
