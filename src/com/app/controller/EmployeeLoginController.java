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
import org.springframework.http.HttpRequest;
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

public class EmployeeLoginController {

   @Autowired
   private LoginService loginService;

   @Autowired
   private ManagerService managerService;
   
   @RequestMapping("/")
   public String home(Locale locale, Model model) {

      model.addAttribute("employee", new Employee());
      
      return "employeeLogin";
   }
   
   @RequestMapping("/login")
   public String login(@ModelAttribute("employee") @Valid Employee employee, BindingResult result, Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session) {

     
	   if (result.hasErrors()) {
		   
		   List<FieldError> errors = result.getFieldErrors();
		    for (FieldError error : errors ) {
		        System.out.println (error.getObjectName() + " - " + error.getDefaultMessage());
		    }
		    model.addAttribute("errorMessage", "Invalid Credentials!!!");
		   return "employeeLogin";
	      }

	
		  Employee loggedInEmployee=loginService.checkEmployeeCredentials(employee);
		   boolean employeeExists=false;
			if(loggedInEmployee!=null)
			{
				if(employee.getPassword().equals(loggedInEmployee.getPassword()))
				{
					employeeExists=true;
				}
			}
	        
			session=request.getSession(true);
			session.setAttribute("loggedInUser", employee);
			
			
			if(employeeExists)
			{
				if(loggedInEmployee.getRole().equalsIgnoreCase(PMSCommonConstants.ADMIN))
				{
					return "adminDashboard";
				}
				else if(loggedInEmployee.getRole().equalsIgnoreCase(PMSCommonConstants.MANAGER))
				{
					model.addAttribute("task", new Task());
					model.addAttribute("project", new Project());
					return "managerDashboard";
				}
				else if(loggedInEmployee.getRole().equalsIgnoreCase(PMSCommonConstants.MEMBER))
				{
					//MemberController memberController=new MemberController();
					displayMemberDashboard(model,loggedInEmployee);
					return "memberDashboard";
				}
				else
				{
					model.addAttribute("employee", new Employee());
					return "employeeLogin";
				}
			}
			else
			{
				model.addAttribute("errorMessage", "Invalid Credentials!!!");
				model.addAttribute("employee", new Employee());
			      return "employeeLogin";
			}
			
   }
   
  
   @RequestMapping("/add")
   public String addUser(@ModelAttribute("employee") @Valid Employee employee,Model model,BindingResult result,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   if (result.hasErrors())
			{
					   List<FieldError> errors = result.getFieldErrors();
					    for (FieldError error : errors ) {
					        System.out.println (error.getObjectName() + " - " + error.getDefaultMessage());
					    }
					   return "adminDashboard";
	        }
			
			
			if(employee.getEid()==0){
				//new employee, add it
				employee.setPassword("Project@123");
				employee.setAssignmentStatus(PMSCommonConstants.UNASSIGNED);
				loginService.addEmployee(employee);
				model.addAttribute("employee", new Employee());
				   return "adminDashboard";
			}
			else if(employee.getEid()!=0 && employee.getEname()!=null && employee.getPhoneNumber()!=null && employee.getRole()!=null)
			{
				//existing person, call update
				
				loginService.updateEmployee(employee);
				model.addAttribute("employee", new Employee());
				   return "adminDashboard";
			}
			model.addAttribute("employee", new Employee());
			return "adminDashboard";
	   }
	   
