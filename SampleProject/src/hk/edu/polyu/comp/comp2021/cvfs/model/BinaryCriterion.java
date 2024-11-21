package hk.edu.polyu.comp.comp2021.cvfs.model;
/**
 * Represents a binary criterion that combines two existing criteria using a logical operator.
 * The supported logical operators are "&&" (AND) and "||" (OR).
 */
public class BinaryCriterion implements Criterion {
    private final Criterion left;
    private final Criterion right;
    private final String operator;
    /**
     * Constructs a new binary criterion with the specified left and right criteria, and a logical operator.
     *
     * @param left     The left-hand criterion.
     * @param operator The logical operator to combine the criteria ("&&" for AND, "||" for OR).
     * @param right    The right-hand criterion.
     * @throws IllegalArgumentException If the operator is invalid (not "&&" or "||").
     */
    public BinaryCriterion(Criterion left, String operator, Criterion right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public boolean evaluate(File file) {
        if (operator.equals("&&")) {
            return left.evaluate(file) && right.evaluate(file);
        } else if (operator.equals("||")) {
            return left.evaluate(file) || right.evaluate(file);
        }
        throw new IllegalArgumentException("Invalid operator: " + operator);
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}
