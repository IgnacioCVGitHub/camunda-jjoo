package clases.exec;

import java.util.logging.Logger;
import java.awt.Desktop;
import java.net.URI;

import org.camunda.bpm.client.ExternalTaskClient;

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
	    	
	  }
}
