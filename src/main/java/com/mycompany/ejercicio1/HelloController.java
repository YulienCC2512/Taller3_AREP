package main.java.com.mycompany.ejercicio1;
@RestController
public class HelloController {

    /GetMapping("\hello")
    public static String index() {
        return "Greetings from Spring boot: ";
    }
}
