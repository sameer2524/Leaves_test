
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.*; 
import java.lang.*;

public class LeaveApplication{

	private static final String dataFile = "database.bin";
	private static Employee loggedinEmployee = null;
	private static List<Employee> employeeList;

	public LeaveApplication(){
		employeeList = new ArrayList<Employee>();
		ReadManager manager = new ReadManager(dataFile);
		Employee employee;
		boolean EOF=false;
		while(!EOF){
			try{
				employee = (Employee) manager.readObject();
				employeeList.add(employee);	
				if(employee.getEmpNo() > Employee.employeeLastId){
					Employee.employeeLastId = employee.getEmpNo();
				}
				for(int i=0; i < employee.leaveRequest.size(); i++){
					if(employee.leaveRequest.get(i).getRequestId() > LeaveRequest.leaveRequestLastId){
						LeaveRequest.leaveRequestLastId = employee.leaveRequest.get(i).getRequestId();
					}
				}
			} catch(Exception e){
				EOF = true;
			}	
		}
		if(employeeList.size() == 0){
			employee = new Employee("Sameer");
			employeeList.add(employee);	
			System.out.println("\nAs the application is new there are no employee. So login with admin account and add employee.");
			System.out.println("Admin credentials: Id = 1\tname = " + employee.getName());
		}
		
	}

	public static Employee login(int id){
		for(int i=0; i < employeeList.size(); i++){
			if(id == employeeList.get(i).getEmpNo()){
				return employeeList.get(i);
			}
		}
		System.out.println("Invalid Employee Id.Please try again.");
		return null;
	}

	public void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	} 

	public void printMenu(){
		System.out.println("\n\n-----Menu-----");
		System.out.println("1. View applied leaves");
		System.out.println("2. Apply for leave");
		System.out.println("3. Cancel leave");
		System.out.println("4. View leave summary");
		System.out.println("5. Logout");
		System.out.println("6. Add User");
		System.out.println("7. Exit");

	}

	public void handleUserAction(int choice){
		Scanner input = new Scanner(System.in); 
		switch(choice){
			case 1:
				if(loggedinEmployee.leaveRequest.size() == 0){
					System.out.println("YOu haven't applied for leaves.");
					break;
				}
				for(int i=0; i < loggedinEmployee.leaveRequest.size(); i++){
					System.out.println(loggedinEmployee.leaveRequest.get(i));
				}  
				break;
			case 2:
				System.out.println("Enter reason for leave : ");
				String reason = input.nextLine();
				LeaveRequest addedLeaveRequest = loggedinEmployee.addLeaveRequest(reason);
				if (addedLeaveRequest != null){
					System.out.println("Leave Successfully Applied.");
					System.out.println(addedLeaveRequest);
				} else{
					System.out.println("You had excedded you leave limit. Leave Limit = " + loggedinEmployee.getMaxLeavesLimit());
				}
				break;
			case 3:
				System.out.println("Enter leave requst id to cancel : ");
				int id = input.nextInt();
				LeaveRequest removedLeaveRequest = loggedinEmployee.removeLeaveRequest(id);
				if (removedLeaveRequest != null){
					System.out.println("Leave Successfully Cancelled.");
					System.out.println(removedLeaveRequest);
				} else{
					System.out.println("PLease check whether you had entered a valid request id.");
				}
				break;
			case 4:
				System.out.println(loggedinEmployee.leaveSummary());
				break;
			case 5:
				System.out.println("Bye " + loggedinEmployee.getName());
				loggedinEmployee = null;
				break;
			case 6:
				System.out.println("Enter name of the employee : ");
				String name = input.nextLine();
				Employee employee = new Employee(name);
				employeeList.add(employee);
				System.out.println("Employee added successfully.");
				System.out.println(employee.leaveSummary());
				break;
			case 7:
				System.exit(0);
			deault:
				System.out.println("Please enter a valid choice from the menu");

		}
	}

	public static void main(String[] args){
		ShutDownTask shutDownTask = new ShutDownTask();
		Runtime.getRuntime().addShutdownHook(shutDownTask);

		LeaveApplication leaveApplication = new LeaveApplication(); 
		Scanner input = new Scanner(System.in); 
		while(true){
			if(loggedinEmployee == null){
				System.out.println("\nEnter Employee Id as login credentials : ");
				int id = input.nextInt();
				loggedinEmployee = login(id);
				System.out.println("\nHi " + loggedinEmployee.getName() + " (ID=" + loggedinEmployee.getEmpNo() + ")");
			}
			else{
				leaveApplication.printMenu();
				System.out.println("\nEnter your choice : ");
				int choice = input.nextInt();
				// leaveApplication.clearScreen();
				leaveApplication.handleUserAction(choice); 
			}
		}
	}

	private static class ShutDownTask extends Thread {
 
		@Override
		public void run() {
			System.out.println("Storing application state before exit.");
			WriteManager manager = new WriteManager(dataFile);
			try{
				for(int i=0; i < employeeList.size(); i++){
					manager.writeObject(employeeList.get(i));
				}
			} catch(Exception e){
				System.out.println("Something went wrong while storing data." + e.toString());
				return;
			}	
			
		}
 
   }

}


