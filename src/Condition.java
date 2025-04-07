/**
 * A condition is of the form operand1 operator operand2, e.g. sid = s1
 */
public class Condition {
    private String operand1;
    private String operand2;
    private String operator;

    /**
     * constructor
     * @param operand1 the first given operand.
     * @param operand2 the second given operand.
     * @param operator the given operator.
     */
    public Condition(String operand1, String operand2, String operator) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operator = operator;
    }

    /**
     * Getters and setters
     */
    public String getOperand1() { return this.operand1; }
    public void setOperand1(String operand1) { this.operand1 = operand1; }

    public String getOperand2() { return this.operand2; }
    public void setOperand2(String operand2) { this.operand2 = operand2; }

    public String getOperator() { return this.operator; }
    public void setOperator(String operator) { this.operator = operator; }
}
