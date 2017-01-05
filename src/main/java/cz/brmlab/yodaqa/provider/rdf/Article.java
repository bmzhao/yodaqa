package cz.brmlab.yodaqa.provider.rdf;


/** A container of enwiki article metadata.
 * This must 1:1 map to label-lookup API. */
public class Article implements Cloneable {
    protected String name;
    protected int pageID;
    protected String matchedLabel;
    protected String canonLabel;
    protected double dist; // edit dist.
    protected double pop; // relevance/prominence of the concept (universally or wrt. the question)
    protected double prob;
    protected String description; // long-ish text
    protected boolean getByFuzzyLookup = false;
    protected boolean getByCWLookup = false;
    protected boolean getByCustomLookup = false;

    public Article(String label, int pageID) {
        this.matchedLabel = label;
        this.canonLabel = label;
        this.pageID = pageID;
    }

    public Article(String label, int pageID, String name, double dist) {
        this(label, pageID);
        this.name = name;
        this.dist = dist;
    }

    public Article(String label, int pageID, String name, double dist, double prob) {
        this(label, pageID);
        this.name = name;
        this.dist = dist;
        this.prob = prob;
    }

    public Article(Article baseA, String label, int pageID, String name, double pop, double prob, String descr) {
        this.name = name;
        this.pageID = pageID;
        this.matchedLabel = baseA.matchedLabel;
        this.canonLabel = label;
        this.dist = baseA.dist;
        this.pop = pop;
        this.prob = prob;
        this.description = descr;
        this.getByCWLookup = baseA.getByCWLookup;
        this.getByFuzzyLookup = baseA.getByFuzzyLookup;
        this.getByCustomLookup= baseA.getByCustomLookup;
    }

    public String getName() { return name; }
    public int getPageID() { return pageID; }
    public String getMatchedLabel() { return matchedLabel; }
    public String getCanonLabel() { return canonLabel; }
    public double getDist() { return dist; }
    public double getPop() { return pop; }
    public double getProb() { return prob; }
    public String getDescription() { return description; }
    public boolean isByFuzzyLookup() { return getByFuzzyLookup; }
    public boolean isByCustomLookup() { return getByCustomLookup; }
    public boolean isByCWLookup() { return getByCWLookup; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPageID(int pageID) {
        this.pageID = pageID;
    }

    public void setMatchedLabel(String matchedLabel) {
        this.matchedLabel = matchedLabel;
    }

    public void setCanonLabel(String canonLabel) {
        this.canonLabel = canonLabel;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setByFuzzyLookup(boolean getByFuzzyLookup) {
        this.getByFuzzyLookup = getByFuzzyLookup;
    }

    public void setByCWLookup(boolean getByCWLookup) {
        this.getByCWLookup = getByCWLookup;
    }

    public void setByCustomLookup(boolean getByCustomLookup) {
        this.getByCustomLookup = getByCustomLookup;
    }

    public Article clone() {
        try { // much boilerplate
            return (Article) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public Article newRenamed(String newName) {
        Article a2 = this.clone();
        a2.name = newName;
        return a2;
    }
}
