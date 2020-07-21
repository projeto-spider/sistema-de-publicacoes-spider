package com.spider.auth.email;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.spider.auth.models.Calendar;

public class MailMessages {

	private static String SubmissionMessage(Calendar event) {
        return  "Caro membro do Spider, \n" + 
				"O Spider alerta que a data de submissão do evento " + event.getPlace() + " se aproxima.\n" + 
				"O deadline de submissão é dia " + formatDateToHumans(event.getSubmissionDate()) + " (faltam 15 dias).\n" + 
				"Não esqueça de agilizar a preparação do seu artigo, caso este evento seja do seu interesse.\n" + 
				"Atenciosamente, \n\n" + 
				"SpiderBot.\n";
    }
	
	private static String StartCalendarMessage(Calendar event) {
        return  "Caro membro do Spider, \n" + 
				"O Spider alerta que a data de realização do evento " + event.getPlace() + " se aproxima.\n" + 
				"O data de início do evento é dia " + formatDateToHumans(event.getStartDate()) + " (faltam 30 dias).\n" + 
				"Não esqueça de agilizar a sua inscrição no evento, caso este evento seja do seu interesse.\n" + 
				"Atenciosamente, \n\n" + 
				"SpiderBot.\n";
    }
	
	private static String formatDateToHumans(String date) {
		DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		String formattedDate = date.format(myFormatDate);
		return LocalDate.parse(date).format(myFormatDate).toString();
	}
}
