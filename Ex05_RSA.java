import java.math.BigInteger;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class Ex05_RSA {

	private static int bitLength = 128;
	private BigInteger n, e, d;
	
	public Ex05_RSA() {
		Random rnd = new Random();
		
		BigInteger p = BigInteger.probablePrime(bitLength, rnd);
		BigInteger q = BigInteger.probablePrime(bitLength, rnd);
		
		this.n = p.multiply(q);
		BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		
		this.e = BigInteger.probablePrime(bitLength / 2, rnd);
		while( e.gcd(phi).compareTo(BigInteger.ONE) == 1 && e.compareTo(phi) < 1 ) {
			e.add(BigInteger.ONE);
		}
		this.d = e.modInverse(phi);
		
		System.out.println("E : " + e);
		System.out.println("D : " + d);
		System.out.println("N : " + n);
		System.out.println();
	}
	
	public byte[] crypt(byte[] input, boolean encrypt) {
		return new BigInteger(input).modPow(encrypt ? e : d, n).toByteArray();
	}
	
	public static void main(String[] args) {
		Ex05_RSA rsa = new Ex05_RSA();
		String plain = "hello";
		
		System.out.println("PLAIN TEXT  : " + plain);
		System.out.println("CIPHER TEXT : " + DatatypeConverter.printHexBinary(rsa.crypt(plain.getBytes(), true)));
		System.out.println("PLAIN TEXT  : " + new String(rsa.crypt(rsa.crypt(plain.getBytes(), true), false)));
	}

}
