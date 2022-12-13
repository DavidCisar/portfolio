package io.dcisar.backend.mail;

import org.springframework.stereotype.Service;

import javax.mail.Session;
import java.util.Properties;

import static io.dcisar.backend.config.EmailConstant.*;

@Service
public class EmailService {

    private Session getEmailSession() {
        Properties properties = new Properties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties);
    }

}