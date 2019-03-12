package com.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "PROJECT_PMS")
public class Project {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "PID")
   private int projectId;

   @Column(name = "PROJ_NAME",unique=true)
   @NotNull
   private String projectName;

   @Column(name = "CLIENT")
   private String clientName;

  
   @Column(name = "START_DATE" )
   private String startDate;

   @Column(name = "END_DATE")
   private String endDate;
   
   @Column(name = "STATUS")
   private int status;
   
   
   @Column(name = "MANAGER_ID")
   @NotNull
   private int managerId;


/**
 * @return the projectId
 */
public int getProjectId() {
	return projectId;
}


/**
 * @param projectId the projectId to set
 */
public void setProjectId(int projectId) {
	this.projectId = projectId;
}


/**
 * @return the projectNname
 */
public String getProjectName() {
	return projectName;
}


/**
 * @param projectNname the projectNname to set
 */
public void setProjectName(String projectName) {
	this.projectName = projectName;
}


/**
 * @return the clientName
 */
public String getClientName() {
	return clientName;
}


/**
 * @param clientName the clientName to set
 */
public void setClientName(String clientName) {
	this.clientName = clientName;
}


/**
 * @return the startDdate
 */
public String getStartDate() {
	return startDate;
}


/**
 * @param startDdate the startDdate to set
 */
public void setStartDate(String startDate) {
	this.startDate = startDate;
}


/**
 * @return the endDate
 */
public String getEndDate() {
	return endDate;
}


/**
 * @param endDate the endDate to set
 */
public void setEndDate(String endDate) {
	this.endDate = endDate;
}


/**
 * @return the status
 */
public int getStatus() {
	return status;
}


/**
 * @param status the status to set
 */
public void setStatus(int status) {
	this.status = status;
}


/**
 * @return the managerId
 */
public int getManagerId() {
	return managerId;
}


/**
 * @param managerId the managerId to set
 */
public void setManagerId(int managerId) {
	this.managerId = managerId;
}


   
}