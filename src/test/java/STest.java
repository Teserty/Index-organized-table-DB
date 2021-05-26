import IOTTable.Controllers.Controller;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class STest extends Thread{
    public BigInteger count = BigInteger.valueOf(0);
    final Controller controller;
    Integer type;
    public STest(Controller controller, Integer type) {
        this.controller = controller;
        this.type = type;
    }

   /* public void run(){
        switch (type){
            case 0:{
                select();
            }
            case 1:{
                insert();
            }
        }
    }
    public int nullPointers = 0;
    public void select(){
        Integer randomNum;
        Integer countd = 500;
        while(countd>0) {
            randomNum = countd;//ThreadLocalRandom.current().nextInt(0, 10000000 + 1);
            try{
                if (controller.printResultIterable(controller.search(randomNum)).get(0).equals(randomNum)){
                    count = count.add(BigInteger.valueOf(1));
                }else{
                    System.out.println(controller.printResultIterable(controller.search(randomNum)).get(0));
                    System.out.println(randomNum);
                }
            }catch (Error e){
                nullPointers++;
                count = count.add(BigInteger.valueOf(1));
            }
            countd--;
        }
    }
    public void insert(){
        while (true){
            try {
                controller.insert(List.of(count.intValue(), 55));
                //treeMap.put(count.intValue(), 55);
                //db.commit();
                count = count.add(BigInteger.valueOf(1));
            }catch (OutOfMemoryError e){
                //System.out.println(e +" "+count);
            }

        }
    }
*/
}
