package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.LoginDao;
import com.app.entity.Employee;



@Service("loginService")
public class LoginServiceImp implements LoginService {

   @Autowired
   private LoginDao loginDao;

	@Override
	public Employee checkEmployeeCredentials(Employee employee) {
		
		return loginDao.checkEmployeeCredentials(employee);
	}

	@Override
	public void addEmployee(Employee employee) {
		
		 loginDao.addEmployee(employee);
	}

	@Override
	public void updateEmployee(Employee employee) {
		
		loginDao.updateEmployee(employee);
		
	}

	@Override
	public List<Employee> listEmployees() {
		
		return loginDao.listEmployees();
	}

	
	public void deleteEmployee(int id)
	{
		loginDao.deleteEmployee(id);
	}

	@Override
	public Employee getEmployeeByName(String name) {
		
		return loginDao.getEmployeeByName(name);
	}
  
   
   

}