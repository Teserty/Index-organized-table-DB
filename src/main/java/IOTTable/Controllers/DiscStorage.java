package IOTTable.Controllers;

import uk.co.omegaprime.btreemap.BTreeMap;
import javax.xml.bind.JAXB;
import java.io.*;
import java.util.*;

public class DiscStorage /*implements ControllerInterface, Serializable*/ {
    /*Long MAX_SIZE;
    public final Integer chunk;
    final String tableName;
    Integer size=0;
    public Integer usageCount = 0;
    public BTreeMap<Comparable, List> tree;
    public DiscStorage(Long MAX_SIZE, Integer chunk, String tableName){
        this.MAX_SIZE = MAX_SIZE;
        this.chunk = chunk;
        this.tableName = tableName;
        readFromFile();
    }*/
    /*@Override
    public void update(Comparable key, List<Object> val) {
        tree.remove(key);
        tree.put(key, val);
        usageCount++;
    }

    @Override
    public void delete(Comparable key) {
        tree.remove(key);
        usageCount++;
    }

    @Override
    protected void finalize() throws Throwable {
        writeToFile();
        super.finalize();
    }

    @Override
    public void insert(List<Object> data) {
        tree.put((Comparable) data.get(0), data.subList(1, data.size()));
        size++;
        usageCount++;
    }

    @Override
    public List search(Comparable key) {
        usageCount++;
        return (List) tree.get(key);
    }
    public void readFromFile(){
        try {
            FileInputStream fileInputStream
                    = new FileInputStream("./tables/"+tableName+"/"+chunk+".txt");
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            Gson gson = new Gson();
            TreeMap loadedTree = new TreeMap<Comparable, List>();
            loadedTree = gson.fromJson((String)objectInputStream.readObject(), loadedTree.getClass());
            //loadedTree.keySet();
            //tree = BTreeMap.create(loadedTree);
            tree = BTreeMap.create(loadedTree);
            objectInputStream.close();
        }catch (Exception e){
            tree = BTreeMap.create(new TreeMap<>());
        }
    }
    public void writeToFile() {
        try {
            File theDir = new File("./tables/" + tableName + "/");
            theDir.mkdirs();
            FileOutputStream fileOutputStream
                    = new FileOutputStream("./tables/" + tableName + "/" + chunk + ".txt");
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            //List<List> elems = new LinkedList<>();
            //for(int i=0; i<tree.keySet().toArray().length; i++){
            //    List<Object> newlist = new LinkedList<>();
            //    newlist.add(List.of(tree.keySet().toArray()[i]));
            //    List list = (List) tree.values().toArray()[i];
            //    newlist.addAll(list);
            //    elems.add(newlist);
            //}
            Gson gson = new Gson();
            String templateJson = gson.toJson(tree, tree.getClass());
            objectOutputStream.writeObject(templateJson);
            objectOutputStream.flush();
            objectOutputStream.close();
        }catch (Exception e){
            System.out.print("Exception on Saving");
            System.out.print(e);
        }
        //System.out.print(tree.toString());
    }*/
}
