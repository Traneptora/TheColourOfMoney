package thebombzen.thecolourofmoney;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class StrategyGuessImplementor {

	public static void main(String[] args) throws Exception {
		byte[][] results = null;
		System.out.println("Reading in generated data...");
		ObjectInputStream input = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(
						"move_guess_data.dat")));
		results = (byte[][]) input.readObject();
		input.close();
		System.out.println("Succesfully read data file... ");
		Scanner scanner = new Scanner(System.in);
		boolean keepPlaying = true;
		while (keepPlaying) {
			System.out.print("Input your points goal: ");
			int ptsLeft = scanner.nextInt();
			boolean inGame = true;
			int cardsLeft = 10;
			int guess = 0;
			int flippedCard = 0;
			int position = 1048575;
			while (inGame) {
				// find int
				if (flippedCard != 0) {
					position -= 1 << (flippedCard - 1);
				}
				guess = results[position][ptsLeft];
				if (guess == -2) {
					System.out
							.println("You have won, although I've made a mistake in my algorithms. ");
					inGame = false;
				} else if (guess == -3) {
					System.out
							.println("Eeets a looooooooosssinngg poh-zeee-tion. ");
					inGame = false;
				} else {
					System.out
							.format("Pick card with number = %2d. You have %2d points left until your target, and %2d cards left to turn over. These cards remain: %n",
									results[position][ptsLeft], ptsLeft,
									cardsLeft);
					int tempCtr = 0;
					for (int i = 0; i < 20; i++) {
						if (1 == ((position >> i) & 1)) {
							tempCtr++;
							if ((i + 1) < 10) {
								System.out.print(" ");
							}
							System.out.print((i + 1));
							if (tempCtr != cardsLeft + 10) {
								System.out.print(", ");
							}
						} else {
							System.out.print("    ");
						}
					}
					System.out.println();
					boolean goodIn = false;
					while (!goodIn) {
						System.out.print("Input the number that came up: ");
						flippedCard = scanner.nextInt();
						if (flippedCard <= 20 && flippedCard > 0
								&& 1 == ((position >> (flippedCard - 1)) & 1)) {
							goodIn = true;
						}
					}
				}
				if (guess <= flippedCard) {
					ptsLeft -= guess;
				}
				cardsLeft--;
				if (ptsLeft <= 0) {
					System.out.println("Congratulations, you won! ");
					inGame = false;
				} else if (cardsLeft == 10) {
					System.out.println("Too bad, you've lost. ");
					inGame = false;
				} else {
				}
			}
			scanner.close();

			System.out
					.println("Would you like to play again?  If not, type Ctrl-C ");
			System.out.println();
		}
	}
}
