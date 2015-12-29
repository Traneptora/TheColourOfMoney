package thebombzen.thecolourofmoney;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class StrategyImplementerGame {

	public static void main(String[] args) throws Exception {
		byte[] results = null;
		DataInputStream resultsIn = new DataInputStream(new FileInputStream(
				"results.ser"));
		int fileSize = (int) (new File("results.ser").length());
		results = new byte[fileSize];
		resultsIn.readFully(results, 0, results.length);
		resultsIn.close();
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
				guess = results[position + ptsLeft * 1048576];
				if (guess == -2) {
					System.out
							.println("You have won, although I've made a mistake in my algorithms. ");
					inGame = false;
				} else if (guess == -3) {
					System.out
							.println("Eeets a looooooooosssinngg poh-zeee-tion. ");
					inGame = false;
				} else {
					String justdefy = "";
					String justdefy2 = "";
					if (guess < 10) {
						justdefy = " ";
					}
					if (ptsLeft < 10) {
						justdefy2 = " ";
					}
					System.out.println("Pick N=" + justdefy
							+ results[position + ptsLeft * 1048576]
							+ ", you have " + ptsLeft + justdefy2
							+ " points left, and " + cardsLeft + " chances: ");
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
					.println("Would you like to play again?  If not, type C-c C-c. ");
			System.out.println();
		}
	}
}
