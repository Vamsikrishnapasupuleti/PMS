package com.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "TASK_PMS")
public class Task {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "TID")
   private int taskId;

   @Column(name = "TASK_NAME")
   @NotNull
   private String taskName;

   @ManyToOne
   @JoinColumn(name = "PID")
   private Project project;
   
   @Column(name = "STATUS")
   private String taskStatus;

   @Column(name="ASSIGNED_EMPLOYEE")
   private int assignedToEmployee;

   @Column(name="PRIORITY")
   private String taskPriority;
/**
 * @return the taskId
 */
public int getTaskId() {
	return taskId;
}

/**
 * @param taskId the taskId to set
 */
public void setTaskId(int taskId) {
	this.taskId = taskId;
}

/**
 * @return the taskName
 */
public String getTaskName() {
	return taskName;
}

/**
 * @param taskName the taskName to set
 */
public void setTaskName(String taskName) {
	this.taskName = taskName;
}

/**
 * @return the project
 */
public Project getProject() {
	return project;
}

/**
 * @param project the project to set
 */
public void setProject(Project project) {
	this.project = project;
}



/**
 * @return the taskStatus
 */
public String getTaskStatus() {
	return taskStatus;
}

/**
 * @param taskStatus the taskStatus to set
 */
public void setTaskStatus(String taskStatus) {
	this.taskStatus = taskStatus;
}

/**
 * @return the assignedToEmployee
 */
public int getAssignedToEmployee() {
	return assignedToEmployee;
}

/**
 * @param assignedToEmployee the assignedToEmployee to set
 */
public void setAssignedToEmployee(int assignedToEmployee) {
	this.assignedToEmployee = assignedToEmployee;
}

/**
 * @return the taskPriority
 */
public String getTaskPriority() {
	return taskPriority;
}

/**
 * @param taskPriority the taskPriority to set
 */
public void setTaskPriority(String taskPriority) {
	this.taskPriority = taskPriority;
}


   
   
}