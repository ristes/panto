package mk.ukim.finki.etnc.panto.model;

public class QueryTriple {

	QueryTripleElement firstBaseNp;
	QueryTripleElement connectingElement;
	QueryTripleElement secondBaseNp;

	public void setFirstBaseNp(QueryTripleElement firstBaseNp) {
		this.firstBaseNp = firstBaseNp;
	}

	public void setConnectingElement(QueryTripleElement connectingElement) {
		this.connectingElement = connectingElement;
	}

	public void setSecondBaseNp(QueryTripleElement secondBaseNp) {
		this.secondBaseNp = secondBaseNp;
	}

	@Override
	public String toString() {
		return String.format("{%s - %s -%s}", firstBaseNp.toString(),
				connectingElement.toString(), secondBaseNp.toString());
	}
}
