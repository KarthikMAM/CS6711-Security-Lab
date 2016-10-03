public class Ex01b_PlayFair {
	public static int[][] processKey(String key) {
        int[][] keyMat = new int[26][2];
        
        int l = 0;
        for(char i: (key + "abcdefghiklmnopqrstuvwxyz").toCharArray()) {
            if(key.indexOf(i + "") < 0 || l < key.length()) {
                keyMat[i - 'a'][0] = l / 5;
                keyMat[i - 'a'][1] = l++ % 5;
                if (i == 'i') {
                    keyMat[i - 'a' + 1][0] = l / 5;
                    keyMat[i - 'a' + 1][1] = l % 5;
                }
            }
        }
        
        return keyMat;
    }
    
    public static String crypt(String inputText, String key, boolean encrypt) {
        int[][] keyMat = processKey(key);
        char[][] indMat = new char[5][5];
        for(int i = 0; i < keyMat.length; i++) {
            indMat[keyMat[i][0]][keyMat[i][1]] = (char)('a' + i);
        }
        String cipherText = "";
        
        for(int i = 0; i < inputText.length(); i+=2) {
            char first = inputText.charAt(i);
            char second = i + 1 == inputText.length() || first == inputText.charAt(i + 1) ?  'x' : inputText.charAt(i + 1);
            
            int fRow = keyMat[first - 'a'][0];
            int fCol = keyMat[first - 'a'][1];
            int sRow = keyMat[second - 'a'][0];
            int sCol = keyMat[second - 'a'][1];
            
            
            if(fRow == sRow) { 
            	fCol = ((fCol + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
            	sCol = ((sCol + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
            } else if(fCol == sCol) { 
            	fRow = ((fRow + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
            	sRow = ((sRow + (encrypt ? 1 : -1)) % 5 + 5) % 5; 
            } else { int tCol = fCol; fCol = sCol; sCol = tCol; }
            
            cipherText += (indMat[fRow][fCol]) + "" + (indMat[sRow][sCol]);
        }
        
        return cipherText;
    }
    
    public static void main(String[] args) {
        String plain = "karthik";
        String key = "monarchy";

    	System.out.println("PLAIN  TEXT : " + plain);
    	System.out.println("KEY    TEXT : " + key);
        System.out.println("CIPHER TEXT : " + crypt(plain, key, true).toUpperCase());
        System.out.println("PLAIN  TEXT : " + crypt(crypt(plain, key, true), key, false).toUpperCase());
    }
}
