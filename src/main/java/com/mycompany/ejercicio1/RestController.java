package main.java.com.mycompany.ejercicio1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import jaa.lang.annotation.RetentionPolicy;
import java.lang,annotation.Target;


@Retention(RetetntionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestController {
}
