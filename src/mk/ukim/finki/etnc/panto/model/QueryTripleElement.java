package mk.ukim.finki.etnc.panto.model;

import java.util.ArrayList;
import java.util.List;

public class QueryTripleElement {

	List<TaggedWord> words;

	public QueryTripleElement() {
		words = new ArrayList<>();
	}

	public void add(TaggedWord word) {
		words.add(word);
	}

	public boolean isEmpty() {
		return words.isEmpty();
	}

	@Override
	public String toString() {
		return words.toString();
	}
}
