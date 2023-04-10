package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionOr extends Condition {
    private List<Condition> list = new ArrayList<>();

    public void addCondition(Condition condition) {
        list.add(condition);
    }

    @Override
    boolean check(Map<String, Object> row) throws Exception {
        for (Condition condition : list) {
            if (condition.check(row)) {
                //System.out.println("TRUE : "+condition);
                return true;
            } else {
                //System.out.println("FALSE : "+condition);
            }
        }
        //System.out.println("FALSE : "+this);
        return false;
    }

    @Override
    public String toString() {
        return "ConditionOr{" +
                "list=" + list +
                '}';
    }
}
