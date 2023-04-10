package com.digdes.school;

import java.util.Map;

abstract public class Condition {
    abstract boolean check(Map<String, Object> row) throws Exception;
}
