package pl.test.learn.productdemo.exceptions;

public class NoSuchProductException extends Throwable {
    public NoSuchProductException() {
        super("No such entity for provided data");
    }
}
