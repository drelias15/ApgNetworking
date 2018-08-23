package ApgNetworking.services;

import ApgNetworking.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService (JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void SendNotification(User user) throws MailException{
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom("no-reply@APG.com");
        mail.setSubject("Thank you for registering with us!");
        mail.setText("You have registered with us and your account has been successfully created. Enjoy your time with us.");

        javaMailSender.send(mail);
    }
}
