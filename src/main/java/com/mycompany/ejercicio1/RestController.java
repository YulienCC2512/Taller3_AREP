package com.mycompany.ejercicio1;

import java.lang.annotation.*;

/**
 * Anotación personalizada que marca una clase como un controlador REST.
 *
 * Esta anotación actúa de forma similar a la de Spring Boot (@RestController),
 * indicando que la clase está destinada a manejar solicitudes HTTP.
 *
 * Uso:
 *
 * @RestController
 * public class MiControlador {
 *     // Métodos que manejarán endpoints
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {}
