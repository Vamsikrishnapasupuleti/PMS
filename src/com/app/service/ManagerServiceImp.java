package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.LoginDao;
import com.app.dao.ManagerDao;
import com.app.entity.Employee;
import com.app.entity.Project;
import com.app.entity.Task;



@Service("managerService")
public class ManagerServiceImp implements ManagerService {

   @Autowired
   private ManagerDao managerDao;

	@Override
	public Project getProject(Project project) {
		
		return managerDao.getProject(project);
	}
	
	@Override
	public void addProject(Project project) {
		managerDao.addProject(project);
		
	}
	
	@Override
	public void updateProject(Project project) {
		managerDao.updateProject(project);
		
	}
	
	@Override
	public List<Project> listProjects(int managerId) {
		
		return managerDao.listProjects(managerId);
	}
	
	@Override
	public void deletePorject(int id) {
		managerDao.deletePorject(id);
		
	}

	@Override
	public List<Task> listTasks(int projectId) {
		
		return managerDao.listTasks(projectId);
	}

	@Override
	public Task getTask(Task task) {
		
		return managerDao.getTask(task);
	}

	@Override
	public void addTask(Task task) {
		managerDao.addTask(task);
		
	}

	@Override
	public void updateTask(Task task) {
		managerDao.updateTask(task);
		
	}
	
	public List<Task> listTaskByEmpID(Employee employee)
	{
		
		return managerDao.listTaskByEmpID(employee);
	}
   
   

}