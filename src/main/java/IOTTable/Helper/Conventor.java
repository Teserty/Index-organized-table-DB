package IOTTable.Helper;

import IOTTable.BTree.Value;

import java.sql.Date;

public class Conventor {
    public Class<?> convert(String type){
        Class<?> rclass;
        switch (type){
            case "int":
                rclass = Integer.class;
                break;
            case "str":
                rclass = String.class;
                break;
            case "date":
                rclass = Date.class;
                break;
            case "id":
                rclass = Long.class;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return rclass;
    }
}
