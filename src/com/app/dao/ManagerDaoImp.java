package com.app.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Employee;
import com.app.entity.Project;
import com.app.entity.Task;



@Repository("managerDao")
public class ManagerDaoImp implements ManagerDao {

   @Autowired
   private HibernateTemplate hibernateTemplate;

	@Override
	@Transactional(readOnly=false)
	public Project getProject(Project project) {
		Project proj=(Project)hibernateTemplate.get(Project.class, project.getProjectId());
		return proj;
	}
	
	@Override
	@Transactional
	public void addProject(Project project) {
		hibernateTemplate.save(project);
		
	}
	
	@Override
	@Transactional
	public void updateProject(Project project) {
		hibernateTemplate.update(project);
		
	}
	
	@Override
	public List<Project> listProjects(int managerId) {
		List<Project> projectList=(List<Project>) hibernateTemplate.find("from Project where MANAGER_ID=?",new Object[] {managerId});
		return projectList;
	}
	
	@Override
	@Transactional
	public void deletePorject(int id) {
		hibernateTemplate.delete((Project)hibernateTemplate.get(Project.class, id));
		
	}

	@Override
	public List<Task> listTasks(int projectId) {
		List<Task> taskList=(List<Task>) hibernateTemplate.find("from Task where PID=?",new Object[] {projectId});
		return taskList;
	}

	@Override
	@Transactional(readOnly=false)
	public Task getTask(Task task) {
		Task searchtask=(Task)hibernateTemplate.get(Task.class, task.getTaskId());
		return searchtask;
	}

	@Override
	@Transactional
	public void addTask(Task task) {
		hibernateTemplate.save(task);
		
	}

	@Override
	@Transactional
	public void updateTask(Task task) {
		hibernateTemplate.update(task);
		
	}
	
	@Override
	public List<Task> listTaskByEmpID(Employee employee)
	{
		List<Task> taskList=(List<Task>) hibernateTemplate.find("from Task where ASSIGNED_EMPLOYEE=?",new Object[] {employee.getEid()});
		return taskList;
	}
  

}