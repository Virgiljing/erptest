package cn.itcast.erp.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Component("mailUtil")
public class MailUtil {
	@Autowired
	private JavaMailSender mailSender;
	@Value("${mail.username}")
	private String from;
	
	public void sendMail(String to,String title,String content) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(title);
		helper.setText(content,true);
		mailSender.send(mimeMessage);
	}
}
