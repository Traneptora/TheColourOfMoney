package thebombzen.thecolourofmoney;

public class FunctionModel {
	public int constn; // numerator additive constant
	public int constAvgCard; // numerator card avg multiplier
	public int constPointsLeft; // numerator pointsLeft multiplier
	public int constCardsLeftNum;
	public int constCardsLeft; // denominator cardsLeft multiplier
	public int constd; // denominator additive constant

	public FunctionModel(int constn, int constAvgCard, int constPointsLeft,
			int constCardsLeftNum, int constCardsLeft, int constd) {
		this.constn = constn;
		this.constd = constd;
		this.constPointsLeft = constPointsLeft;
		this.constAvgCard = constAvgCard;
		this.constCardsLeft = constCardsLeft;
		this.constCardsLeftNum = constCardsLeftNum;
	}

	public int getValue(int cardsLeft, int pointsLeft, int cardSum) {
		return (constn + constPointsLeft * pointsLeft + constCardsLeftNum
				* cardsLeft + (constAvgCard * cardSum / cardsLeft))
				/ (constCardsLeft * cardsLeft + constd);
	}
}
