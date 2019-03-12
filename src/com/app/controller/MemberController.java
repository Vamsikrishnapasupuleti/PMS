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

public class MemberController {

   @Autowired
   private ManagerService managerService;
   
   @Autowired
   private LoginService loginService;

   
   
   
   public  void displayMemberDashboard(Model model,Employee employee) 
   {
	   //MemberController memberController=new MemberController();
	   List<Task> tasks=managerService.listTaskByEmpID(employee);
	   model.addAttribute("tasks", tasks);
	   List<Project> projectList=new ArrayList<Project>();
	   Iterator itr=tasks.iterator();
	   while(itr.hasNext())
	   {
		   Task task=(Task)itr.next();
		   Project project=task.getProject();
		   //model.addAttribute("task", task);
		   
		  int projectStatus=getStatus(project.getProjectId());
		  project.setStatus(projectStatus);
		  
		   model.addAttribute("searchProject", project);
		   model.addAttribute("projectStatus", projectStatus);
		   
	   }
	   model.addAttribute("tasksDisplayDiv", "tasksDisplayDiv");
	   model.addAttribute("employee", new Employee());
	   model.addAttribute("task", new Task());
   }
   
   
   
   @RequestMapping("/employeeTaskedit")
   public String editTaskschangesStatus(@RequestParam("tid") int tid,@RequestParam("pid") int pid,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
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
	       
	          model.addAttribute("searchProject", project);
	          model.addAttribute("employee", new Employee());
			 // model.addAttribute("display", "taskDiv");
	          model.addAttribute("changeTaskStatusDiv", "changeTaskStatusDiv");
	          model.addAttribute("task", task1);
			
		   return "memberDashboard";
		   
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
       
        
   }
   
   
   @RequestMapping("/changeTaskStatus")
   public String changeTaskStatus(@ModelAttribute("task") Task task,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   
		   Task t=managerService.getTask(task);
		   t.setTaskStatus(task.getTaskStatus());
		   managerService.updateTask(t);
		   displayMemberDashboard(model, emp2);
			
		   return "memberDashboard";
		   
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
       
        
   }
   
   
   @RequestMapping("/taskforEmployee")
   public String tasksforEmployee(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   displayMemberDashboard(model, emp2);
			
		   return "memberDashboard";
		   
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
       
        
   }
   
   @RequestMapping("/peopleInProject")
   public String peopleInProject(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		  List<Task> tasks=(List<Task>)managerService.listTaskByEmpID(emp2);
		   
		  Iterator itr =tasks.iterator();
		  Project p=null;
		  while(itr.hasNext())
		  {
			  
			  Task task=(Task)itr.next();
			  
			   p=task.getProject();
			  
		  }
		  List<Employee> employees=(List<Employee>)loginService.listEmployees();
		  List<Task> allTasks=managerService.listTasks(p.getProjectId());
		  List<Employee> employees1=new ArrayList<Employee>();
		  
		  Iterator taskitr=allTasks.iterator();
		  

		  while(taskitr.hasNext())
		  {
			Task t1=(Task) taskitr.next();  
			  for (Employee employee : employees)
			  {
				  if(employee.getEid()==t1.getAssignedToEmployee())
				  {
					  if(!employees1.contains(employee))
					  {
						  employees1.add(employee);
						  break;
					  }
					 
				  }
				
			   }
		  }
			
		  model.addAttribute("projectEmployees", employees1);
		  //model.addAttribute("changePasswordDiv", "changePwdDiv");
		   //model.addAttribute("client", employee);
		   model.addAttribute("project", new Project());
		   model.addAttribute("task", new Task());
		   //model.addAttribute("display", "projectDiv");
		   model.addAttribute("employee", new Employee());
		   //model.addAttribute("displayClientDiv", "displayClientDiv");
		  // return "memberDashboard";
		  // return "memberDashboard";
		   //displayMemberDashboard(model, emp2);
			
		   return "memberDashboard";
		   
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
   
}