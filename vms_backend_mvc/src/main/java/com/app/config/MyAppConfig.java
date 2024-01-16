package com.app.config;





import java.util.Properties;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;


@Configuration
@EnableWebMvc
@ComponentScan("com.app")
public class MyAppConfig extends WebMvcConfigurerAdapter{
	
	@Bean
	public InternalResourceViewResolver viewResolver(){
		  
		  InternalResourceViewResolver viewResolver =  new InternalResourceViewResolver();
		  
		  viewResolver.setPrefix("/WEB-INF/views/");
		  
	      viewResolver.setSuffix(".jsp");
	      
	      viewResolver.setViewClass(JstlView.class);
		
          return viewResolver;
	}
	
	
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public Validator getValidator() {
        return validator();
    }
	
    @PostConstruct
	public void init() {
		// Setting the default time zone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("vms.nyggs@gmail.com");
        mailSender.setPassword("wyjc tisw apkk ozcl");

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
	
	    @Bean
	    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
	        return new Jackson2ObjectMapperBuilder();
	    }

	

}
