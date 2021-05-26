package IOTTable;

import IOTTable.Controllers.Controller;
import IOTTable.Controllers.ControllerInterface;
import IOTTable.Controllers.DiskController;
import IOTTable.Controllers.DiskControllerPartition;
import IOTTable.Helper.Conventor;
import org.junit.jupiter.api.Test;
import uk.co.omegaprime.btreemap.BTreeMap;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    public HashMap<String, ControllerInterface> tables = new HashMap<>();
    Conventor conventor = new Conventor();
    boolean DEBUG = false;
    //"create table users (id: id, value: int) partition 1000000"
    public void createSource(String script) throws Exception{
        String[] lines = script.toLowerCase().split("\\(");
        String[] firstWords = lines[0].split(" ");
        boolean isPartition = script.toLowerCase().contains("partition");
        long partitionSize= 0;
        if (isPartition){
            partitionSize = Long.parseLong(script.split(" ")[script.split(" ").length-1]);
        }
        if (firstWords[0].equals("create") && firstWords[1].equals("table")){
            String name = firstWords[2];
            String[] params = lines[1].split("\\)")[0].replaceAll(" ", "").split(",");
            List<Class<?>> types = new LinkedList<>();
            List<String> names = new LinkedList<>();
            for (String param: params){
                String[] temp = param.split(":");
                names.add(temp[0]);
                types.add(conventor.convert(temp[1]));
            }
            if(isPartition)
                createSource(names,name, types, true, partitionSize);
            else
                createSource(names,name, types, false, 0);
        }
    }
    private void createSource( List<String> names, String name, List<Class<?>> types, boolean isPartition, long size){
        if(isPartition) {
            tables.put(name, new DiskControllerPartition(names, name, types, size));
        }else{
            tables.put(name, new DiskController(names, name, types));
        }
        System.out.println("Table '"+name+"' created");
    }
    public void update(String command) throws Exception{
        String params[] = command.split(" "); //update users id vals
        List<String> list = Arrays.asList(params[3].split(","));
        ControllerInterface controllerInterface = tables.get(params[1]);
        controllerInterface.log(command);
        controllerInterface.update(Long.parseLong(params[2]), list);
    }
    public void insert(String command) throws Exception{
        String params[] = command.split(" "); //insert into users id vals
        List<String> list = Arrays.asList(params[4].split(","));
        ControllerInterface controllerInterface = tables.get(params[2]);
        controllerInterface.log(command);
        controllerInterface.insert(Long.parseLong(params[3]), list);
    }
    public void delete(String command)throws Exception{
        String params[] = command.split(" "); //delete from users id
        ControllerInterface controllerInterface = tables.get(params[2]);
        controllerInterface.log(command);
        controllerInterface.delete(Integer.parseInt(params[3]));
    }
    public String select(String command)throws Exception{
        String params[] = command.split(" "); //select * from users id
        ControllerInterface controllerInterface = tables.get(params[3]);
        controllerInterface.log(command);
        if (params[1].equals("*")){
            return controllerInterface.search(Integer.parseInt(params[4]));
        }else{
            return tables.get(params[params.length-2]).search(Integer.parseInt(params[params.length-1]));
        }
    }
    public void readScript(String readLine) {
        try {
            if(readLine.split(" ")[0].equals("create") && readLine.split(" ")[1].equals("table")){
                createSource(readLine);
            }else if(readLine.split(" ")[0].equals("select")){
                if(DEBUG)
                    select(readLine);
                else
                    System.out.println(select(readLine));
            }else if(readLine.split(" ")[0].equals("update")){
                update(readLine);
            }else if(readLine.split(" ")[0].equals("delete")){
                delete(readLine);
            }else if(readLine.split(" ")[0].equals("insert")){
                insert(readLine);
            }
        }catch (Exception e){
            System.out.println("Invalid command");
            System.out.println(e);
        }
    }
}
