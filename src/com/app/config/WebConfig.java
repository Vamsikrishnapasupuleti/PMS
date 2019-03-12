package com.app.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author Vamsi
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.app"})
public class WebConfig extends WebMvcConfigurerAdapter {

   @Bean
   public InternalResourceViewResolver resolver() {
      InternalResourceViewResolver resolver = new InternalResourceViewResolver();
      resolver.setViewClass(JstlView.class);
      resolver.setPrefix("/WEB-INF/views/");
      resolver.setSuffix(".jsp");
      return resolver;
   }

   @Bean
   public MessageSource messageSource() {
      ResourceBundleMessageSource source = new ResourceBundleMessageSource();
      source.setBasename("messages");
      return source;
   }

   @Override
   public Validator getValidator() {
      LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
      validator.setValidationMessageSource(messageSource());
      return validator;
   }
   
   @Override
   public void addResourceHandlers(final ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
       registry.addResourceHandler("/jquery-1.8.0/**").addResourceLocations("/WEB-INF/jquery-1.8.0/");
       registry.addResourceHandler("/font-awesome/**").addResourceLocations("/WEB-INF/font-awesome/");
       registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/");
   }

}