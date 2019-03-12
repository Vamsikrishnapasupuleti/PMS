package com.app.service;


import java.util.List;

import com.app.entity.Employee;
import com.app.entity.Project;
import com.app.entity.Task;



public interface ManagerService {
  
	public Project getProject(Project project);
	public void addProject(Project project);
	public void updateProject(Project project);
	public List<Project> listProjects(int managerId);
	public void deletePorject(int id);
	
	public List<Task> listTasks(int projectId);
	public Task getTask(Task task);
	public void addTask(Task task);
	public void updateTask(Task task);
	public List<Task> listTaskByEmpID(Employee employee);
}
