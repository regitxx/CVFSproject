package hk.edu.polyu.comp.comp2021.cvfs.model;
/**
 * Represents a criterion that checks if a file is a document.
 * This criterion evaluates to true if and only if the file is an instance of {@link Document}.
 */
public class IsDocumentCriterion implements Criterion {
    @Override
    public boolean evaluate(File file) {
        return file instanceof Document;
    }

    @Override
    public String toString() {
        return "IsDocument";
    }
}
