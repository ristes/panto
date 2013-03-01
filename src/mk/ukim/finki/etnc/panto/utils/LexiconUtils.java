package mk.ukim.finki.etnc.panto.utils;

import java.util.List;

import mk.ukim.finki.etnc.panto.model.TaggedWord;

import com.hp.hpl.jena.ontology.OntResource;

public class LexiconUtils {

	public static List<OntResource> getResource(TaggedWord word) {
		List<String> synonims = induceSynonims(word.word);
		// connect to the dbpedia endpoint and extract all resources from there
		//use http appache libs to create request to dbpedia endpoint
		
		/**
		if (endpoint == null) {
			endpoint = "http://dbpedia.org/sparql";
		}
		String url = String.format("%s?query=%s&output=json", query, output);
		System.out.println(url);
		Map<String, Object> reqParams = new HashMap<String, Object>();
		reqParams.put("query", query);
		reqParams.put("output", output);
		InputStream body = WS.url(endpoint).params(reqParams).get().getStream();
		System.out.println("done");
		
		 */
		return null;
	}

	/**
	 * Use wordnet to induce synonyms. Use the synset relations
	 * 
	 * @param word
	 * @return
	 */
	private static List<String> induceSynonims(String word) {
		return null;
	}

}
