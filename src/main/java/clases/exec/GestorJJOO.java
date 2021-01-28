package clases.exec;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.awt.Desktop;
import java.net.URI;

import org.camunda.bpm.client.ExternalTaskClient;
import org.passay.*;

public class GestorJJOO {
	private final static Logger LOGGER = Logger.getLogger(GestorJJOO.class.getName());

	  public static void main(String[] args) {
	    ExternalTaskClient client = ExternalTaskClient.create()
	        .baseUrl("http://localhost:8080/engine-rest")
	        .asyncResponseTimeout(10000) // long polling timeout
	        .build();

	    // subscribe to an external task topic as specified in the process
	    client.subscribe("coi-transfer")
	        .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
	        .handler((externalTask, externalTaskService) -> {
	          // Put your business logic here

	          // Get a process variable
	        	Long cuenta = (Long) externalTask.getVariable("numerocuenta");
	          String asunto = (String) externalTask.getVariable("asunto");
	          Long cantidad = (Long) externalTask.getVariable("cantidad");

	          LOGGER.info("Realizando transferencia a la cuenta" + cuenta + " con asunto " + asunto + " por valor de " + cantidad+ "€ ...");
	          LOGGER.info("Transferencia realizada");     

	          // Complete the task
	          externalTaskService.complete(externalTask);
	        })
	        .open();
	    
	    client.subscribe("send-docs")
        .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
        .handler((externalTask, externalTaskService) -> {
          // Put your business logic here
        	
          // Get a process variable
        	SendMail sendMail = new SendMail();
        	String mail= (String) externalTask.getVariable("mail");
        	String user = (String) externalTask.getVariable("user");
            String asunto =  "Enlace a documentación";
            String cuerpo = "Estimado candidato:   \n\n Se le facilitará a continuación un enlace "
            		+ "a la documentación generada hasta el momento:\n"
            		+ "https://drive.google.com/drive/folders/0B1qoi1IlEKwaM2tSMFBmOGUyNzg"; 

	          try {
				sendMail.sendMail(mail, user, asunto, cuerpo);
			} catch (Exception e) {
				LOGGER.warning("Correo no enviado. Error");
			}   

          // Complete the task
          externalTaskService.complete(externalTask);
        })
        .open();
	    	
	    
	    client.subscribe("grant-access")
        .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
        .handler((externalTask, externalTaskService) -> {
        	String user = (String) externalTask.getVariable("user");
        	
        	//generación de contraseña para dar acceso a PCGJJOO
        	List rules = Arrays.asList(new CharacterRule(EnglishCharacterData.UpperCase, 1),
    				new CharacterRule(EnglishCharacterData.LowerCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1),new CharacterRule(EnglishCharacterData.Special, 1));
        	
        	LOGGER.info("generando contraseña para el usuario" + user +" ...");

    		PasswordGenerator generator = new PasswordGenerator();
        	String passwd= generator.generatePassword(8, rules);
        	
        	LOGGER.info("contraseña generada: " + user +" - "+passwd);
        })
        .open();
	  }
}
