package thebombzen.thecolourofmoney;

//import java.util.Scanner;
//import java.util.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StrategyGenerator {

	/**
	 * @param args
	 */
	public static byte[] results = new byte[1048576 * 81];
	public static float[] probs = new float[1048576 * 81];
	public static int numOfCards = 20;
	public static int numOfChances = 10;
	public static long iterations = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// boolean[] cards={
		// true, true, true, true, true, true, true, true, true, true, false,
		// true, false, false, true, true, true, true, true, true};
		// Scanner scanner = new Scanner(System.in);
		// System.out.println(scanner.next());
		// int input = Integer.parseInt(scanner.next());
		// byte[] results = new byte[1048576*64];

		// int cards = 1035263;
		/*
		 * for (int i=0; i<20; i++) {
		 * 
		 * if (cards[i]) { cardsInt+=(int)(1)<<i; }
		 * 
		 * } System.out.println(cardsInt);
		 */

		/*
		 * boolean[]cardsOut=new boolean[20]; int isSet = 0; for (int i=0; i<20;
		 * i++) { isSet=cards&1; if(isSet==1){ System.out.println("true"); }
		 * else { System.out.println("false"); } cards = cards >> 1; }
		 */

		for (int i = 0; i < 1048576 * 81; i++) {
			results[i] = (byte) -1;
			probs[i] = (float) -1.0;
		}
		boolean[] cards = new boolean[20];
		// boolean[] cards={true, true, true, true, true, true, true, true,
		// true, true, true, true, true, true, true, true, true, true, true,
		// true};
		for (int cardsLeft = 10; cardsLeft <= 20; cardsLeft++) {
			for (int i = 0; i <= 80; i++) {
				// for(int cardsLeft=11; cardsLeft<=20; cardsLeft++) {
				// System.out.println("Calculating for "+cardsLeft+", so far have completed "
				// + iterations + " iterations.");
				for (int j = 0; j < 1048576; j++) {
					int cardsUnFlipped = 0;
					int tempJ = j;
					// int finalj = j;
					int isSet = 0;
					for (int k = 0; k < 20; k++) {
						isSet = tempJ & 1;
						cardsUnFlipped += isSet;
						tempJ = tempJ >> 1;
						if (isSet == 1) {
							cards[k] = true;
						} else {
							cards[k] = false;
						}
					}
					/*
					 * if (cardsUnFlipped < 10){ finalj = 1048575-j;
					 * cardsUnFlipped=20-cardsUnFlipped; }
					 */
					if (cardsUnFlipped == cardsLeft) {
						// System.out.println(cardsUnFlipped+": "+j);
						/* results[finalj+i*1048576] = */pickCardBool(
								cardsUnFlipped - 10, i, cards, j);
						/*
						 * if(results[j+i*1048576]!=0) {
						 * System.out.println("get!!"
						 * +cardsLeft+" "+j+" "+i+" "+results[j+i*1048576]); }
						 */
					}
					// if (iterations%10000000<1000&&iterations>0) {
					// System.out.println("Iteration "+iterations+" and at j="+j);
					// }

				}
				System.out.println(cardsLeft + " faceup cards, w/ " + i
						+ " pts, after " + iterations + " flops.           - "
						+ results[i * 1048576 + (1 << cardsLeft) - 1] + " - "
						+ probs[i * 1048576 + (1 << cardsLeft) - 1]);
			}

			// System.out.println(i + ": " + pickCard(5,i,cards));
			// System.out.println(i + ": " + results[(i+1)*1048576-1]/* +
			// pickCard(10,i,1048576-1)*/);
			// System.out.println(i+": "+results[(i+1)*1048576-1]);
			// int rand = 8492384;

			// System.out.println(rand+": "+results[rand]);
		}
		System.out.println();
		for (int i = 50; i <= 80; i++) {
			System.out.println(i + " pts, pick: "
					+ results[i * 1048576 + 1048575] + " with probability "
					+ probs[i * 1048576 + 1048575]);
			// System.out.println(i+" pts, pick: "+results[i*1048576+1048574]+" with probability "+probs[i*1048576+1048574]);
		}
		System.out.println();
		System.out.println("Serializing... ");
		try {
			// ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					"results.ser"));
			// ByteArrayOutputStream out = new ByteArrayOutputStream(fileOut);
			out.write(results, 0, results.length);
			out.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		System.out.println("Done!");
	}

	public static void pickCard(int cardsLeft, int ptsLeft, int cards) {
		if (cardsLeft == 0 || ptsLeft <= 0) {
			// return 0;
		}
		boolean[] cardsOut = new boolean[20];
		int isSet = 0;
		int tempCards = cards;
		for (int i = 0; i < 20; i++) {
			isSet = (cards & (int) 1);
			if (isSet == 1) {
				cardsOut[i] = true;
			} else {
				cardsOut[i] = false;
				// System.out.println("Yay, a false!!!");
			}
			tempCards = tempCards >> 1;
		}
		/* byte output = (byte) */pickCardBool(cardsLeft, ptsLeft, cardsOut,
				cards);
		// output;
		// return output;

	}

	public static void pickCardBool(int cardsLeft, int ptsLeft,
			boolean[] cards, int intCard) {
		float maxProb = 0;
		float tempProb = 0;
		int bestCard = -3;
		// int cardsNotFlipped = numOfCards-numOfChances+cardsLeft;
		// if (results[]==-1) {
		if (ptsLeft <= 0) {
			maxProb = 1;
			bestCard = -2;
		} else if (cardsLeft == 0) {
			/*
			 * if (ptsLeft<=0) { maxProb=1;
			 * 
			 * } else {
			 */
			maxProb = 0;
			// }
			bestCard = -3;
		} else if (cardsLeft < 3
				&& ((cardsLeft == 1 && ptsLeft > 20) || (cardsLeft == 2 && ptsLeft > 39))) {
			bestCard = -3;
			maxProb = 0;
		} else if (cardsLeft > 1
				&& cardsLeft < 5
				&& ((cardsLeft == 3 && ptsLeft > 57) || (cardsLeft == 4 && ptsLeft > 75))) {
			bestCard = -3;
			maxProb = 0;
		} else {

			for (int guess = 0; guess < 20; guess++) {
				if (cards[guess]) {
					/*
					 * int cardsUnder = 0; for (int j=i; j<numOfCards; j++){ if
					 * (cards[j]) { cardsUnder++; } }
					 */
					tempProb = 0;

					// int testCount = 0;
					for (int i = 0; i < 20; i++) {
						if (cards[i]) {

							// testCount++;
							cards[i] = false;
							// double tempProb =
							// (double)(cardsUnder)/(double)cardsNotFlipped*prob(cardsLeft-1,
							// ptsLeft-(i+1), cards)
							// +
							// (double)(cardsNotFlipped-cardsUnder)/(double)cardsNotFlipped*prob(cardsLeft-1,
							// ptsLeft, cards);
							int cardsInt = 0;
							int power = 1;
							for (int j = 0; j < 20; j++) {

								if (cards[j]) {
									cardsInt += power;
								}
								power = power << 1;

							}
							int predictedPtsLeft = ptsLeft;
							if (i >= guess) {
								predictedPtsLeft -= (guess + 1);
							}
							if (predictedPtsLeft <= 0) {
								tempProb += 1;
							} else {

								tempProb += probs[predictedPtsLeft * 1048576
										+ cardsInt];
							}

							// tempProb =
							// (float)(cardsUnder)/(float)cardsNotFlipped*probs[predictedPtsLeft*1048576+cardsInt]
							// +
							// (float)(cardsNotFlipped-cardsUnder)/(float)cardsNotFlipped*probs[ptsLeft*1048576+cardsInt];
							iterations++;
							cards[i] = true;
							// System.out.println((cardsLeft+9)+" should be >= "+testCount);

						}
					}

					tempProb = tempProb / ((float) (cardsLeft + 10));
					if (tempProb > maxProb) {
						maxProb = tempProb;
						bestCard = guess + 1;
					}
					// cards[i]=true;
				} else {
					// System.out.println("here");
				}
			}
		}
		// }
		probs[ptsLeft * 1048576 + intCard] = maxProb;
		results[ptsLeft * 1048576 + intCard] = (byte) bestCard;
		/*
		 * if(bestCard!=0&&bestCard!=-3){
		 * System.out.println(intCard+" "+ptsLeft+" "+bestCard+" "+maxProb); }
		 */
		// return bestCard;
	}

	public static double prob(int cardsLeft, int ptsLeft, boolean[] cards) {
		double maxProb = 0;
		int cardsNotFlipped = numOfCards - numOfChances + cardsLeft;
		if (ptsLeft <= 1) {
			return 1;
		} else if (cardsLeft <= 0) {
			return 0;
		}
		int cardsInt = 0;

		for (int i = 0; i < 20; i++) {

			if (cards[i]) {
				cardsInt += (int) (1) << i;
			}

		}
		/*
		 * for (int i = 0; i<numOfCards; i++) { if (cards[i]){
		 */
		/*
		 * if(results[cardsInt+ptsLeft*1048576]==-1) { iterations++; for(int
		 * i=0; i<20; i++) { int cardsUnder = 0; for (int j=i; j<numOfCards;
		 * j++){ if (cards[j]) { cardsUnder++; } } int
		 * predictedPtsLeft=ptsLeft-(i+1); if (predictedPtsLeft<0) {
		 * predictedPtsLeft=0; }
		 * 
		 * cards[i]=false;
		 * tempProb=(double)(cardsUnder)/(double)cardsNotFlipped*
		 * prob(cardsLeft-1, predictedPtsLeft, cards) +
		 * (double)(cardsNotFlipped-
		 * cardsUnder)/(double)cardsNotFlipped*prob(cardsLeft-1, ptsLeft,
		 * cards); if (tempProb>maxProb) { maxProb=tempProb; } cards[i]=true; }
		 * return maxProb; } else if(results[cardsInt+ptsLeft*1048576]==0) {
		 * return 0; } else {
		 */
		int i = results[cardsInt + ptsLeft * 1048576];
		int cardsUnder = 0;
		for (int j = i - 1; j < numOfCards; j++) {
			if (cards[j]) {
				cardsUnder++;
			}
		}
		cards[i - 1] = false;
		int predictedPtsLeft = ptsLeft - i;
		if (predictedPtsLeft < 0) {
			predictedPtsLeft = 0;
		}
		maxProb = (double) (cardsUnder) / (double) cardsNotFlipped
				* prob(cardsLeft - 1, predictedPtsLeft, cards)
				+ (double) (cardsNotFlipped - cardsUnder)
				/ (double) cardsNotFlipped
				* prob(cardsLeft - 1, ptsLeft, cards);
		return maxProb;
		// cards[i-1]=true;

		/*
		 * } }
		 */
		// return maxProb;
	}

}
