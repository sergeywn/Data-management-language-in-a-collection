package com.digdes.school;

import java.util.Map;

public class ConditionOne extends Condition {

    private Colon name;
    private Operation operation;
    private Object value;

    public ConditionOne(Colon name, Operation operation, Object value) {
        this.name = name;
        this.operation = operation;
        this.value = value;
    }


    private void probeNumeric(Object o1, Object o2) throws Exception {
        if (o1 == null || o2 == null) {
            throw new Exception("Must not be null");
        }

        if (o1.getClass() != Long.class && o1.getClass() != Double.class) {
            throw new Exception("Must be numeric: " + o1);
        }

        if (o2.getClass() != Long.class && o2.getClass() != Double.class) {
            throw new Exception("Must be numeric: " + o2);
        }
    }

    private void probeString(Object o1, Object o2) throws Exception {
        if (o1 == null || o2 == null) {
            throw new Exception("Must not be null");
        }

        if (o1.getClass() != String.class) {
            throw new Exception("Must be String: " + o1);
        }

        if (o2.getClass() != String.class) {
            throw new Exception("Must be String: " + o2);
        }
    }

    private boolean like(String pattern, String value) {
        boolean startFlag = pattern.startsWith("%");
        boolean endFlag = pattern.endsWith("%");
        if (!startFlag && !endFlag) {
            return value.equals(pattern);
        }
        if (startFlag && endFlag) {
            return value.contains(pattern.substring(1, pattern.length() - 1));
        }
        if (startFlag) {
            return value.endsWith(pattern.substring(1));
        }
        if (endFlag) {
            return value.startsWith(pattern.substring(0, pattern.length() - 1));
        }
        return false;
    }

    @Override
    boolean check(Map<String, Object> row) throws Exception {
        Object rowValue = row.get(name.getName());
        //System.out.println("rowValue = "+rowValue);
        switch (operation) {
            case EQ:
                return (value == null && rowValue == null) ||
                        (value != null && rowValue != null &&
                                value.getClass() == rowValue.getClass() && value.equals(rowValue)
                        );
            case NEQ:
                return (value == null && rowValue != null) ||
                        (value != null && rowValue == null) ||
                        (
                                (value != null && rowValue != null) &&
                                        (value.getClass() != rowValue.getClass() || !value.equals(rowValue))
                        );
            case LESS:
                probeNumeric(value, rowValue);
                return ((Number) rowValue).doubleValue() < ((Number) value).doubleValue();
            case MORE:
                probeNumeric(value, rowValue);
                return ((Number) rowValue).doubleValue() > ((Number) value).doubleValue();
            case LEQ:
                probeNumeric(value, rowValue);
                return ((Number) rowValue).doubleValue() <= ((Number) value).doubleValue();
            case MEQ:
                probeNumeric(value, rowValue);
                return ((Number) rowValue).doubleValue() >= ((Number) value).doubleValue();
            case LIKE:
                probeString(value, rowValue);
                return like((String) value, (String) rowValue);
            case ILIKE:
                probeString(value, rowValue);
                return like(((String) value).toLowerCase(), ((String) rowValue).toLowerCase());
            default:
                throw new Exception("Unknown operation: " + operation);
        }
    }

    @Override
    public String toString() {
        return "ConditionOne{" +
                "name='" + name + '\'' +
                ", operation='" + operation + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
