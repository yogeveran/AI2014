
public class Tuple<A,B> {
	public A getVal1() {
		return val1;
	}
	public void setVal1(A val1) {
		this.val1 = val1;
	}
	public B getVal2() {
		return val2;
	}
	public void setVal2(B val2) {
		this.val2 = val2;
	}
	A val1;
	B val2;
	public Tuple(A val1, B val2) {
		super();
		this.val1 = val1;
		this.val2 = val2;
	}
	public static Tuple<Double, Double> maxLeft(Tuple<Double, Double> alpha,Tuple<Double, Double> alphabeta) {
		if(alpha.val1>alphabeta.val1)
			return alpha;
		if(alpha.val1<alphabeta.val1)
			return alphabeta;
		if(alpha.val2>alphabeta.val2)
			return alpha;
		if(alpha.val2<alphabeta.val2)
			return alphabeta;
		return alpha;
	}
	public static Tuple<Double, Double> minLeft(Tuple<Double, Double> beta,	Tuple<Double, Double> alphabeta) {
		if(beta.val1<=alphabeta.val1)
			return beta;
		return alphabeta;
	}
}
