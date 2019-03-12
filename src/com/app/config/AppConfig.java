package com.app.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import org.springframework.web.multipart.commons.CommonsMultipartResolver;




import com.app.entity.Employee;
import com.app.entity.Project;
import com.app.entity.Task;

import static org.hibernate.cfg.Environment.*;


/**
 * @author Vamsi
 */
@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan("com.app.service,com.app.dao")
public class AppConfig {

   @Autowired
   private Environment env;


	@Bean(name = "dataSource")
	public DataSource getDataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName( env.getRequiredProperty("db.driver"));
	    dataSource.setUrl( env.getRequiredProperty("db.url"));
	    dataSource.setUsername( env.getRequiredProperty("db.username"));
	    dataSource.setPassword( env.getRequiredProperty("db.password"));
	 
	    return dataSource;
	}
	
	@Autowired
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean getSessionFactory() {
	 
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	 
	    sessionFactory.setAnnotatedClasses(Employee.class,Project.class,Task.class);
	    //sessionFactory.setAnnotatedClasses(Files.class);
	    sessionFactory.setDataSource(getDataSource());
	    //sessionFactory.setAnnotatedClasses(annotatedClasses);
       sessionFactory.setPackagesToScan(new String[] { "com.app" });
	    
	    sessionFactory.setHibernateProperties(getHibernateProperties());

	    return sessionFactory ;
	}

	
	private Properties getHibernateProperties() {
	    Properties properties = new Properties();
	    properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
	    properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
	    properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
	    return properties;
	}
	
	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(
	        SessionFactory sessionFactory) {
	    HibernateTransactionManager transactionManager = new HibernateTransactionManager(
	            sessionFactory);
	 transactionManager.setSessionFactory(sessionFactory);
	    return transactionManager;
	}
	
	
	@Bean
	@Autowired
	public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory)
	{
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		hibernateTemplate.setCheckWriteOperations(false);
		return hibernateTemplate;
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver()
	{
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(20848820);
	    return multipartResolver;
	}


}