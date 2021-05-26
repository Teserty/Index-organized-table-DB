package IOTTable.ACID;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Logger {
    private RandomAccessFile[] file;
    private Integer currentFileId = 0;
    private long offset;
    private long size; // in bytes
    private Integer arraySize;
    private String filename;
    public Logger(String filename){
        offset = 0;
        size = 1024*1024*8;
        arraySize = 3;
        this.filename = filename;
        try {
            createFiles();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public Logger(String filename, Integer size){
        offset = 0;
        this.size = size * 1024*1024;
        arraySize = 3;
        this.filename = filename;
    }
    private void createFiles() throws IOException {
        file = new RandomAccessFile[arraySize];
        for (int i = 0; i < arraySize; i++) {
            this.file[i] = new RandomAccessFile("./logger/" + filename+"." + i, "rw");
        }
    }
    public void writeLog(@NotNull String command){
        if (offset + command.getBytes().length > size){
            offset = 0;
            currentFileId = (currentFileId +1)%arraySize;
        }
        try {
            file[currentFileId].seek(offset);
            file[currentFileId].write(command.getBytes());
            offset += command.getBytes().length;
        }catch (Exception ex){
            System.out.println("Logger corrupted");
        }
    }
}
