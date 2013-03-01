package mk.ukim.finki.etnc.panto;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import mk.ukim.finki.etnc.panto.model.QueryTriple;
import mk.ukim.finki.etnc.panto.model.QueryTripleElement;
import mk.ukim.finki.etnc.panto.model.TaggedWord;
import mk.ukim.finki.etnc.panto.utils.QueryTripleUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
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
	static {
		lp = LexicalizedParser
				.loadModel("edu/stanford/nlp/models/lexparser/englishFactored.ser.gz");
	}

	public static void demoAPI(String text) {

		// This option shows loading and using an explicit tokenizer
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		List<CoreLabel> rawWords2 = tokenizerFactory.getTokenizer(
				new StringReader(text)).tokenize();
		Tree parse = lp.apply(rawWords2);

		process(parse);

		TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		tp.printTree(parse);
	}

	public static void dependencyTest(String text) {

		// This option shows loading and using an explicit tokenizer
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		List<CoreLabel> rawWords2 = tokenizerFactory.getTokenizer(
				new StringReader(text)).tokenize();
		Tree parse = lp.apply(rawWords2);

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		System.out.println(tdl);
		System.out.println();
	}

	/**
	 * Process the parse tree
	 * 
	 * @param parse
	 */
	public static void process(Tree parse) {
		List<QueryTripleElement> tripleElements = new ArrayList<QueryTripleElement>();
		tripleElements.add(new QueryTripleElement());
		List<TaggedWord> targets = new ArrayList<TaggedWord>();
		List<Tree> modifiers = new ArrayList<Tree>();

		// Process the tags in order to extract the query triple elements,
		// modifiers and targets
		QueryTripleUtils.findQueryTripleElements(parse, tripleElements, modifiers, targets);

		System.out.println(targets);
		System.out.println(modifiers);

		List<QueryTriple> triples = QueryTripleUtils.assembleQueryTriples(tripleElements);
		System.out.println(triples);
		
		
	}

	public static void main(String[] args) {
		demoAPI("Which is the longest river that flows thorught the states neighbouring Mississippi?");
	}

}
