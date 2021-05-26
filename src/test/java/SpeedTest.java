import IOTTable.App;
import IOTTable.Controllers.Controller;
import IOTTable.Controllers.DiskController;
import IOTTable.Helper.Conventor;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

public class SpeedTest {
    private static App app = new App();
    class TestThread extends Thread{
        int count = 0;
        int randomNum;
        int type;
        public TestThread(int insert) {
            type = insert;
        }

        public void run(){
            switch (type){
                case 0:{
                    insert();
                };
                case 1:{
                    update();
                }
                case 2:{
                    select();
                }
                case 3:{
                    delete();
                }
            }
        }
        public void insert(){
            while (true){
                app.readScript("insert into users "+count+" " + count*2);
                count++;
            }
        }
        public void update(){
            while (true){
                randomNum = ThreadLocalRandom.current().nextInt(0, 10000000 + 1);
                app.readScript("update users_update "+randomNum+" vals"+randomNum+4);
                count++;
            }
        }
        public void select(){
            while (true){
                randomNum = ThreadLocalRandom.current().nextInt(0, 10000000 + 1);
                app.readScript("select * from users_select "+randomNum);
                count++;
            }
        }
        public void delete(){
            while (true){
                randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
                app.readScript("delete from users "+randomNum);
                count++;
            }
        }
    }
    @Test
    public void speedInsert() throws Exception{
        app.readScript("create table users (id: id, value: int)");
        TestThread testThread = new TestThread(0);
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
    @Test
    public void speedSelect() throws Exception{
        app.readScript("create table users_select (id: id, value: int)");
        for (int i = 0; i < 10000000; i++){
            app.readScript("insert into users_select "+i+" "+i);
        }
        TestThread testThread = new TestThread(2);
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
    @Test
    public void speedUpdate() throws Exception{
        app.readScript("create table users_update (id: id, value: int)");
        for (int i = 0; i < 10000000; i++){
            app.readScript("insert into users_update "+i+" "+i);
        }
        TestThread testThread = new TestThread(1);
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
    @Test
    public void speedDelete() throws Exception{
        app.readScript("create table users_delete (id: id, value: int)");
        TestThread testThread = new TestThread(3);
        for (int i = 0; i < 10000000; i++){
            app.readScript("insert into users_update "+i+" "+i);
        }
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
}
