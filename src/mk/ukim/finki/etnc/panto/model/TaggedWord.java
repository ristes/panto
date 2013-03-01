package mk.ukim.finki.etnc.panto.model;

public class TaggedWord {

	public String tag;
	public String word;

	public TaggedWord(String tag, String word) {
		this.tag = tag;
		this.word = word;
	}

	@Override
	public String toString() {
		return String.format("(%s-%s)", tag, word);
	}
}