	   model.addAttribute("employee", new Employee());
		return "employeeLogin";
	   
		   
   }
   
   
   @RequestMapping("/search")
   public String searchEmployee(@RequestParam("search") String id,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   Employee employee=new Employee();
		   employee.setEid(Integer.parseInt(id));
		   Employee searchEmployee=loginService.checkEmployeeCredentials(employee);
		   model.addAttribute("searchEmployee", searchEmployee);
		   model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	  
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   
   @RequestMapping("/listAll")
   public String listEmployees(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   List<Employee> employeesList=loginService.listEmployees();
		   List<Employee> employeesList1=new ArrayList<Employee>();
		   Iterator itr=employeesList.iterator();
		   while(itr.hasNext())
		   {
			   Employee e=(Employee) itr.next();
			   if(!e.getRole().equalsIgnoreCase(PMSCommonConstants.CLIENT))
			   {
				   employeesList1.add(e);
			   }
			   
		   }
		   
		   
		   model.addAttribute("employeesList", employeesList1);
		   model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
		
	  
   }
   
   @RequestMapping("/signout")
   public String signoutEmployee(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
		session=request.getSession(false);
		
		session.invalidate();
		
		model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   @RequestMapping("/edit")
   public String editEmployee(@RequestParam("eid") int id,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		    session=request.getSession(false);
			Employee employee=new Employee();
			employee.setEid(id);
			Employee updateEmployee=loginService.checkEmployeeCredentials(employee);
			model.addAttribute("employee", updateEmployee);
		    return "adminDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   @RequestMapping("/delete")
   public String deleteEmployee(@RequestParam("eid") int id,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   loginService.deleteEmployee(id);
			
			model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	   
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }

   
	
	  @RequestMapping("/assignManager") 
	  public String assignManager(@RequestParam("eid") int eid,@RequestParam("mid") int mid,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
	  {
	  
		  Employee emp1=(Employee)(session.getAttribute("loggedInUser"));
		   if(emp1!=null)
		   {
			   Employee employee=new Employee();
				  employee.setEid(eid); 
				  Employee emp=(Employee)loginService.checkEmployeeCredentials(employee);
				  emp.setManager_id(mid); 
				  loginService.updateEmployee(emp);
				  model.addAttribute("employee", new Employee());
				  
				  return "adminDashboard";
			
		   }
		   model.addAttribute("employee", new Employee());
		   return "employeeLogin";
		   
       }
	 
   
   @RequestMapping("/listManager")
   public String listManagers(@RequestParam("eid") int eid,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   List<Employee> listAll=(List<Employee>)loginService.listEmployees();
	        List<Employee> managerList=new ArrayList<Employee>();
	        Employee employee=new Employee();
			employee.setEid(eid); 
		    Employee emp=(Employee)loginService.checkEmployeeCredentials(employee);
	        Iterator itr=listAll.iterator();
	        while(itr.hasNext())
	        {
	        	Employee emp1=(Employee)itr.next();
	        	if(emp1.getRole().equalsIgnoreCase(PMSCommonConstants.MANAGER))
	        	{
	        		managerList.add(emp1);
	        	}
	        }
	        model.addAttribute("managersList", managerList);
			model.addAttribute("emp", emp);
			model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   @RequestMapping("/addClientButton")
   public String addClientButton(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   
		   
		   model.addAttribute("addClientDiv", "addClientDiv");
		   model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   
   @RequestMapping("/addClient")
   public String addClient(@ModelAttribute("employee") Employee employee,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   employee.setPassword(PMSCommonConstants.DEFAULT_PASSWORD);
		   employee.setRole(PMSCommonConstants.CLIENT);
		   loginService.addEmployee(employee);
		   //model.addAttribute("addClientDiv", "addClientDiv");
		   model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   @RequestMapping("/listClients")
   public String listClient(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   List<Employee> clients=(List<Employee>)loginService.listEmployees();
		   List<Employee> clientsList=new ArrayList<Employee>();
		   Iterator itr=clients.iterator();
		   
		   while(itr.hasNext())
		   {
			   Employee emp=(Employee) itr.next();
			   
			   if(emp.getRole().equalsIgnoreCase(PMSCommonConstants.CLIENT))
			   {
				   clientsList.add(emp);
			   }
			   
		   }
		   
		   model.addAttribute("clientsList", clientsList);
		   model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   
   
   @RequestMapping("/changePassword")
   public String changePassword(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   Employee emp=(Employee) loginService.checkEmployeeCredentials(emp2);
		   
		   if(emp.getRole().equalsIgnoreCase(PMSCommonConstants.MANAGER))
		   {
			   model.addAttribute("changePasswordDiv", "changePwdDiv");
			   model.addAttribute("display", "projectDiv");
			   model.addAttribute("project", new Project());
			   model.addAttribute("task", new Task());
			   model.addAttribute("employee", new Employee());
			   return "managerDashboard";
		   }
		   else if(emp.getRole().equalsIgnoreCase(PMSCommonConstants.MEMBER))
		   {
			   model.addAttribute("changePasswordDiv", "changePwdDiv");
			   //model.addAttribute("client", employee);
			   model.addAttribute("project", new Project());
			   model.addAttribute("task", new Task());
			   //model.addAttribute("display", "projectDiv");
			   model.addAttribute("employee", new Employee());
			   //model.addAttribute("displayClientDiv", "displayClientDiv");
			  // return "memberDashboard";
			   return "memberDashboard";
		   }
		   else if(emp.getRole().equalsIgnoreCase(PMSCommonConstants.ADMIN))
		   {
			   model.addAttribute("changePasswordDiv", "changePwdDiv");
			   model.addAttribute("employee", new Employee());
			   return "adminDashboard";
		   }
		   else
		   {
			   return "employeeLogin";
		   }
		   
		 
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   @RequestMapping("/changePwdAction")
   public String setChangePasswordtoDB( @ModelAttribute("employee") Employee employee,Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp2=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp2!=null)
	   {
		   Employee emp=(Employee) loginService.checkEmployeeCredentials(emp2);
		   if(employee.getPassword().equalsIgnoreCase(employee.getReEnterPwd()))
		   {
			   emp.setPassword(employee.getPassword());
			   
			   loginService.updateEmployee(emp);
		   }
		   else
		   {
			   model.addAttribute("changePwdError", "Passwords does not Match!!!");
			   model.addAttribute("changePasswordDiv", "changePwdDiv");
			   
		   }
		   if(emp.getRole().equalsIgnoreCase(PMSCommonConstants.MANAGER))
		   {
			   model.addAttribute("display", "projectDiv");
			   model.addAttribute("project", new Project());
			   model.addAttribute("task", new Task());
			   model.addAttribute("employee", new Employee());
			   return "managerDashboard";
		   }
		   else if(emp.getRole().equalsIgnoreCase(PMSCommonConstants.MEMBER))
		   {
			   displayMemberDashboard(model, emp);
			   return "memberDashboard";
		   }
		   else if(emp.getRole().equalsIgnoreCase(PMSCommonConstants.ADMIN))
		   {
			   model.addAttribute("employee", new Employee());
			   return "adminDashboard";
		   }
		   else
		   {
			   return "employeeLogin";
		   }
		   
	   }
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
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
		   
		  int projectStatus=getStatus(project.getProjectId());
		  project.setStatus(projectStatus);
		   model.addAttribute("searchProject", project);
		   model.addAttribute("projectStatus", projectStatus);
		   
	   }
	   model.addAttribute("employee", new Employee());
	   model.addAttribute("tasksDisplayDiv", "tasksDisplayDiv");
	   model.addAttribute("task", new Task());
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
   
   @RequestMapping("/addEmployeeButton")
   public String addEmployeeButtonPressed(Model model,HttpServletRequest request,HttpServletResponse response ,HttpSession session)
   {
	   
	   Employee emp=(Employee)(session.getAttribute("loggedInUser"));
	   if(emp!=null)
	   {
		   //model.addAttribute("searchEmployee", searchEmployee);
		   model.addAttribute("addButtonDisplay", "addButtonPressed");
		   model.addAttribute("employee", new Employee());
		   return "adminDashboard";
	   }
	  
	   model.addAttribute("employee", new Employee());
	   return "employeeLogin";
   }
   
   
 
}