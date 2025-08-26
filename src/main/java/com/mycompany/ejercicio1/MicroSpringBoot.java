package main.java.com.mycompany.ejercicio1;
public class MicroSpringBoot {

    public static void main(String[] args){
        try{
            HttpServer.runServer(args);
        } catch (IOEsception ex){
            logger.getLogger(MicroSprinBoot.class.getName()).log(Level.Severe, null, ex);
        } catch (URISyntaxException ex){
            Logger.getLogger(MicroSprinBoot.class.getName()).log(level.SEVERE, null, ex);
        }
    }
}
