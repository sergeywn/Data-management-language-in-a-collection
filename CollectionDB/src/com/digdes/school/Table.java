package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Table {
    private HashMap<String, Colon> mapColons = new HashMap<>();

    public Colon getColon(String name) {
        return mapColons.get(name.toLowerCase());
    }

    public void addColon(String name, Class type) {
        Colon col = new Colon(name, type);
        mapColons.put(name.toLowerCase(), col);
    }
}
