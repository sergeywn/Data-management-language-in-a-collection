package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private Table table = new Table();
    private List<Map<String, Object>> data = new ArrayList<>();

    //Дефолтный конструктор
    public Controller() {
        init();
    }

    private void init() {
        table.addColon("id", Long.class);
        table.addColon("lastName", String.class);
        table.addColon("cost", Double.class);
        table.addColon("age", Long.class);
        table.addColon("active", Boolean.class);
    }

    private Pattern ptrnWord = Pattern.compile("[a-zA-Z]+");

    private String getToken(String line) {
        Matcher matcher = ptrnWord.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    private String unqote(String str) {
        return (str.startsWith("'") && str.endsWith("'"))
                ? str.substring(1, str.length() - 1)
                : str;
    }

    private Object readValue(String str) throws Exception {
        if (str == null || str.isEmpty()) {
            throw new Exception("Value must not by null or empty");
        }
        if (str.startsWith("'") && str.endsWith("'")) {
            return unqote(str);
        }
        if (str.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (str.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (str.indexOf('.') < 0) {
            return Long.valueOf(str);
        }
        return Double.valueOf(str);
    }

    private Object readValue(String str, Class type) throws Exception {
        if (str == null) {
            throw new Exception("Value must not by null");
        }
        if ("null".equalsIgnoreCase(str)) {
            return null;
        }
        if (type == Boolean.class) {
            if (str.equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            } else if (str.equalsIgnoreCase("false")) {
                return Boolean.FALSE;
            } else {
                throw new Exception("Incorrect Boolean value '" + str + "'");
            }
        } else if (type == Long.class) {
            // return Long or throw NumberFormatException
            return Long.valueOf(str);
        } else if (type == Double.class) {
            // return Double or throw NumberFormatException
            return Double.valueOf(str);
        } else if (type == String.class) {
            if (!str.startsWith("'") || !str.endsWith("'")) {
                throw new Exception("String value must be enclosed in ''");
            }
            return unqote(str);
        }
        throw new Exception("Incompatible class: " + type);
    }

    private Map<String, Object> parseValues(String values) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        //System.out.println("values: " + values);
        String[] parts = values.split("\\s*,\\s*");
        for (String item : parts) {
            String[] pair = item.split("\\s*=\\s*");
            if (pair == null || pair.length != 2) {
                throw new Exception("Not found =");
            }
            String name = unqote(pair[0]);
            Colon colon = table.getColon(name);
            if (colon == null) {
                throw new Exception("Field '" + name + "' not found");
            }

            Object value = readValue(pair[1], colon.getType());
            map.put(colon.getName(), value);
        }
        return map;
    }

    private Pattern ptrnCond = Pattern.compile(
            "'(\\w+)'\\s*(=|!=|like|ilike|>=|<=|>|<)\\s*([^!><=']+|'.+')",
            Pattern.CASE_INSENSITIVE);

    private Condition parseOneCondition(String str) throws Exception {
        Matcher m = ptrnCond.matcher(str);
        if (m.matches()) {
            String name = m.group(1);
            String op = m.group(2).toLowerCase();
            String value = m.group(3);
            //System.out.println("name=[" + name + "], op=[" + op + "], value=[" + value + "]");

            Colon colon = table.getColon(name);
            if (colon == null) {
                throw new Exception("Unknown field: " + name);
            }
            Object val = readValue(value);

            return new ConditionOne(colon, Operation.of(op), val);
        }
        throw new Exception("Incorrect condition: " + str);
    }

    private Condition parseGroupAnd(String where) throws Exception {
        if (where == null || where.isBlank()) {
            throw new Exception("Incorrect WHERE format");
        }
        String[] byAnd = where.split("\\s+(a|A)(n|N)(d|D)\\s+");
        if (byAnd.length == 1) {
            return parseOneCondition(byAnd[0]);
        }
        ConditionAnd andGroup = new ConditionAnd();
        for (String str : byAnd) {
            andGroup.addCondition(parseOneCondition(str));
        }
        return andGroup;
    }

    private Condition parseWhere(String where) throws Exception {
        //System.out.println("where: " + where);
        if (where == null || where.isBlank()) {
            throw new Exception("Incorrect WHERE format");
        }
        String[] byOr = where.split("\\s+(o|O)(r|R)\\s+");
        if (byOr.length == 1) {
            return parseGroupAnd(byOr[0]);
        }
        ConditionOr orGroup = new ConditionOr();
        for (String str : byOr) {
            //System.out.println("or: " + str);
            orGroup.addCondition(parseGroupAnd(str));
        }
        return orGroup;
    }

    private Request parse(String line) throws Exception {
        Request res = new Request();

        String cmd = getToken(line);
        if (cmd == null) {
            throw new Exception("Command not found");
        }
        // set Command or throw IllegalArgumentException
        res.setCommand(Command.valueOf(cmd.toUpperCase()));

        line = line.substring(cmd.length()).trim();

        // find VALUES
        String token = getToken(line);
        if (token != null && token.equalsIgnoreCase("values")) {
            String str = line.toLowerCase();
            int wherePos = str.indexOf("where");
            String valuesStr = (wherePos < 0)
                    ? line.substring(6)
                    : line.substring(6, wherePos);
            valuesStr = valuesStr.trim();
            line = (wherePos < 0) ? "" : line.substring(wherePos);
            res.setValues(parseValues(valuesStr));
        }
        // find WHERE
        token = getToken(line);
        if (token != null && token.equalsIgnoreCase("where")) {
            line = line.substring(5).trim();
            res.setWhere(parseWhere(line));
        }
        return res;
    }

    private List<Map<String, Object>> selectRows(Condition condition) throws Exception {
        List<Map<String, Object>> res = new ArrayList<>();
        if (condition == null) {
            res.addAll(data);
        } else {
            for (Map<String, Object> row : data) {
                if (condition.check(row)) {
                    res.add(row);
                }
            }
        }
        return res;
    }

    private List<Map<String, Object>> doInsert(Request req) throws Exception {
        if (req.getValues() == null || req.getValues().size() == 0) {
            throw new Exception("No data to insert");
        }
        data.add(req.getValues());
        return List.of(req.getValues());
    }

    private List<Map<String, Object>> doDelete(Request req) throws Exception {
        List<Map<String, Object>> res = selectRows(req.getWhere());
        data.removeAll(res);
        return res;
    }

    private List<Map<String, Object>> doSelect(Request req) throws Exception {
        List<Map<String, Object>> res = selectRows(req.getWhere());
        return res;
    }

    private List<Map<String, Object>> doUpdate(Request req) throws Exception {
        List<Map<String, Object>> res = selectRows(req.getWhere());
        Map<String, Object> values = req.getValues();
        for (Map<String, Object> row : res) {
            for (String key : values.keySet()) {
                Object val = values.get(key);
                if (val == null) {
                    row.remove(key);
                } else {
                    row.put(key, val);
                }
            }
        }
        return res;
    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        //Здесь начало исполнения кода
        Request req = parse(request);
        switch (req.getCommand()) {
            case INSERT:
                return doInsert(req);
            case DELETE:
                return doDelete(req);
            case SELECT:
                return doSelect(req);
            case UPDATE:
                return doUpdate(req);
            default:
                throw new Exception("Unknown command: " + req.getCommand());
        }
    }

}
