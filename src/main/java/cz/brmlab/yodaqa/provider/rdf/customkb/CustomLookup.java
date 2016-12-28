package cz.brmlab.yodaqa.provider.rdf.customkb;

import cz.brmlab.yodaqa.provider.rdf.CachedJenaLookup;
import cz.brmlab.yodaqa.provider.url.UrlConstants;
import cz.brmlab.yodaqa.provider.url.UrlManager;

/** This is an abstract base class for accessing a custom knowledge base. */

public abstract class CustomLookup extends CachedJenaLookup {
    public CustomLookup() {
		/* Replace the first URL below with http://dbpedia.org/sparql
		 * to use the public DBpedia SPARQL endpoint. */
        super(UrlManager.getInstance().getUrl(UrlConstants.CUSTOM),
                "PREFIX : <http://dbpedia.org/resource/>\n" +
                        "PREFIX dbpedia2: <http://dbpedia.org/property/>\n" +
                        "PREFIX dbpedia: <http://dbpedia.org/>\n" +
                        "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                        "");
    }
}
