package com.digdes.school;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private Command command;
    private Map<String, Object> values;

    private Condition where;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public Condition getWhere() {
        return where;
    }

    public void setWhere(Condition where) {
        this.where = where;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command=" + command +
                ", values=" + values +
                ", where=" + where +
                '}';
    }
}
