package com.spider.auth.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.spider.auth.models.Email;
import com.spider.auth.repository.EmailRepository;

@Configuration
@PropertySource("classpath:application.properties")
public class MailConfig {
	
//	@Autowired
//	private Environment env;
	
	@Autowired
	private EmailRepository emailRepository;
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		List<Email> emailList = new ArrayList<Email>();
		emailRepository.findAll().forEach(emailList::add);
		
		Optional<Email> email = emailList.stream().findFirst();
		Email emailData = email.get();
		
		mailSender.setHost(emailData.getHost());
		//mailSender.setPort(Integer.parseInt(emailData.getPort()));
		mailSender.setUsername(emailData.getEmail());
		mailSender.setPassword(emailData.getPassword());
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
                props.put("mail.smtp.port", emailData.getPort());
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.connectiontimeout", 10000);
             
		
		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
}
