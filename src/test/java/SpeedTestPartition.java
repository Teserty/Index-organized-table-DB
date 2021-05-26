import IOTTable.App;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

public class SpeedTestPartition {
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
                app.readScript("insert into users_insert_p "+count+" " + count*2);
                count++;
            }
        }
        public void update(){
            while (true){
                randomNum = ThreadLocalRandom.current().nextInt(0, 10000000 + 1);
                app.readScript("update users_update_p "+randomNum+" vals"+randomNum+4);
                count++;
            }
        }
        public void select(){
            while (true){
                randomNum = ThreadLocalRandom.current().nextInt(0, 10000000 + 1);
                app.readScript("select * from users_select_p "+randomNum);
                count++;
            }
        }
        public void delete(){
            while (true){
                randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
                app.readScript("delete from users_delete_p "+randomNum);
                count++;
            }
        }
    }
    @Test
    public void speedInsert() throws Exception{
        app.readScript("create table users_insert_p (id: id, value: int) partition 1000000");
        SpeedTestPartition.TestThread testThread = new SpeedTestPartition.TestThread(0);
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
    @Test
    public void speedSelect() throws Exception{
        app.readScript("create table users_select_p (id: id, value: int) partition 1000000");
        for (int i = 0; i < 10000000; i++){
            app.readScript("insert into users_select_p "+i+" "+i);
        }
        SpeedTestPartition.TestThread testThread = new SpeedTestPartition.TestThread(2);
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
    @Test
    public void speedUpdate() throws Exception{
        app.readScript("create table users_update_p (id: id, value: int) partition 1000000");
        for (int i = 0; i < 10000000; i++){
            app.readScript("insert into users_update_p "+i+" "+i);
        }
        SpeedTestPartition.TestThread testThread = new SpeedTestPartition.TestThread(1);
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
    @Test
    public void speedDelete() throws Exception{
        app.readScript("create table users_delete_p (id: id, value: int)");
        SpeedTestPartition.TestThread testThread = new SpeedTestPartition.TestThread(3);
        for (int i = 0; i < 10000000; i++){
            app.readScript("insert into users_delete_p "+i+" "+i);
        }
        testThread.start();
        sleep(300000);
        testThread.stop();
        System.out.println(testThread.count);
    }
}
