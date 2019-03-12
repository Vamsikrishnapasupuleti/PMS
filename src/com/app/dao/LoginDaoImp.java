package com.app.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Employee;
import com.app.entity.Project;



@Repository("loginDao")
public class LoginDaoImp implements LoginDao {

   @Autowired
   private HibernateTemplate hibernateTemplate;

	@Override
	@Transactional(readOnly=false)
	public Employee checkEmployeeCredentials(Employee employee) {
		
		Employee loggedInemployee = (Employee)hibernateTemplate.get(Employee.class,employee.getEid());

		return loggedInemployee;
	}

	@Override
	@Transactional()
	public void addEmployee(Employee employee) {
		hibernateTemplate.save(employee);
		
	}

	@Override
	@Transactional
	public void updateEmployee(Employee employee) {
		
		hibernateTemplate.update(employee);
		
	}

	@Override
	public List<Employee> listEmployees() {
		List<Employee> employeelist=(List<Employee>) hibernateTemplate.find("from Employee");
		return employeelist;
	}

	@Override
	public Employee getEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void deleteEmployee(int id)
	{
		hibernateTemplate.delete((Employee)hibernateTemplate.get(Employee.class, id));
	}
  

	 @Override
	 public Employee getEmployeeByName(String name)
	 {
		 List<Employee> eList=(List<Employee>) hibernateTemplate.find("from Employee where EMP_NAME=?",new Object[] {name});
		 Employee emp=null;
		 Iterator<Employee> itr=eList.iterator();
		 while(itr.hasNext())
		 {
			 
			 emp=(Employee)itr.next();
			 
			 if(emp.getEname().equalsIgnoreCase(name))
			 {
				 return emp;
			 }
		 }
		 return emp;
	 }
}