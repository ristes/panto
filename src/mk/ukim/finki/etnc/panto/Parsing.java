package mk.ukim.finki.etnc.panto;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Parsing {

	private static LexicalizedParser lp;
	private static final List<String> modifierLabels = new ArrayList<String>();

	static {
		lp = LexicalizedParser
				.loadModel("edu/stanford/nlp/models/lexparser/englishFactored.ser.gz");
		modifierLabels.add("JJS");
		modifierLabels.add("RBS");
		modifierLabels.add("JJR");
		modifierLabels.add("RBR");
		modifierLabels.add("CC");

	}

	public static void demoAPI(String text) {
		List<Tree> headBaseNP = new ArrayList<Tree>();
		List<Tree> baseNP = new ArrayList<Tree>();
		List<Tree> targets = new ArrayList<Tree>();
		List<Tree> modifiers = new ArrayList<Tree>();

		// This option shows loading and using an explicit tokenizer
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		List<CoreLabel> rawWords2 = tokenizerFactory.getTokenizer(
				new StringReader(text)).tokenize();
		Tree parse = lp.apply(rawWords2);

		// Process the tags in order to extract the head base NP, modifiers and
		// targets
		findBaseNP(parse, headBaseNP, baseNP, modifiers, targets);

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		System.out.println(tdl);
		System.out.println();

		TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		tp.printTree(parse);
	}

	private static void findBaseNP(Tree tree, List<Tree> headPhraseTree,
			List<Tree> baseNominalPhrases, List<Tree> modifiers,
			List<Tree> targets) {

		List<Tree> childs = tree.getChildrenAsList();

		boolean hasNpChilds = false;
		boolean hasCC = false;
		String treeLabel = tree.label().value();
		boolean targetFound = treeLabel.equals("NP") && targets.isEmpty();

		if (childs != null) {
			for (Tree child : childs) {
				String childLabel = child.label().value();
				if (childLabel.equals("NP")) {
					hasNpChilds = true;
				}
				if (childLabel.equals("CC")) {
					hasCC = true;
				}
				if (!targetFound && childLabel.startsWith("NN")) {
					targets.add(child);
				}
				if (modifierLabels.contains(childLabel)) {
					modifiers.add(child);
				}
				findBaseNP(child, headPhraseTree, baseNominalPhrases,
						modifiers, targets);
			}
			if (treeLabel.equals("NP")) {
				if (!hasNpChilds) {
					baseNominalPhrases.add(tree);
					tree.pennPrint();
					System.out.println();
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
	private QueryTripleElement getElementFromNounPhrase(Tree baseNp) {
		return null;
	}

	public static void main(String[] args) {
		demoAPI("Which is the longest river that flows thorught the states neighbouring Mississippi?");
	}

}
