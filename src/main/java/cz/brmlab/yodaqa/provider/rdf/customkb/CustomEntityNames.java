package cz.brmlab.yodaqa.provider.rdf.customkb;

/**
 * Created by bzhao on 1/4/17.
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.stream.JsonReader;
import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.Literal;

import cz.brmlab.yodaqa.provider.rdf.Article;
import cz.brmlab.yodaqa.provider.url.UrlConstants;
import cz.brmlab.yodaqa.provider.url.UrlManager;
import org.slf4j.Logger;

/**
 * A wrapper around a custom RDF knowledge base that maps clue strings to
 * Knowledge Base entities. The main point of using this is to find out
 * whether an entity with the given clue string (termed also "clue label")
 * exists.
 * <p>
 * We still use "Article" objects, so as to fit the
 * same interface that package
 * cz.brmlab.yodaqa.provider.rdf.dbpedia.DBPediaTitles provides.
 * <p>
 * TODO we should refactor the pipeline to depend on an interface instead and have the Article class implement it
 * This would also allow us to create a new class that also implements the same interface
 * (that this class should instantiate objects out of after it queries the label lookup service, instead of "Articles")
 */

public class CustomEntityNames extends CustomLookup {
	protected static final String customLookupUrl = UrlManager.getInstance().getUrl(UrlConstants.CUSTOM_LABEL);

	/**
	 * Query for a given entity, returning a set of "article" objects.
	 */
	public List<Article> query(String title, Logger logger) {
		for (String titleForm : cookedTitles(title)) {
			List<Article> entities;
			while (true) {
				try {
					entities = queryCustomLabelLookup(titleForm, logger);
					break; // Success!
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("*** " + customLookupUrl + " label lookup query (temporarily?) failed, retrying in a moment...");
					try {
						TimeUnit.SECONDS.sleep(10);
					} catch (InterruptedException e2) { // oof...
						e2.printStackTrace();
					}
				}
			}
			if (!entities.isEmpty())
				return entities;
		}
		return new ArrayList<Article>();
	}


	/**
	 * Query the fuzzy search label-lookup service for a concept label.
	 * We use https://github.com/bmzhao/custom-label-lookup,
	 * based on providing the same interface as https://github.com/brmson/label-lookup/ (main label-lookup
	 * script) as a fuzzy search that's tolerant to wrong capitalization,
	 * omitted interpunction and typos.
	 * <p>
	 * XXX: This method should probably be in a different
	 * provider subpackage altogether...
	 * <p>
	 * TODO fix code duplication w/same method in cz.brmlab.yodaqa.provider.rdf.dbpedia.DBPediaTitles
	 */
	private List<Article> queryCustomLabelLookup(String label, Logger logger) throws IOException {
		List<Article> results = new LinkedList<>();
		String capitalisedLabel = super.capitalizeTitle(label);
		String encodedName = URLEncoder.encode(capitalisedLabel, "UTF-8").replace("+", "%20");
		String requestURL = customLookupUrl + "/search?name=" + encodedName;

		URL request = new URL(requestURL);
		URLConnection connection = request.openConnection();
		Gson gson = new Gson();

		JsonReader jr = new JsonReader(new InputStreamReader(connection.getInputStream()));
		jr.beginObject();
		jr.nextName(); //results :
		jr.beginArray();
		while (jr.hasNext()) {
			Article o = gson.fromJson(jr, Article.class);
			// Record all exact-matching entities,
			// or the single nearest fuzzy-matched
			// one.
			o.setByCustomLookup(true);
			if (o.getDist() == 0) {
				// Sometimes, we get duplicates
				// like "U.S. Navy" and "U.S. navy".
				if (results.isEmpty() || !results.get(results.size() - 1).getName().equals(o.getName()))
					results.add(o);
			} else if (results.isEmpty()) {
				results.add(o);
			}
			logger.debug("custom-lookup({}) returned: d{} ~{} [{}] {} {}", label, o.getDist(), o.getMatchedLabel(), o.getCanonLabel(), o.getName(), o.getPageID());
		}
		jr.endArray();
		jr.endObject();

		return results;
	}


}
