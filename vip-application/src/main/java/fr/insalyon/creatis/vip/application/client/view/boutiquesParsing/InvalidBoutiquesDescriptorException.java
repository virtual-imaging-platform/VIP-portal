package fr.insalyon.creatis.vip.application.client.view.boutiquesParsing;

/**
 * Exception for Boutiques descriptor parsing errors.
 */
public class InvalidBoutiquesDescriptorException extends Exception{
    public InvalidBoutiquesDescriptorException(String message) {
        super(message);
    }
    public InvalidBoutiquesDescriptorException(String message, Throwable parent){
        super(message, parent);
    }
}
