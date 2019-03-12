package com.app.dao;

import java.util.List;

import com.app.entity.Employee;



public interface LoginDao {
   public Employee checkEmployeeCredentials(Employee employee);
   public void addEmployee(Employee employee);
   public void updateEmployee(Employee employee);
   public List<Employee> listEmployees();
   public Employee getEmployee(Employee employee);
   public void deleteEmployee(int id);
   public Employee getEmployeeByName(String name);
}