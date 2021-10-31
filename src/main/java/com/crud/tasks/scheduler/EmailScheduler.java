package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailScheduler {
    private final SimpleEmailService simpleEmailService;
    private final TaskRepository taskRepository;
    private final AdminConfig adminConfig;
    private static final String SUBJECT = "Tasks: Once a day email";

    @Scheduled(cron = "0 0 8 * * *")
    public void sendInformationEmail() {
        long size = taskRepository.count();
        String txt;
        if (size > 1) {
            txt = " tasks";
        } else {
            txt = " task";
        }
        simpleEmailService.send(
                new Mail(adminConfig.getAdminMail(), SUBJECT, "Currently in database you got: " + size + txt, null));
    }
}