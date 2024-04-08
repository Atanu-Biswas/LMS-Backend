package com.project.library.management.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Testmail {

    private final MailService mailService;




    public void sendEmail(String to, String from, String subject, String body) {
        mailService.sendEmail(to, from, subject, body);
    }
}
