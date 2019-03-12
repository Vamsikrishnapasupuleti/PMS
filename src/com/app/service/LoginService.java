package com.app.service;


import java.util.List;

import com.app.entity.Employee;



public interface LoginService {
  
	public Employee checkEmployeeCredentials(Employee employee);
	public void addEmployee(Employee employee);
	public void updateEmployee(Employee employee);
	public List<Employee> listEmployees();
	public void deleteEmployee(int id);
	public Employee getEmployeeByName(String name);
	
}
