package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.config.CompanyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private CompanyConfig companyConfig;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildTrelloCardEmail(String message) {
        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8888/crud");
        context.setVariable("button", "Visit website");
        context.setVariable("admin_config", adminConfig);
        context.setVariable("application_functionality", functionality);
        context.setVariable("company_name", companyConfig.getCompanyName());
        context.setVariable("company_goal", companyConfig.getCompanyGoal());
        context.setVariable("company_mail", companyConfig.getCompanyMail());
        context.setVariable("company_phone", " phone number:" + companyConfig.getCompanyPhone());
        context.setVariable("show_button", false);
        context.setVariable("is_friend", true);
        return templateEngine.process("mail/created-trello-card-mail", context);
    }

    public String buildSecondEmail(String message) {

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("show_button", true);
        context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
        context.setVariable("button", "check website for current tasks");
        context.setVariable("admin_config", adminConfig);
        context.setVariable("is_friend", false);
        context.setVariable("goodbye_msg",  "Have nice day");
        return templateEngine.process("mail/second-mail", context);
    }
}
