package IOTTable.Controllers;

import IOTTable.ACID.Logger;
import IOTTable.BTree.BTree;
import IOTTable.BTree.BTreeException;
import IOTTable.BTree.BTreeIndex;
import IOTTable.BTree.Value;
import uk.co.omegaprime.btreemap.BTreeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.TreeMap;

public class DiskController extends PDiskController {
    private Long keyl = Long.valueOf(0);
    private Integer size;
    BTreeMap<Long, Long> tree;
    RandomAccessFile file;
    Logger log;
    BTreeIndex btree;
    public DiskController(List<String> names, String tableName, List<Class<?>> types){
        super(names, tableName, types);
        tree = BTreeMap.create(new TreeMap<>());
        size = getSize();
        //boolean duplicateAllowed = false;
        log = new Logger(tableName);
        btree = new BTreeIndex(new File(tableName+".index"), false);
        try {
            btree.init(false);
        } catch (BTreeException e) {
            e.printStackTrace();
        }
        try {
            file = new RandomAccessFile(tableName, "rw");
        }catch (FileNotFoundException e){
            try {
                File f = new File(tableName);
                file = new RandomAccessFile(f, "rw");
            }catch (Exception ignored){

            }
        }
    }

    @Override
    public void update(long key, List<String> data){
        try {
            //file.seek(tree.get(key));
            file.seek(btree.findValue(new Value(key)));
            file.write(toByte(data));
        }catch (Exception e){

        }
    }
    @Override
    public void log(String command){
        log.writeLog(command);
    }
    @Override
    public void delete(long key){
        //tree.remove(key);
        try {
            btree.removeValue(new Value(key));
        }catch (Exception e){

        }
    }
    @Override
    public void insert(long key, List<String> data) {
        long position = keyl*size;
        //tree.put(key, position);
        try {
            btree.addValue(new Value(key), position);
        } catch (BTreeException e) {
            e.printStackTrace();
        }
        try {
            file.seek(position);
            byte[] bytes = toByte(data);
            file.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyl++;
    }
    @Override
    public String search(long key) {
        byte[] bytes = new byte[0];
        try {
            //file.seek(tree.get(key));
            file.seek(btree.findValue(new Value(key)));
            bytes = new byte[size];
            file.read(bytes);
        }catch (Exception e){

        }
        if(bytes.length == 0)
            return "";
        else
            return convert(key, bytes);
    }
    public void close() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
