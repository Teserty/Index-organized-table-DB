import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import IOTTable.Controllers.Controller;
import IOTTable.Helper.Conventor;
import org.junit.Test;

public final class TreeTest {
    public HashMap<String, Controller> tables = new HashMap<>();
    Conventor conventor = new Conventor();
    public void createSource(String script){
        String[] lines = script.toLowerCase().split("\\(");
        String[] firstWords = lines[0].split(" ");
        if (firstWords[0].equals("create") && firstWords[1].equals("table")){
            String name = firstWords[2];
            String[] params = lines[1].replaceAll("\\)", "").replaceAll(";", "").replaceAll(" ", "").split(",");
            List<Class<?>> types = new LinkedList<>();
            List<String> names = new LinkedList<>();
            for (String param: params){
                String[] temp = param.split(":");
                names.add(temp[0]);
                types.add(conventor.convert(temp[1]));
            }
            createSource(names,name, types);
        }
    }
    private void createSource( List<String> names, String name, List<Class<?>> types){
        tables.put(name, new Controller(names, name, types, false));
    }
    @Test
    public void test(){
        createSource("create table users (id: id, value: int)");
        Controller c = tables.get("users");
        for (int i = 0; i < 1000000; i++){
            c.insert(i, Collections.singletonList(i));
        }
        for(int i = 666; i < 16874631; i++){
            ByteBuffer wrapped = ByteBuffer.wrap(c.search(i));
            System.out.println( wrapped.getInt()+ " " + i);
        }
    }

}