package cz.brmlab.yodaqa.pipeline.structured.dbpedia;

import cz.brmlab.yodaqa.pipeline.structured.StructuredAnswerProducer;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;


/**
 * From the QuestionCAS, generate a bunch of CandidateAnswerCAS instances.
 *
 * In this case, this is just a thin wrapper of DBpediaPropertyPrimarySearch,
 * producing raw infobox-extracted relationships of ClueSubject entities
 * as answers.
 */

public class DBpediaPropertyAnswerProducer extends StructuredAnswerProducer {
	public static AnalysisEngineDescription createEngineDescription()
			throws ResourceInitializationException {
		return createEngineDescription("cz.brmlab.yodaqa.pipeline.structured.dbpedia.DBpediaPropertyAnswerProducer",
				DBpediaPropertyPrimarySearch.class);
	}
}
