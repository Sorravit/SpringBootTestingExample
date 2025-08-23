package sorravit.example.springboottestingexample.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String s) {
        super(s);
    }
}
