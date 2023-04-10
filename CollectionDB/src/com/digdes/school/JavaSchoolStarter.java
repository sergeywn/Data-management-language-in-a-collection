package com.digdes.school;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {

    private Controller controller = new Controller();

    public JavaSchoolStarter() {
    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        //начало исполнения кода
        return controller.execute(request);
    }

    public static void main(String[] args) throws Exception {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
//      Добавление, изменение, удаление, поиск строк.
//            List<Map<String, Object>> result = starter.execute("INSERT VALUES 'id'=1, 'lastName' = 'Федоров' , 'age'=null, 'active'=true");
//            System.out.println(result);
//            List<Map<String, Object>> result2 = starter.execute("UPDATE VALUES 'age'=40, 'cost'=10.1 where 'age' != 0");
//            System.out.println(result2);
//            List<Map<String, Object>> result3 = starter.execute("SELECT WHERE 'age'>=30 and 'lastName' ilike '%ф%'");
//            System.out.println(result3);
//            List<Map<String, Object>> result4 = starter.execute("DELETE WHERE 'id'=1");
//            System.out.println(result4);
//            List<Map<String, Object>> result5 = starter.execute("SELECT");
//            System.out.println(result5);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
