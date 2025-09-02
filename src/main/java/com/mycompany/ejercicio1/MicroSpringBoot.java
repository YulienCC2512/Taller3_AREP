package com.mycompany.ejercicio1;

public class MicroSpringBoot {

    public static void main(String[] args) throws Exception {
        int port = 35000;
        BeanContainer context = new BeanContainer();
        WebRouter router = new WebRouter();

        if (args.length > 0) {

            String className = args[0];
            Class<?> clazz = Class.forName(className);
            Object bean = context.getOrCreate(clazz);
            router.register(bean);
        } else {

            ClasspathScanner scanner = new ClasspathScanner();
            for (Class<?> controller : scanner.findRestControllers()) {
                Object bean = context.getOrCreate(controller);
                router.register(bean);
            }
        }

        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                System.err.println("Puerto inválido, se usará el valor por defecto: " + port);
            }
        }

        HttpServer.start(port, router);
    }
}
