package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionAnd extends Condition {
    private List<Condition> list = new ArrayList<>();

    public void addCondition(Condition condition) {
        list.add(condition);
    }

    @Override
    boolean check(Map<String, Object> row) throws Exception {
        boolean res = true;
        for (Condition condition : list) {
            boolean val = condition.check(row);
            //System.out.println(""+val+" : "+condition);
            res &= val;
        }
        //System.out.println(""+res+" : "+this);
        return res;
    }

    @Override
    public String toString() {
        return "ConditionAnd{" +
                "list=" + list +
                '}';
    }
}
