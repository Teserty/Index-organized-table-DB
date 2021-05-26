package IOTTable.Controllers;

import IOTTable.ACID.Logger;
import IOTTable.BTree.BTreeException;
import IOTTable.BTree.BTreeIndex;
import IOTTable.BTree.Value;
import uk.co.omegaprime.btreemap.BTreeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class DiskControllerPartition extends PDiskController {
    private Long keyl = Long.valueOf(0);
    private Integer size;
    private Long partitionSize;
    Logger log;
    ArrayList<BTreeIndex> mapTree = new ArrayList<>();
    RandomAccessFile file;
    public DiskControllerPartition(List<String> names, String tableName, List<Class<?>> types, long size){
        super(names, tableName, types);
        log = new Logger(tableName);
        partitionSize = size;
        try {
            file = new RandomAccessFile(tableName+"_partition", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.size = getSize();
    }
    private void createTree(long id){
        BTreeIndex tree = new BTreeIndex(new File(tableName + ".idx" + id), false);
        try {
            tree.init(false);
        } catch (BTreeException e) {
            e.printStackTrace();
        }
        mapTree.add((int) id, tree);
        //try {
        //    mapFile.put(id, new RandomAccessFile(tableName+id, "rw"));
        //}catch (FileNotFoundException e){
        //    try {
        //        File f = new File(tableName+id);
        //        mapFile.put(id, new RandomAccessFile(f, "rw"));
        //    }catch (Exception ignored){
//
        //    }
        //}
    }
    @Override
    public void log(String command){
        log.writeLog(command);
    }
    @Override
    public void update(long key, List<String> data){
        BTreeIndex tree = mapTree.get((int) ((int)key/partitionSize));
        if (tree == null){
            createTree(key/partitionSize);
        }
        try {
            file.seek(mapTree.get((int) ((int)key/partitionSize)).findValue(new Value(key)));
            file.write(toByte(data));
        }catch (Exception e){

        }
    }
    @Override
    public void delete(long key){
        try {
            mapTree.get((int) ((int)key/partitionSize)).remove(new Value(key));
        } catch (BTreeException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void insert(long key, List<String> data) {
        BTreeIndex tree;
        try {
            tree= mapTree.get((int) ((int)key/partitionSize));
        }catch (IndexOutOfBoundsException e){
            createTree(key/partitionSize);
            tree= mapTree.get((int) ((int)key/partitionSize));
        }
        long position = keyl*size;
        try {
            tree.addValue(new Value(key), position);
        } catch (BTreeException e) {
            e.printStackTrace();
        }
        try {
            file.seek(position);
            file.write(toByte(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyl++;
    }
    @Override
    public String search(long key) {
        BTreeIndex tree = mapTree.get((int) ((int)key/partitionSize));
        if (tree == null){
            createTree(key/partitionSize);
        }
        byte[] bytes = new byte[0];
        try {
            file.seek(mapTree.get((int) ((int)key/partitionSize)).findValue(new Value(key)));
            bytes = new byte[size];
            file.read(bytes);
        }catch (Exception ignored){

        }
        return convert(key, bytes);
    }
    public void close(){
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//create table users (id: id, age: int, money: int)
//Table 'users' created
//insert into users 4 6,8
//select * from users 4