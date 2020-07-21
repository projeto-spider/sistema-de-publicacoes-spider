package com.spider.auth.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.spider.auth.models.Calendar;
import com.spider.auth.models.User;
import com.spider.auth.models.Vehicle;
import com.spider.auth.models.Approved;
import com.spider.auth.repository.CalendarRepository;
import com.spider.auth.repository.ApprovedRepository;
import com.spider.auth.repository.VehicleRepository;
import com.spider.auth.repository.UserRepository;
import com.spider.auth.email.MailService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RegistrationController {
        @Autowired
	VehicleRepository vehicleRepository;
        
        @Autowired
	ApprovedRepository approvedRepository;

	@Autowired
	private MailService notificationService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	CalendarRepository eventRepository;

	public List<User> generateUserlist() {
		List<User> users = new ArrayList<User>();
		userRepository.findAll().forEach(users::add);
		return users;
	}
	public List<Calendar> generateStartCalendarList(){
		List<Calendar> events = eventRepository.findByStartReminded(false);
		return events;
	}
	public List<Calendar> generateDeadlineCalendarList(){
		List<Calendar> events = eventRepository.findByDeadlineReminded(false);
		return events;
	}
	public static String DateToString(LocalDateTime date) {
		DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = date.format(myFormatDate);
		return formattedDate;
	}
	static public String MinusDays(String date, int minusDays) {
		return LocalDate.parse(date).minusDays(minusDays).toString();
	}

	@RequestMapping("sendMail")
//	@Scheduled(cron="*/30 * * * * *") //cron teste, é executdo a cada 30 segundos.
	@Scheduled(cron="0 0 */1 * * *") //cron produção, é executado às 8:00h e às 20;00h
	public String sendDeadlineMail() {
		System.out.println("iniciando DeadlineEmail...");
		List<Calendar> events = generateDeadlineCalendarList(); 
		List<User> users = generateUserlist();
		for(Calendar event : events) {

			String deadline = event.getSubmissionDate();
			String today = DateToString(LocalDateTime.now());			
			String deadlineEmail = MinusDays(event.getSubmissionDate(),15);

			System.out.println("hoje é " + today);
			System.out.println("a data de submissão é " + deadline);
			System.out.println("a data para enviar o email é " + deadlineEmail);
			System.out.println("o Local é " + event.getPlace());

			if(today.compareTo(deadlineEmail) == 0) {
				for (User user: users) {
					try {
						notificationService.sendEmail(user, event, true);
					} catch (MailException mailException) {
						System.out.println(mailException);
					}
					markCalendar(event, true, false);
				}
			}
			System.out.println();
		}
		return "Email enviado.";
	}

//	@Scheduled(cron="*/30 * * * * *") //cron teste, é executdo a cada 30 segundos.
	@Scheduled(cron="0 0 */1 * * *") //cron produção, é executado às 8:00h e às 20;00h
	public String sendStartCalendarMail() {
		System.out.println("iniciando StartEventEmail...");
		List<Calendar> events = generateStartCalendarList();
		List<User> users = generateUserlist();
		for(Calendar event : events) {	

			String eventday = event.getStartDate();
			String eventEmail = MinusDays(event.getStartDate(),30);
			String today = DateToString(LocalDateTime.now());

			System.out.println("hoje é " + today);
			System.out.println("a data para lembrar da inicio do evento é " + eventEmail);
			System.out.println("a data do início do evento é " + eventday);
			System.out.println("o Local é " + event.getPlace());

			if(today.compareTo(eventEmail) == 0){
				for (User user: users) {
					try {
						notificationService.sendEmail(user, event, false);
					} catch (MailException mailException) {
						System.out.println(mailException);
					}
					markCalendar(event, false, true);
				}
			}
			System.out.println();
		}
		return "Email enviado.";
	}

	//marca o evento para que não seja chamado depois
	public void markCalendar(Calendar event, boolean d, boolean s) {
		Optional<Calendar> eventData = eventRepository.findById(event.getId());
		Calendar _event = eventData.get();
		_event.setStartDate(event.getStartDate());
		_event.setEndDate(event.getEndDate());
		_event.setSubmissionDate(event.getSubmissionDate());
		_event.setPlace(event.getPlace());
		if(d) {
			_event.setDeadlineReminded(true);
		}else {
			_event.setDeadlineReminded(event.isDeadlineReminded());
		}
		if(s) {
			_event.setStartReminded(true);
		}else {
			_event.setStartReminded(event.isStartReminded());
		}
		eventRepository.save(_event);
	}
        private static String formatDateToHumans(String date) {
		DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		String formattedDate = date.format(myFormatDate);
		return LocalDate.parse(date).format(myFormatDate).toString();
	}

	//Envia email simples
	@RequestMapping("sendSimpleEmail/{vehicleName}/{revisionDate}")
	public String sendSimpleMail(@RequestBody User user, @PathVariable("vehicleName") String vehicleName, @PathVariable("revisionDate") String revisionDate) throws MessagingException {
		String emailAdress = user.getEmail(); //Receiver's email address
		
                String subject = "Correção de Artigo - " + vehicleName;
                String body = "Caro membro do Spider, \n"
                        + "O Spider alerta que existe um artigo a ser corrigido para versão final por você no veículo " + vehicleName + "\n"
                        + "A data de revisão interna para encaminhamento ao professor é dia " + formatDateToHumans(revisionDate) + "\n"
                        + "Não esqueça de agilizar a correção do seu artigo dentro da data estabelecida\n"
                        + "Atenciosamente,\n    Spider Bot";
                        
     
                
                try {
			notificationService.sendSimpleMail(emailAdress, subject, body);
		} catch (MailException mailException) {
			System.out.println(mailException);
		}
		return "Email enviado para " + user.getName() + ".";
	}
        
        @RequestMapping("sendPlanningEmail/{vehicleName}/{revisionDate}")
	public String sendPlanningMail(@RequestBody User user, @PathVariable("vehicleName") String vehicleName, @PathVariable("revisionDate") String revisionDate) throws MessagingException {
		String emailAdress = user.getEmail(); //Receiver's email address
		
                String subject = "Planejamento de submissão - " + vehicleName;
                String body = "Caro membro do Spider, \n"
                        + "O Spider alerta que existe um plano de submissão de artigo cadastrado para você no veículo " + vehicleName + "\n"
                        + "A data de revisão interna para encaminhamento ao professor é dia " + formatDateToHumans(revisionDate) + "\n"
                        + "Não esqueça de agilizar a escrita do seu artigo dentro da data estabelecida\n"
                        + "Atenciosamente,\n    Spider Bot";
                        
     
                
                try {
			notificationService.sendSimpleMail(emailAdress, subject, body);
		} catch (MailException mailException) {
			System.out.println(mailException);
		}
		return "Email enviado para " + user.getName() + ".";
	}
}
