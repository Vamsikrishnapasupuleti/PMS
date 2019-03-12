package com.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "EMPLOYEE_PMS")
public class Employee {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "EMP_ID")
   private int eid;

   @Column(name = "EMP_NAME")
   @Size(max = 20, min = 3, message = "{employee.name.invalid}")
   private String ename;

   @Column(name = "EMAIL", unique = true)
   @Email(message = "{employee.email.invalid}")
   private String email;

  
   @Column(name = "ADDRESS")
   private String address;

   @Column(name = "ROLE")
   @Pattern(regexp = "ADMIN|MANAGER|MEMBER|CLIENT", flags = Pattern.Flag.CASE_INSENSITIVE,message="{employee.name.invalid}")
   private String role;
   
   @Column(name = "PHONE")
   @Pattern(regexp = "^\\d{10}$", flags = Pattern.Flag.CASE_INSENSITIVE,message="{employee.name.invalid}")
   private String phoneNumber;
   
   
   @Column(name = "PASSWORD")
   @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",message="{employee.password.invalid}")
   @Size(max = 20, min = 8, message = "{employee.password.invalidsize}")
   private String password;


   @Column(name="STATUS")
   private String assignmentStatus;
   
   @Column(name="MANAGER_ID")
   private int manager_id;
   
   @Column(name="team")
   private String team;
   
   @Column(name="RE_PWD")
   private String reEnterPwd;
	/**
	 * @return the eid
	 */
	public int getEid() {
		return eid;
	}
	
	
	/**
	 * @param eid the eid to set
	 */
	public void setEid(int eid) {
		this.eid = eid;
	}
	
	
	/**
	 * @return the ename
	 */
	public String getEname() {
		return ename;
	}
	
	
	/**
	 * @param ename the ename to set
	 */
	public void setEname(String ename) {
		this.ename = ename;
	}
	
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	
	
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @return the assignmentStatus
	 */
	public String getAssignmentStatus() {
		return assignmentStatus;
	}


	/**
	 * @param assignmentStatus the assignmentStatus to set
	 */
	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}


	/**
	 * @return the manager_id
	 */
	public int getManager_id() {
		return manager_id;
	}


	/**
	 * @param manager_id the manager_id to set
	 */
	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}


	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}


	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}


	/**
	 * @return the reEnterPwd
	 */
	public String getReEnterPwd() {
		return reEnterPwd;
	}


	/**
	 * @param reEnterPwd the reEnterPwd to set
	 */
	public void setReEnterPwd(String reEnterPwd) {
		this.reEnterPwd = reEnterPwd;
	}

}