import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Ex06_DiffeHellman {

	public static ArrayList<BigInteger> getPrimeFactors(BigInteger n) {
		ArrayList<BigInteger> res = new ArrayList<BigInteger> ();

		//Add the prime factors to the list by using isProbablePrime to easily check for prime
		for (BigInteger i = new BigInteger("2"); i.intValue() <= Math.sqrt(n.intValue()); i = i.add(BigInteger.ONE))
			if (i.isProbablePrime(100) == true && n.mod(i).intValue() == 0)
				res.add(i);

		return res;
	}

	public static BigInteger primitiveRoot(BigInteger n) {
		//phi = n - 1
		BigInteger phi = n.subtract(BigInteger.ONE);

		//primitive root is value which never gives a result equal than 1 on the following operation
		// i ^ ( phi / primeFactor[j] ) % n
		ArrayList<BigInteger> primeFactors = getPrimeFactors(phi);
		for (BigInteger i = new BigInteger("2"); i.intValue() < n.intValue(); i = i.add(BigInteger.ONE)) {
			boolean flag = true;
			for (BigInteger j = BigInteger.ZERO; j.intValue() < primeFactors.size(); j = j.add(BigInteger.ONE))
				if (i.modPow(phi.divide(primeFactors.get(j.intValue())), n).longValue() == 1)
					flag = false;
			if (flag == true)
				return i;
		}

		return BigInteger.ZERO;
	}

	private static Scanner stdIn = new Scanner(System.in);
	public static void main(String[] args) {
		System.out.print("PRIME NUMBER P : ");
		BigInteger p = new BigInteger(stdIn.nextInt() + "");
		BigInteger q = primitiveRoot(p);
		System.out.println("PRIMITIVE ROOT Q : " + q);

		//public key is q modPow private , p
		System.out.println();
		System.out.print("SECRET xA : ");
		BigInteger xA = new BigInteger(stdIn.nextInt() + "");
		BigInteger yA = q.modPow(xA, p);
		System.out.println("PUBLIC yA: " + yA);

		//public key is q modPow private , p
		System.out.println();
		System.out.print("SECRET xB : ");
		BigInteger xB = new BigInteger(stdIn.nextInt() + "");
		BigInteger yB = q.modPow(xB, p);
		System.out.println("PUBLIC yB: " + yB);

		//shared is public modPow private, p
		System.out.println();
		BigInteger sharedKeyA = yB.modPow(xA, p);
		BigInteger sharedKeyB = yA.modPow(xB, p);
		System.out.println("SHARED KEY sA and sB : " + sharedKeyA + " " + sharedKeyB);
	}

}
