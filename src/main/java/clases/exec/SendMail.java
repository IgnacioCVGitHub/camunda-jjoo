package clases.exec;

import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class SendMail {
	final static Logger LOGGER = Logger.getLogger(SendMail.class.getName());
	
	private static final String HOST = "smtp.gmail.com";
	private static final String USER = "camunda.jjoo@gmail.com";
	private static final String PWD = "klhwfosynivajtxp";
	private static final int PORT = 465;
	
	public void sendMail(String mail, String user, String asunto, String cuerpo) {
		if (mail!= null && !mail.isEmpty()) {
			Email email = new SimpleEmail();
			email.setStartTLSEnabled(true);
			email.setSSLOnConnect(true);
			email.setHostName(HOST);
			email.setAuthenticator(new DefaultAuthenticator(USER, PWD));
			email.setSmtpPort(PORT);
			email.setStartTLSEnabled(true);
			try {
				email.setFrom(USER,"[GPS]");
				email.setSubject(asunto);
				email.setMsg(cuerpo);
				email.addTo(mail,user);
				email.send();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warning("Error al enviar el correo: "+e.getMessage());
			}
		}
	}
	
}
