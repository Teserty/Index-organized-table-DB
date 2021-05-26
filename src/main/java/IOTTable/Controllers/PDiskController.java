package IOTTable.Controllers;

import java.nio.ByteBuffer;
import java.util.List;

public abstract class PDiskController implements ControllerInterface {
    protected List<String> names;
    protected final String tableName;
    protected Integer size;
    protected List<Class<?>> types;

    protected PDiskController(List<String> names, String tableName, List<Class<?>> types) {
        this.names = names;
        this.tableName = tableName;
        this.types = types;
        this.size = getSize();
    }

    byte[] toByte(List<String> data){
        byte[] bytes = new byte[size];
        for (int i=0; i < types.subList(1, types.size()).size(); i++){
            if (Integer.class.equals(types.get(i+1))) {
                byte[] b = ByteBuffer.allocate(4).putInt(Integer.parseInt(data.get(i))).array();
                System.arraycopy(b, 0, bytes, i * 4, 4);
            } else if(Double.class.equals(types.get(i+1))){
                byte[] b = ByteBuffer.allocate(8).putDouble(Double.parseDouble(data.get(i))).array();
                System.arraycopy(b, 0, bytes, i * 8, 8);
            }else if(Long.class.equals(types.get(i+1))){
                byte[] b = ByteBuffer.allocate(8).putLong(Long.parseLong(data.get(i))).array();
                System.arraycopy(b, 0, bytes, i * 8, 8);
            }
        }

        return bytes;
    }
    String convert(long key, byte[] reade) {
        StringBuilder line = new StringBuilder("" + key);
        for (int i = 0; i < types.subList(1, types.size()).size(); i++) {
            if (Integer.class.equals(types.get(i + 1))) {
                line.append(" ").append(ByteBuffer.wrap(
                        new byte[]{reade[i], reade[i + 1], reade[i + 2], reade[i + 3]}).getInt());
            } else if (Double.class.equals(types.get(i + 1))) {
                line.append(" ").append(ByteBuffer.wrap(new byte[]{
                        reade[i], reade[i + 1], reade[i + 2], reade[i + 3],
                        reade[i + 4], reade[i + 5], reade[i + 6], reade[i + 7]
                }).getDouble());
            } else if (Long.class.equals(types.get(i + 1))) {
                line.append(" ").append(ByteBuffer.wrap(new byte[]{
                        reade[i], reade[i + 1], reade[i + 2], reade[i + 3],
                        reade[i + 4], reade[i + 5], reade[i + 6], reade[i + 7]
                }).getLong());
            }
        }
        return line.toString();
    }

    private Integer getTypeSize(Class<?> type){
        if (Integer.class.equals(type)) {
            return 4;
        } else if(Double.class.equals(type)){
            return 8;
        } else if(Long.class.equals(type)){
            return 8;
        }
        return -1;
    }
    public int getSize(){
        Integer size = 0;
        for (Class<?> type: types.subList(1, types.size())){
            size += getTypeSize(type);
        }
        return size;
    }
}
