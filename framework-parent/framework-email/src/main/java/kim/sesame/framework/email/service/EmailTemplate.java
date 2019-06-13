package kim.sesame.framework.email.service;

import freemarker.template.Template;
import kim.sesame.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 发送邮件
 * http://blog.didispace.com/springbootmailsender/
 * 邮箱 :  POP3/SMTP服务 ,记住授权码
 * https://blog.csdn.net/u011244202/article/details/54809696/
 */
@Service
public class EmailTemplate {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private EmailProperties emailProperties;

    /**
     * 发送邮件给多个人,不支持昵称发送
     *
     * @param to      接受人, String []
     * @param subject 主题
     * @param text    内容
     */
    public void sendSimpleMail(String[] to, String subject, String text) {

        // 简单发送, 收件人显示的是邮箱号码
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(emailProperties.getUsername());
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        mailSender.send(message);

        // 把发件人变成昵称发送
        sendSimpleEmail(to, subject, text, false);
    }

    /**
     * 发送简单的html邮件
     *
     * @param to      接受人, String []
     * @param subject 主题
     * @param text    内容
     */
    public void sendSimpleHtml(String[] to, String subject, String text) {
        sendSimpleEmail(to, subject, text, true);
    }

    /**
     * 发送简单邮件的通用方法
     *
     * @param to      发送给谁
     * @param subject 主题
     * @param text    内容
     * @param html    是否是html代码内容
     * @param consumer  发送之前可以添加附件
     */
    public void sendSimpleEmail(String[] to, String subject, String text, boolean html, Consumer<MimeMessageHelper> consumer) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            setNickName(helper);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, html);

            consumer.accept(helper);
//        FileSystemResource file = new FileSystemResource(new File("weixin.jpg"));
//        helper.addInline("weixin", file);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSimpleEmail(String[] to, String subject, String text, boolean html) {
        sendSimpleEmail(to, subject, text, true, helper -> {
        });
    }

    /**
     * 发送模板html 邮件
     *
     * @param to       接受人, String []
     * @param subject  主题
     * @param filePath 模板文件路径
     * @param model    模板里的参数
     */
    public void sendTemplateHtml(String[] to, String subject, String filePath, Map<String, Object> model) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            setNickName(helper);
            helper.setTo(to);
            helper.setSubject(subject);

            if (model == null) {
                model = new HashMap<>();
            }
            //修改 application.properties 文件中的读取路径
//        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//        configurer.setTemplateLoaderPath("classpath:templates");
            //读取 html 模板
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(filePath);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendTemplateHtml(String to, String subject, String filePath, Map<String, Object> model) {
        sendTemplateHtml(new String[]{to}, subject, filePath, model);
    }

    public void sendSimpleHtml(String to, String subject, String text) {
        sendSimpleHtml(new String[]{to}, subject, text);
    }

    public void sendSimpleMail(String to, String subject, String text) {
        sendSimpleMail(new String[]{to}, subject, text);
    }

    /**
     * 设置发件人的昵称,只有在发送的是html模板时才可以设置
     *
     * @param helper
     */
    private void setNickName(MimeMessageHelper helper) {
        try {
            if (StringUtil.isNotEmpty(emailProperties.getSendNickname())) {
                helper.setFrom(new InternetAddress(emailProperties.getUsername(), emailProperties.getSendNickname(), "UTF-8"));
            } else {
                helper.setFrom(emailProperties.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
