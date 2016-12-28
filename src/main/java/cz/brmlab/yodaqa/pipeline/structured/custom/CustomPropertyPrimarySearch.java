package cz.brmlab.yodaqa.pipeline.structured.custom;

import cz.brmlab.yodaqa.analysis.ansscore.AF;
import cz.brmlab.yodaqa.analysis.ansscore.AnswerFV;
import cz.brmlab.yodaqa.flow.dashboard.AnswerSourceStructured;
import cz.brmlab.yodaqa.model.Question.Concept;
import cz.brmlab.yodaqa.model.TyCor.CustomPropertyLAT;
import cz.brmlab.yodaqa.pipeline.structured.StructuredPrimarySearch;
import cz.brmlab.yodaqa.provider.rdf.PropertyValue;
import cz.brmlab.yodaqa.provider.rdf.customkb.CustomProperties;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * From the QuestionCAS, generate a bunch of CandidateAnswerCAS
 * instances.  In this case, we generate answers from your custom
 * knowledge base relation properties.
 */

public class CustomPropertyPrimarySearch extends StructuredPrimarySearch {
	public CustomPropertyPrimarySearch() {
		super("Custom KB Property", AF.OriginCustomP_ClueType, AF.OriginCustomPNoClue);
		logger = LoggerFactory.getLogger(CustomPropertyPrimarySearch.class);
	}

	final CustomProperties customKB = new CustomProperties();

	protected List<PropertyValue> getConceptProperties(JCas questionView, Concept concept) {
		List<PropertyValue> properties = new ArrayList<>();
		// Query the custom kb raw dataset
		/* TODO: Fetch by pageID. */
		properties.addAll(customKB.query(concept.getCookedLabel(), logger));
		return properties;
	}

	protected AnswerSourceStructured makeAnswerSource(PropertyValue property) {
		return new AnswerSourceStructured(AnswerSourceStructured.TYPE_CUSTOM,
				property.getOrigin(), property.getObjRes(), property.getObject());
	}

	protected void addTypeLAT(JCas jcas, AnswerFV fv, String type) throws AnalysisEngineProcessException {
		fv.setFeature(AF.LATCustomProperty, 1.0);
		addTypeLAT(jcas, fv, type, new CustomPropertyLAT(jcas));
	}
}
