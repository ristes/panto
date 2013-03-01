package mk.ukim.finki.etnc.panto.utils;

import java.util.ArrayList;
import java.util.List;

import mk.ukim.finki.etnc.panto.model.QueryTriple;
import mk.ukim.finki.etnc.panto.model.QueryTripleElement;
import mk.ukim.finki.etnc.panto.model.TaggedWord;
import edu.stanford.nlp.trees.Tree;

public class QueryTripleUtils {

	private static final List<String> modifierLabels = new ArrayList<String>();

	static {
		modifierLabels.add("JJS");
		modifierLabels.add("RBS");
		modifierLabels.add("JJR");
		modifierLabels.add("RBR");
		modifierLabels.add("CC");

	}

	/**
	 * Extraction of the query triple elements
	 * 
	 * @param tree
	 * @param tripleElements
	 * @param modifiers
	 * @param targets
	 */
	public static void findQueryTripleElements(Tree tree,
			List<QueryTripleElement> tripleElements, List<Tree> modifiers,
			List<TaggedWord> targets) {

		List<Tree> childs = tree.getChildrenAsList();

		if (childs != null && !childs.isEmpty()) {
			for (Tree child : childs) {
				String childLabel = child.label().value();

				if (modifierLabels.contains(childLabel)) {
					modifiers.add(child);
				}

				// the base NP are separate QueryTripleElements
				if (isBaseNp(child)) {
					// the target elements are always in a base NP
					if (targets.isEmpty()) {
						getTargetElement(child, targets);
					}
					// create the query triple element and add it to the list
					QueryTripleElement qte = new QueryTripleElement();
					getElementFromNounPhrase(child, qte);
					tripleElements.add(qte);
					qte = new QueryTripleElement();
					// add query triple for the next connecting part
					tripleElements.add(qte);
				} else {
					List<Tree> nextChilds = child.getChildrenAsList();
					if (nextChilds == null || nextChilds.isEmpty()) {
						// leaf elements

						// add the tagged word into the connecting query triple
						// element
						tripleElements.get(tripleElements.size() - 1).add(
								new TaggedWord(tree.value(), child.value()));
					} else {
						findQueryTripleElements(child, tripleElements,
								modifiers, targets);
					}
				}
			}
		}
	}

	/**
	 * Assemble the query triples from the list of the elements
	 * 
	 * @param elements
	 * @return
	 */
	public static List<QueryTriple> assembleQueryTriples(
			List<QueryTripleElement> elements) {
		List<QueryTripleElement> remove = new ArrayList<QueryTripleElement>();
		for (QueryTripleElement elem : elements) {
			if (elem.isEmpty()) {
				remove.add(elem);
			}
		}
		elements.removeAll(remove);
		List<QueryTriple> triples = new ArrayList<QueryTriple>();
		for (int i = 2; i < elements.size(); i += 2) {
			QueryTriple qt = new QueryTriple();
			qt.setFirstBaseNp(elements.get(i - 2));
			qt.setConnectingElement(elements.get(i - 1));
			qt.setSecondBaseNp(elements.get(i));
			triples.add(qt);
		}
		return triples;
	}

	/**
	 * Check if this is base noun phrase
	 * 
	 * @param node
	 * @return
	 */
	private static boolean isBaseNp(Tree node) {
		String label = node.label().value();

		if ("NP".equals(label)) {
			List<Tree> childs = node.getChildrenAsList();
			boolean hasNp = false;
			if (childs != null) {
				for (Tree child : childs) {
					String childLabel = child.label().value();
					if (childLabel.equals("NP")) {
						hasNp = true;
						break;
					}
				}
			}
			return !hasNp;
		}
		return false;
	}

	/**
	 * We extract the question target words from the first base noun phrase that
	 * contains NN* element
	 * 
	 * @param firstBaseNp
	 * @param targets
	 */
	private static void getTargetElement(Tree firstBaseNp,
			List<TaggedWord> targets) {
		List<Tree> childs = firstBaseNp.getChildrenAsList();
		if (childs != null && !childs.isEmpty()) {
			for (Tree child : childs) {
				List<Tree> nextChilds = child.getChildrenAsList();
				if (nextChilds == null || nextChilds.isEmpty()) {
					// leaf element
					if (firstBaseNp.label().value().startsWith("NN")) {
						targets.add(new TaggedWord(firstBaseNp.value(), child
								.value()));
					}
				} else {
					getTargetElement(child, targets);
				}
			}
		}

	}

	/**
	 * Od decata na baseNp ke kreira element. Pominuvame so dfs i go dobivame
	 * elementot.
	 * 
	 * @param baseNp
	 * @return
	 */
	private static void getElementFromNounPhrase(Tree baseNp,
			QueryTripleElement qte) {
		List<Tree> childs = baseNp.getChildrenAsList();
		if (childs != null && !childs.isEmpty()) {
			for (Tree child : childs) {
				List<Tree> nextChilds = child.getChildrenAsList();
				if (nextChilds == null || nextChilds.isEmpty()) {
					// leaf element
					qte.add(new TaggedWord(baseNp.value(), child.value()));
				} else {
					getElementFromNounPhrase(child, qte);
				}
			}
		}
	}

}
