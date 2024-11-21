package hk.edu.polyu.comp.comp2021.cvfs.model;
import java.io.Serializable;
/**
 * Represents a general criterion for evaluating files in the virtual file system.
 * This interface provides methods to evaluate a file against the criterion and to
 * represent the criterion as a string.
 */
public interface Criterion extends Serializable {
    /**
     * Evaluates the criterion on the specified file.
     *
     * @param file The file to evaluate.
     * @return True if the file satisfies the criterion, otherwise false.
     */
    boolean evaluate(File file);
    String toString();
}

