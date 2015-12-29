package thebombzen.thecolourofmoney;

public class PolynomialModel { // x is cardsleft, y is pointsleft, z is card
								// average
	public int constant;
	public int constantd;
	public int xterm;
	public int yterm;
	public int zterm;
	public int xtermd;
	public int ytermd;
	public int ztermd;
	public int xyterm;
	public int xzterm;
	public int yzterm;
	public int x2term;
	public int y2term;
	public int z2term;

	/*
	 * public PolynomialModel(int constant, int xterm, int yterm, int zterm, int
	 * xyterm, int xzterm, int yzterm, int x2term, int y2term, int z2term){
	 * this.constant = constant; this.xterm = xterm; this.yterm = yterm;
	 * this.zterm = zterm; this.xyterm = xyterm; this.xzterm = xzterm;
	 * this.yzterm = yzterm; this.x2term = x2term; this.y2term = y2term;
	 * this.z2term = z2term; }
	 */
	public PolynomialModel(int constant, int constantd, int xterm, int xtermd,
			int yterm, int ytermd, int zterm, int ztermd) {
		this.constant = constant;
		this.constantd = constantd;
		this.xterm = xterm;
		this.yterm = yterm;
		this.zterm = zterm;
		this.xtermd = xtermd;
		this.ytermd = ytermd;
		this.ztermd = ztermd;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !obj.getClass().equals(getClass())) {
			return false;
		}
		PolynomialModel model = (PolynomialModel) obj;
		return constant == model.constant && xterm == model.xterm
				&& yterm == model.yterm && zterm == model.zterm
				&& xyterm == model.xyterm && xzterm == model.xzterm
				&& yzterm == model.yzterm && x2term == model.x2term
				&& y2term == model.y2term && z2term == model.z2term;
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + constant;
		hash = 31 * hash + xterm;
		hash = 31 * hash + yterm;
		hash = 31 * hash + zterm;
		hash = 31 * hash + xyterm;
		hash = 31 * hash + xzterm;
		hash = 31 * hash + yzterm;
		hash = 31 * hash + x2term;
		hash = 31 * hash + y2term;
		hash = 31 * hash + z2term;
		return hash;
	}

	/*
	 * public int getValue(int x, int y, int z){ return constant + x * xterm + y
	 * * yterm + z * zterm + x * y * xyterm + x * z * xzterm + y * z * yzterm +
	 * x * x * x2term + y * y * y2term + z * z * z2term; }
	 */
	public int getLinearValue(int x, int y, int z) {
		return constant / constantd + x * xterm / xtermd + y * yterm / ytermd
				+ z * zterm / ztermd;
	}
}
