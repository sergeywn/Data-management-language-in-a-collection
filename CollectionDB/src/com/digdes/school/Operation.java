package com.digdes.school;

public enum Operation {
    EQ("="), NEQ("!="), LESS("<"), MORE(">"), LEQ("<="), MEQ(">="), LIKE("like"), ILIKE("ilike");

    private final String op;

    Operation(String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
    }

    public static Operation of(String str) {
        for (Operation operation : values()) {
            if (operation.getOp().equals(str)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Unknown operation '" + str + "'");
    }
}
