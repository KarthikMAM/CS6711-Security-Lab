public class Ex02b_Vigenere {
	public static String cryptic(String input, String key, boolean encrypt) {
        StringBuilder output = new StringBuilder(""); 
        
        int j = 0;
        for(char i: input.toCharArray()) {
            output.append((char)(((i - 'a' + (encrypt ? key.charAt(j) - 'a' : -key.charAt(j) + 'a')) % 26 + 26) % 26 + 'a'));
            j = (j + 1) % key.length();
        }
        return output.toString();
    }
    
    public static void main(String[] args) {
    	
    	String plain = "karthik", key = "hello";
    	
    	System.out.println("PLAIN  TEXT : " + plain);
    	System.out.println("KEY    TEXT : " + key);
        System.out.println("CIPHER TEXT : " + cryptic(plain, key, true));
        System.out.println("PLAIN  TEXT : " + cryptic(cryptic(plain, key, true), key, false));
    }
}
