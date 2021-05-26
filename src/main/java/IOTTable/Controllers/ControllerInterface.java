package IOTTable.Controllers;

import IOTTable.BTree.Value;

import java.util.List;

public interface ControllerInterface{
    void update(long key, List<String> val);

    void delete(long key);
    void log(String command);
    void insert(long key, List<String> data);
    String search(long key);
}
