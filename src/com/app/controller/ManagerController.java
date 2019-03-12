package com.app.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.common.PMSCommonConstants;
import com.app.entity.Employee;
import com.app.entity.Project;
import com.app.entity.Task;
import com.app.service.LoginService;
import com.app.service.ManagerService;


/**
 * @author Vamsi
 */
@Controller

public class ManagerController {

   @Autowired
   private ManagerService managerService;
   
   @Autowired
   private LoginService loginService;

   
   
   
   @RequestMapping("/projects")
   public String listProjects(Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) {

		/*
		 * if (result.hasErrors()) {
		 * 
		 * List<FieldError> errors = result.getFieldErrors(); for (FieldError error :
		 * errors ) { System.out.println (error.getObjectName() + " - " +
		 * error.getDefaultMessage()); } return "employeeLogin"; }
		 */
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
		   if(emp.getEid()!=0)
		   {
			   List<Project> projectList=managerService.listProjects(emp.getEid());
			   
			   Iterator itr=projectList.iterator();
			   
			   while(itr.hasNext())
			   {
				   Project p1=(Project)itr.next();
				   
				   int statusBar=getStatus(p1.getProjectId());
				   
				   p1.setStatus(statusBar);
				   
				   managerService.updateProject(p1);
				   
			   }
			   
			   List<Project> projectList1=managerService.listProjects(emp.getEid());
			   model.addAttribute("projects", projectList1);
			   model.addAttribute("employee", new Employee());
			   model.addAttribute("project", new Project());
			   model.addAttribute("task", new Task());
			   model.addAttribute("display", "projectDiv");
			   return "managerDashboard";
		   }
		  
		   model.addAttribute("employee", new Employee());
		   model.addAttribute("project", new Project());
		   model.addAttribute("task", new Task());
		   model.addAttribute("display", "projectDiv");		
		  return "managerDashboard";
	   }
	   
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
	   
		
	   
   }
   
   
   @RequestMapping("/searchProject")
   public String searchByProjectId(@RequestParam("searchprojectId") int pid,Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2.getEid()!=0)
	   {
		   if(pid!=0)
		   {
			   Employee emp=(Employee) session.getAttribute("loggedInUser");
			   Project project =new Project();
			   project.setProjectId(pid);
			   Project searchProject=(Project)managerService.getProject(project);
			   
			   if(searchProject.getManagerId()==emp.getEid())
			   {
				   
					int statusbar=getStatus(pid);	   
					searchProject.setStatus(statusbar);
					managerService.updateProject(searchProject);
					
					searchProject=managerService.getProject(searchProject);
				   
				   model.addAttribute("searchProject", searchProject);
			   }
			   model.addAttribute("employee", new Employee());
			   model.addAttribute("project", new Project());
			   model.addAttribute("task", new Task());
			   model.addAttribute("display", "projectDiv");
			   return "managerDashboard";
		   }
		   model.addAttribute("employee", new Employee());
		   model.addAttribute("task", new Task());
		   model.addAttribute("project", new Project());
		   model.addAttribute("display", "projectDiv");
		   return "managerDashboard";
		   
	   }

	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
	   
	      
   }
   
   
   
   @RequestMapping("/addProject")
   public String addProject(@ModelAttribute("project") @Valid Project project,Model model,BindingResult result, HttpServletRequest request,HttpServletResponse response,HttpSession session)
   {
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   if(project.getProjectId()==0)
		   {
				//new employee, add it
			   
			    project.setManagerId(emp.getEid());
				managerService.addProject(project);
				model.addAttribute("employee", new Employee());
				model.addAttribute("project", new Project());
				model.addAttribute("task", new Task());
				model.addAttribute("display", "projectDiv");
				return "managerDashboard";
			}
			else if(project.getProjectId()!=0)
			{
				//existing person, call update
				project.setManagerId(emp.getEid());
				 managerService.updateProject(project);
				 Project searchProject=managerService.getProject(project);
				 //project.setManagerId(emp.getEid());
				 List<Task> tasks=managerService.listTasks(searchProject.getProjectId());
				 model.addAttribute("tasks", tasks);
				 model.addAttribute("project", searchProject);
				 model.addAttribute("task", new Task());
				 model.addAttribute("display", "taskDiv");
				 model.addAttribute("employee", new Employee());
			     return "managerDashboard";
			}
			else
			{
				model.addAttribute("employee", new Employee());
				 model.addAttribute("task", new Task());
				    model.addAttribute("display", "projectDiv");
				    model.addAttribute("project", new Project());
					return "adminDashboard";
			}
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
	   
   }
   
   @RequestMapping("/project")
   public String displayProjectDashboard(@RequestParam("pid") int pid,Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session)
   {
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   if(pid!=0)
		   {
			   Project project =new Project();
			   project.setProjectId(pid);;
			   Project searchProject=managerService.getProject(project);
			   List<Task> tasks=managerService.listTasks(pid);
			   model.addAttribute("tasks", tasks);
			   model.addAttribute("task", new Task());
			   model.addAttribute("display", "taskDiv");
			   model.addAttribute("project", searchProject);
			   model.addAttribute("employee", new Employee());
			   return "managerDashboard";
		   }
		   model.addAttribute("employee", new Employee());
		   model.addAttribute("task", new Task());
		   model.addAttribute("display", "taskDiv");
		   model.addAttribute("project", new Project());
		   return "managerDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
	   
   }
   
   @RequestMapping("/editProject")
   public String editProject(@RequestParam("pid") int pid,Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session)
   {
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   if(pid!=0)
		   {
			   Project project =new Project();
			   project.setProjectId(pid);
			   Project searchProject=managerService.getProject(project);
			   //managerService.updateProject(searchProject);
			   model.addAttribute("project", searchProject);
			   model.addAttribute("task", new Task());
			   model.addAttribute("employee", new Employee());
			   model.addAttribute("display", "taskDiv");
			   model.addAttribute("editDiv", "editProjectDiv");
			   return "managerDashboard";
		   }
		   model.addAttribute("employee", new Employee());
		   model.addAttribute("project", new Project());
		   model.addAttribute("task", new Task());
		   model.addAttribute("display", "taskDiv");
		   return "managerDashboard";
		
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
	    
   }
   
   @RequestMapping("/tasks")
   public String listTasks(@RequestParam("pid") int pid,Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session)
   {
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   if(pid!=0)
		   {
			   Project project =new Project();
			   project.setProjectId(pid);;
			   Project searchProject=managerService.getProject(project);
			   List<Task> tasks=managerService.listTasks(pid);
			   model.addAttribute("tasks", tasks);
			   model.addAttribute("employee", new Employee());
			   model.addAttribute("task", new Task());
			   model.addAttribute("display", "taskDiv");
			   model.addAttribute("project", searchProject);
			   return "managerDashboard";
		   } 
		   model.addAttribute("employee", new Employee());
		   model.addAttribute("task", new Task());
		   model.addAttribute("display", "taskDiv");
		   model.addAttribute("project", new Project());
		   return "managerDashboard";
	   }
	   
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
	   
	   
	   
   }
   
   
   @RequestMapping("/addTask")
   public String addTask(@ModelAttribute("task") @Valid Task task,@RequestParam("pid") int pid,Model model,BindingResult result, HttpServletRequest request,HttpServletResponse response,HttpSession session)
   {
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   if(task.getTaskId()==0)
		   {
				//new Task, add it
			    Project project=new Project();
			    project.setManagerId(emp.getEid());
			    project.setProjectId(pid);
			    Project project1=managerService.getProject(project);
			    task.setProject(project1);
			    task.setTaskStatus(PMSCommonConstants.TODO);
				managerService.addTask(task);
				List<Task> tasks=managerService.listTasks(project1.getProjectId());
				model.addAttribute("employee", new Employee());
				model.addAttribute("tasks", tasks);
				model.addAttribute("project", project1);
				model.addAttribute("task", new Task());
				model.addAttribute("display", "taskDiv");
				return "managerDashboard";
			}
			else if(task.getTaskId()!=0)
			{
				//existing person, call update
				Project project=new Project();
			    project.setManagerId(emp.getEid());
			    project.setProjectId(pid);
			    Project project1=managerService.getProject(project);
			    task.setProject(project1);
				 managerService.updateTask(task);
				 if(task.getTaskStatus().equalsIgnoreCase(PMSCommonConstants.COMPLETED))
				 {
					 Employee e=new Employee();
					 e.setEid(task.getAssignedToEmployee());
					 Employee employee=(Employee)loginService.checkEmployeeCredentials(e);
					 employee.setAssignmentStatus(PMSCommonConstants.UNASSIGNED);
					 loginService.updateEmployee(employee);
				 }
				 
				 
				 //project.setManagerId(emp.getEid());
				 List<Task> tasks=managerService.listTasks(project1.getProjectId());
				 model.addAttribute("employee", new Employee());
				 model.addAttribute("tasks", tasks);
				 model.addAttribute("project", project1);
				 model.addAttribute("task", new Task());
				 model.addAttribute("display", "taskDiv");
			     return "managerDashboard";
			}
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
	   
   }
  
 
	
	  @RequestMapping("/assignEmployeetoTask")
	  public String assignEmployeetoTask(@RequestParam("tid") int tid,@RequestParam("eid") int eid,Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session)
	  {
		  Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
		   if(emp2!=null)
		   {
			   Task task=new Task();
				  task.setTaskId(tid);
				  
				  Task task1=(Task)managerService.getTask(task);
				  task1.setAssignedToEmployee(eid);
				  task1.setTaskStatus(PMSCommonConstants.INPROGRESS);
				  managerService.updateTask(task1);
				  Employee e=new Employee();
				  e.setEid(eid);
				  Employee emp=(Employee)loginService.checkEmployeeCredentials(e);
				  emp.setAssignmentStatus(PMSCommonConstants.ASSIGNED);
				  loginService.updateEmployee(emp);
				  Project project=managerService.getProject(task1.getProject());
				 // project.setProjectId(task1.getProject());
				  //model.addAttribute("employee", new Employee());
				  List<Task> tasks=managerService.listTasks(project.getProjectId());
					 model.addAttribute("tasks", tasks);
					 model.addAttribute("project", project);
					 model.addAttribute("task", task);
					 model.addAttribute("display", "taskDiv");
					 model.addAttribute("employee", new Employee());
				  return "managerDashboard";
		   }
		   model.addAttribute("employee", new Employee());
		   return "employeeLogin";
	  }
	 
   
   @RequestMapping("/listUnassignedEmployees")
   public String listManagers(@RequestParam("tid") int tid,@RequestParam("pid") int pid,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   List<Employee> listAll=(List<Employee>)loginService.listEmployees();
	        List<Employee> unassignedEmployeeList=new ArrayList<Employee>();
	        Task task=new Task();
	        task.setTaskId(tid);
	        Project p=new Project();
	        p.setProjectId(pid);
	        Project project=managerService.getProject(p);
	        Iterator itr=listAll.iterator();
	        while(itr.hasNext())
	        {
	        	Employee emp1=(Employee)itr.next();
	        	if(!emp1.getRole().equalsIgnoreCase(PMSCommonConstants.CLIENT) && emp1.getAssignmentStatus().equalsIgnoreCase(PMSCommonConstants.UNASSIGNED) && emp1.getManager_id()==project.getManagerId())
	        	{
	        		unassignedEmployeeList.add(emp1);
	        	}
	        }
	          model.addAttribute("project", project);
			// model.addAttribute("task", new Task());
			 model.addAttribute("display", "taskDiv");
	        model.addAttribute("unassignedEmployeeList", unassignedEmployeeList);
	        model.addAttribute("task", task);
	        model.addAttribute("assignEmployee", "assignEmployee");
			model.addAttribute("employee", new Employee());
		   return "managerDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
        	 
        
   }
   
   @RequestMapping("/inprogressTask")
   public String inProgressTasks(@RequestParam("tid") int tid,@RequestParam("pid") int pid,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   Task task=new Task();
	        task.setTaskId(tid);
	        Project p=new Project();
	        p.setProjectId(pid);
	        task.setProject(p);
	        Task task1=(Task)managerService.getTask(task);
	        Project project=managerService.getProject(p);
	       
	          model.addAttribute("project", project);
	          model.addAttribute("employee", new Employee());
			  model.addAttribute("display", "taskDiv");
	          model.addAttribute("editTaskDiv", "addTaskDiv");
	          model.addAttribute("task", task1);
			
		   return "managerDashboard";
		   
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
       
        
   }
   
   
   @RequestMapping("/people")
   public String peopleunderManager(@RequestParam("pid") int pid,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   List<Employee> employees=(List<Employee>)loginService.listEmployees();
		   List<Employee> managerEmployees=new ArrayList<Employee>();
	       Iterator<Employee> itr=employees.iterator();
	       while(itr.hasNext())
	       {
	    	   Employee emp1=(Employee)itr.next();
	    	   if(emp1.getManager_id()==emp.getEid())
	    	   {
	    		   managerEmployees.add(emp1);
	    	   }
	    	   
	       }
	       Project p=new Project();
	       p.setProjectId(pid);
	       Project project=(Project)managerService.getProject(p);
	       
	       model.addAttribute("managerEmployees", managerEmployees);
	       model.addAttribute("managerEmployeesDiv", "managerEmployees");
	        model.addAttribute("project", project);
			model.addAttribute("display", "taskDiv");
	        //model.addAttribute("editTaskDiv", "addTaskDiv");
			model.addAttribute("employee", new Employee());
	        model.addAttribute("task", new Task());
			return "managerDashboard";
	   }
       
	   model.addAttribute("employee", new Employee());
		
	   return "employeeLogin";
   }
   
   
   public int getStatus(int pid)
   {
	   int status=0;
	   Task task=new Task();
	   List<Task> tasks=(List<Task>)managerService.listTasks(pid);
	   if(tasks.size()>0)
	   {
		   Iterator itr=tasks.iterator();
		   int completedTasks=0;
		   
		   while( itr.hasNext())
		   {
			   Task t=(Task)itr.next();
			  
			   if(t.getTaskStatus().equalsIgnoreCase(PMSCommonConstants.COMPLETED))
			   {
				   completedTasks=completedTasks+1;
			   }
			   
		   }
		   //status=(((int)completedTasks)/tasks.size())*100;
		   double status1=(((double)completedTasks)/tasks.size())*100;
		   status=(int)status1;
	   }
	   else
	   {
		   status=0;
	   }
	   
	   	   
	   return status;
   }
   
   @RequestMapping("/displayClient")
   public String displayClient(@RequestParam("cname") String clientName,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   Employee employee=loginService.getEmployeeByName(clientName);
		   Employee loggedInUser=(Employee)loginService.checkEmployeeCredentials(emp);
		   
		   if(loggedInUser.getRole().equalsIgnoreCase(PMSCommonConstants.MANAGER))
		   {
			   if(employee!=null)
			   {
				   
				   model.addAttribute("client", employee);
				   model.addAttribute("project", new Project());
				   model.addAttribute("task", new Task());
				   model.addAttribute("display", "projectDiv");
				   model.addAttribute("displayClientDiv", "displayClientDiv");
				   
				   model.addAttribute("employee", new Employee());
				   return "managerDashboard";
				   
			   }
			   else
			   {
				   model.addAttribute("displayClientDiv", "displayClientDiv");
				   model.addAttribute("project", new Project());
				   model.addAttribute("task", new Task());
				   model.addAttribute("display", "projectDiv");
				   model.addAttribute("employee", new Employee());
				   return "managerDashboard";
			   }
			
		   }
		   else if(loggedInUser.getRole().equalsIgnoreCase(PMSCommonConstants.MEMBER))
		   {
			   if(employee!=null)
			   {
				   
				   model.addAttribute("client", employee);
				   model.addAttribute("project", new Project());
				   model.addAttribute("task", new Task());
				   model.addAttribute("display", "projectDiv");
				   model.addAttribute("employee", new Employee());
				   model.addAttribute("displayClientDiv", "displayClientDiv");
				   return "memberDashboard";
				   
			   }
			   else
			   {
				   model.addAttribute("displayClientDiv", "displayClientDiv");
				   model.addAttribute("project", new Project());
				   model.addAttribute("task", new Task());
				   model.addAttribute("display", "projectDiv");
				   model.addAttribute("employee", new Employee());
				   return "memberDashboard";
			   }
			
		   }
		   else
		   {
			   model.addAttribute("employee", new Employee());
			   return "employeeLogin";
		   }
		      
		   
	   }
       
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   @RequestMapping("/reports")
   public String projectReport(@RequestParam("pid") int pid,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   Project p=new Project();
		   p.setProjectId(pid);
		   Project project=managerService.getProject(p);
	      int completedPercentage= getStatus(pid, PMSCommonConstants.COMPLETED);
	      int inProgressPercentage= getStatus(pid, PMSCommonConstants.INPROGRESS);
	      int todoPercentage= getStatus(pid, PMSCommonConstants.TODO);
	       
	      model.addAttribute("completedPercentage", completedPercentage);
	      model.addAttribute("inProgressPercentage", inProgressPercentage);
	      model.addAttribute("todoPercentage", todoPercentage);
	       //model.addAttribute("managerEmployees", managerEmployees);
	       //model.addAttribute("managerEmployeesDiv", "managerEmployees");
	        model.addAttribute("project", project);
			model.addAttribute("display", "taskDiv");
	        //model.addAttribute("editTaskDiv", "addTaskDiv");
			model.addAttribute("chartContainer", "doughnut-chart");
			model.addAttribute("employee", new Employee());
	        model.addAttribute("task", new Task());
			return "managerDashboard";
	   }
       
	   model.addAttribute("employee", new Employee());
		
	   return "employeeLogin";
   }
   
   public int getStatus(int pid,String taskStatus)
   {
	   int status=0;
	   Task task=new Task();
	   List<Task> tasks=(List<Task>)managerService.listTasks(pid);
	   if(tasks.size()>0)
	   {
		   Iterator itr=tasks.iterator();
		   int completedTasks=0;
		   
		   while( itr.hasNext())
		   {
			   Task t=(Task)itr.next();
			  
			   if(t.getTaskStatus().equalsIgnoreCase(taskStatus))
			   {
				   completedTasks=completedTasks+1;
			   }
			   
		   }
		   //status=(((int)completedTasks)/tasks.size())*100;
		   double status1=(((double)completedTasks)/tasks.size())*100;
		   status=(int)status1;
	   }
	   else
	   {
		   status=0;
	   }
	   
	   	   
	   return status;
   }
   
}