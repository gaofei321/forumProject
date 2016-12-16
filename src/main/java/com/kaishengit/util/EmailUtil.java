package com.kaishengit.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

private static Logger logger= LoggerFactory.getLogger(EmailUtil.class);

    public static void sendHtmlEmail(String toAdress,String subject,String context){

        HtmlEmail htmlEmail=new HtmlEmail();
        htmlEmail.setHostName(Config.get("email.smpt"));
        htmlEmail.setSmtpPort(Integer.valueOf(Config.get("email.port")));
        htmlEmail.setAuthentication(Config.get("email.username"),Config.get("email.password"));
        htmlEmail.setCharset("UTF-8");
        htmlEmail.setStartTLSEnabled(true);

        System.out.println(Config.get("email.username"));

        try{
            htmlEmail.setFrom(Config.get("email.frommail"));

            htmlEmail.setSubject(subject);

            htmlEmail.setHtmlMsg(context);
            htmlEmail.addTo(toAdress);
            htmlEmail.send();

        }catch(EmailException e){
        logger.error("向{}发送邮件失败"+toAdress);
        throw new RuntimeException("发送邮件失败:"+toAdress);


        }


    }

}
