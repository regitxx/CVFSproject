package hk.edu.polyu.comp.comp2021.cvfs.model;
/**
 * Represents a simple criterion for evaluating files based on a single attribute.
 * The supported attributes are "name", "type", and "size".
 */
public class SimpleCriterion implements Criterion {
    private final String name;
    private final String attrName;
    private final String operator;
    private final String value;
    /**
     * Constructs a new simple criterion.
     *
     * @param name      The name of the criterion.
     * @param attrName  The attribute to evaluate ("name", "type", or "size").
     * @param operator  The operator to use for evaluation:
     *                  - "contains" for "name".
     *                  - "equals" for "type".
     *                  - ">", "<", ">=", "<=", "==", "!=" for "size".
     * @param value     The value to compare the attribute against.
     *                  - A string for "name" or "type".
     *                  - An integer (as a string) for "size".
     */
    public SimpleCriterion(String name, String attrName, String operator, String value) {
        this.name = name;
        this.attrName = attrName;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean evaluate(File file) {
        switch (attrName) {
            case "name":
                return file.getName().contains(value);
            case "type":
                return file instanceof Document && ((Document) file).getType().equals(value);
            case "size":
                int fileSize = file.getSize();
                int val = Integer.parseInt(value);
                switch (operator) {
                    case ">": return fileSize > val;
                    case "<": return fileSize < val;
                    case ">=": return fileSize >= val;
                    case "<=": return fileSize <= val;
                    case "==": return fileSize == val;
                    case "!=": return fileSize != val;
                }
                break;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (" + attrName + " " + operator + " " + value + ")";
    }
}
