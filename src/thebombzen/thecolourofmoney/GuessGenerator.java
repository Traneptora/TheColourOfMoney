package thebombzen.thecolourofmoney;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GuessGenerator {
	public static Map<Integer, boolean[]> positionMap = new HashMap<Integer, boolean[]>();
	public static Map<Integer, Integer> positionSumMap = new HashMap<Integer, Integer>();
	public static int pts = 59;
	public static int initialPlays = 5;
	public static Comparator<Map.Entry<List<Integer>, Float>> comparer = new Comparator<Map.Entry<List<Integer>, Float>>() {
		public int compare(Map.Entry<List<Integer>, Float> entry1,
				Map.Entry<List<Integer>, Float> entry2) {
			if (entry2.getValue() > entry1.getValue()) {
				return 1;
			}
			if (entry2.getValue() < entry1.getValue()) {
				return -1;
			}
			return 0;
		}
	};

	public static void main(String[] args) throws Exception {
		System.out.println("Reading...");
		// populatePositionMap();
		readPositionMap();
		System.out.println("Calculating...");
		FunctionModel model = getOptimalModel();
		float prob = chanceOfWinning(model, pts, 100000);
		System.out
				.format("Prob of winning is %f at a=%d, b=%d, c=%d, d=%d, e=%d, f=%d%n",
						prob, model.constn, model.constAvgCard,
						model.constPointsLeft, model.constCardsLeftNum,
						model.constCardsLeft, model.constd);
	}

	public static FunctionModel getOptimalModel() throws Exception {
		int numtesting = 10 * 10 * 10 * 10 * 10 * 10;
		List<List<Integer>> pointsToTest = new ArrayList<List<Integer>>(
				numtesting);
		for (int constn = 1; constn < 10; constn++) {
			for (int constAvgCard = 1; constAvgCard < 10; constAvgCard++) {
				for (int constPointsLeft = 1; constPointsLeft < 10; constPointsLeft++) {
					for (int constCardsLeftNum = 1; constCardsLeftNum < 10; constCardsLeftNum++) {
						for (int constCardsLeft = 1; constCardsLeft < 10; constCardsLeft++) {
							for (int constd = 1; constd < 10; constd++) {
								List<Integer> temp = new ArrayList<Integer>(6);
								temp.add(constn);
								temp.add(constAvgCard);
								temp.add(constPointsLeft);
								temp.add(constCardsLeftNum);
								temp.add(constCardsLeft);
								temp.add(constd);
								pointsToTest.add(temp);
							}
						}
					}
				}
			}
		}
		Map<List<Integer>, Float> pointToProb = new HashMap<List<Integer>, Float>();
		List<Map.Entry<List<Integer>, Float>> currSublist = null;
		int plays = initialPlays;
		while (numtesting > 1) {
			for (List<Integer> p : pointsToTest) {
				int constn = p.get(0);
				int constAvgCard = p.get(1);
				int constPointsLeft = p.get(2);
				int constCardsLeftNum = p.get(3);
				int constCardsLeft = p.get(4);
				int constd = p.get(5);
				FunctionModel model = new FunctionModel(constn, constAvgCard,
						constPointsLeft, constCardsLeftNum, constCardsLeft,
						constd);
				float prob = chanceOfWinning(model, pts, plays);
				pointToProb.put(p, prob);
			}
			List<Map.Entry<List<Integer>, Float>> entries = new ArrayList<>(
					pointToProb.entrySet());
			Collections.sort(entries, comparer);
			System.out.format("Completed %d points each with %d plays.%n",
					numtesting, plays);
			numtesting /= 2;
			plays = plays > 5000 ? 10000 : plays * 2;
			currSublist = entries.subList(0, numtesting);
			pointsToTest.clear();
			pointToProb.clear();
			for (Map.Entry<List<Integer>, Float> entry : currSublist) {
				pointsToTest.add(entry.getKey());
			}
		}
		List<Integer> point = currSublist.get(0).getKey();
		return new FunctionModel(point.get(0), point.get(1), point.get(2),
				point.get(3), point.get(4), point.get(5));
	}

	public static int getGuess(FunctionModel model, int position, int pointsLeft) {
		int numCards = hammingWeight(position);
		boolean[] cards = positionMap.get(position);
		int sum = positionSumMap.get(position);
		int base = model.getValue(numCards, pointsLeft, sum) - 1;
		if (base < 0) {
			base = 0;
		}
		if (base > 19) {
			base = 19;
		}
		int reach = 1;
		while (!cards[base]) {
			int lower = base - reach;
			int higher = base + reach;
			if (lower < 0) {
				lower = 0;
			}
			if (higher > 19) {
				higher = 19;
			}
			if (cards[higher]) {
				base = higher;
				break;
			}
			if (cards[lower]) {
				base = lower;
				break;
			}
			reach++;
		}
		return base + 1;
	}

	public static void populatePositionMap() throws Exception {
		for (int i = 0; i < (1 << 20); i++) {
			boolean[] positionArray = new boolean[20];
			int sum = 0;
			for (int k = 0; k < 20; k++) {
				positionArray[k] = ((i >> k) & 1) == 1;
				if (positionArray[k]) {
					sum += k + 1;
				}
			}
			positionMap.put(i, positionArray);
			positionSumMap.put(i, sum);
		}
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(
						"position_array_data.dat")));
		oos.writeObject(positionMap);
		oos.writeObject(positionSumMap);
		oos.close();
	}

	@SuppressWarnings("unchecked")
	public static void readPositionMap() throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream("position_array_data.dat")));
		positionMap = (Map<Integer, boolean[]>) ois.readObject();
		positionSumMap = (Map<Integer, Integer>) ois.readObject();
		ois.close();
	}

	public static int hammingWeight(int x) {
		x -= ((x >>> 1) & 0x55555555);
		x = (((x >>> 2) & 0x33333333) + (x & 0x33333333));
		x = (((x >>> 4) + x) & 0x0f0f0f0f);
		x += (x >>> 8);
		x += (x >>> 16);
		return (x & 0x0000003f);
	}

	public static float chanceOfWinning(FunctionModel model, int pts,
			final float plays) throws Exception {
		float wins = 0;
		Random random = new Random();
		boolean[] cards = new boolean[20];
		for (int play = 0; play < plays; play++) {
			int ptsLeft = pts;
			int guess = 0;
			int flippedCard = 0;
			int position = (1 << 20) - 1;
			Arrays.fill(cards, true);
			for (int cardsLeft = 20; cardsLeft > 10 && ptsLeft > 0; cardsLeft--) {
				flippedCard = getRandomTrueSlot(random, cards);
				position -= 1 << (flippedCard - 1);
				cards[flippedCard - 1] = false;
				guess = getGuess(model, position, ptsLeft);
				if (guess <= flippedCard) {
					ptsLeft -= guess;
				}
				if (ptsLeft <= 0) {
					wins++;
				}
			}
		}
		return wins / plays;
	}

	public static int[] openSlots = new int[20];

	public static int getRandomTrueSlot(Random random, boolean[] array) {
		int curr = 0;
		for (int i = 0; i < 20; i++) {
			if (array[i]) {
				openSlots[curr] = i;
				curr++;
			}
		}
		return openSlots[random.nextInt(curr)] + 1;
	}
}
