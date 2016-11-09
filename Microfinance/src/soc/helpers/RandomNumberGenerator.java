package soc.helpers;

import java.util.Random;

public class RandomNumberGenerator {

	public int generateRandomNumber() {
		Random rand = new Random();
		double d = 100000 * rand.nextDouble();
		int p = (int) d;
		if (p > 0 && p < 100000) {
			//System.out.println(p);
		} else {
			//System.out.println(p + 1);
		}
		return p;
	}

}
