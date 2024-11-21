package hk.edu.polyu.comp.comp2021.cvfs.model;
/**
 * Represents a negation criterion that inverts the result of another criterion.
 * This criterion evaluates to true if and only if the inner criterion evaluates to false.
 */
public class NegationCriterion implements Criterion {
    private final Criterion inner;
    /**
     * Constructs a new negation criterion.
     *
     * @param inner The inner criterion to negate.
     */
    public NegationCriterion(Criterion inner) {
        this.inner = inner;
    }

    @Override
    public boolean evaluate(File file) {
        return !inner.evaluate(file);
    }

    @Override
    public String toString() {
        return "NOT (" + inner + ")";
    }
}
