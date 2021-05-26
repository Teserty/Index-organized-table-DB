package IOTTable.Controllers;


import IOTTable.BTree.*;
import IOTTable.BTree.utils.io.FileUtils;
import com.github.davidmoten.bplustree.BPlusTree;
import com.github.davidmoten.bplustree.Serializer;
import com.google.gson.Gson;
import org.junit.Assert;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;

public class Controller {
    private final boolean allowDuplicates;
    private List<String> names;
    private final String tableName;
    private List<Class<?>> types;
    private Integer STORE_LIMIT = 3000;
    private Integer STORAGE_SIZE = 100000;
    BTreeIndex tree;
    Gson gson = new Gson();
    public Controller(List<String> names, String tableName, List<Class<?>> types, boolean allowDuplicates) {
        this.names = names;
        this.tableName = tableName;
        this.types = types;
        this.allowDuplicates = allowDuplicates;
        tree = create();
    }

    private BTreeIndex create(){
        File tmpDir = FileUtils.getTempDir();
        Assert.assertTrue(tmpDir.exists());
        File indexFile = new File(tmpDir, tableName+".idx");
        BTreeIndex btree = new BTreeIndex(indexFile, allowDuplicates);
        try {
            btree.init(false);
        } catch (BTreeException e) {
            e.printStackTrace();
        }
        return btree;
    }
    public void update(long key, List<Object> data){

    }
    public void delete(long key){
        try {
            tree.removeValue(new Value(key));
        }catch (Exception e){

        }
    }
    public void insert(long key, List<Object> data) {
        byte[] bytes = ByteBuffer.allocate(4).putInt((Integer) data.get(0)).array();
        Value value = new Value(bytes);
        try {
            tree.addValue(new Value(key), value);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public byte[] search(long key) {
        try {
            return tree.getValueBytes(key);
        }catch (Exception e){
            return null;
        }

    }
    public List convert(List<Object> data){
        List<Object> cData = new LinkedList<>();
        for (int i = 0; i < data.size(); i++){
            if(types.get(i+1).equals(Integer.class)){
                cData.add((Integer)data.get(i));
            }else if(types.get(i+1).equals(String.class)){
                cData.add(String.valueOf(data));
            }else{
                System.out.println(types.get(i+1).toString());
                System.out.println(data.get(i).getClass().toString());
            }
        }
        return cData;
    }
    public List printResultIterable(Iterable<String> iter){
        java.lang.String last = "";
        for(java.lang.String l: iter){
            last= l;
        }
        try{
            return convert(gson.fromJson(last, LinkedList.class));
        }catch (Exception e){
            return new LinkedList();
        }
    }
}
