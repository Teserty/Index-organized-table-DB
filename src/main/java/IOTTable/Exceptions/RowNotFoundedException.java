package IOTTable.Exceptions;

public class RowNotFoundedException extends RuntimeException {
    String text;

    public RowNotFoundedException(String s) {
        text=s;
    }
}
