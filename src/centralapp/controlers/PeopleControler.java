package centralapp.controlers;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import centralapp.model.AbstractPerson;
import centralapp.model.Company;
import centralapp.model.Department;
import centralapp.model.Employee;
import centralapp.views.PeopleView;
import centralapp.views.PeopleView.MyDefaultMutableTreeNode;

public class PeopleControler {
	private CentralApp mainControler;
	private PeopleView view;
	
	private int lastSelectedID;
	
	public PeopleControler(CentralApp controler) {
		mainControler = controler;
		view = new PeopleView(controler, this);
	}
	
	public PeopleView getView() {
		return view;
	}
	
	public void updateDepartmentsList(ArrayList<Department> list) {
		view.updateDepartmentsList(list);
	}
	
	public void updatePeopleList(ArrayList<Employee> list) {
		view.updatePeopleList(list);
	}
	
	public class TreeSelectEvent implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent event) {
			MyDefaultMutableTreeNode actualNode = ((MyDefaultMutableTreeNode) event.getNewLeadSelectionPath().getLastPathComponent());
			lastSelectedID = actualNode.getId();
			
			if(lastSelectedID >= 0) {
				Employee employee = mainControler.getCompany().findEmployee(lastSelectedID);
				
				view.setFirstName(employee.getfName());
				view.setLastName(employee.getlName());
				
				//Change selected item on comboBox to Dpt of selected Employee
				view.selectDepartmentId(-((MyDefaultMutableTreeNode)actualNode.getParent()).getId());	
			}
			else
				view.selectDepartmentId(-lastSelectedID);	//Change selected item on comboBox to selected Dpt
		}
	}
	
	public class AddEvent extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {			
			String firstName = view.getFirstName();
			String lastName = view.getLastName();
			Department dpt = view.getDepartment();
			
			Employee employee = new Employee(firstName, lastName);
			if(dpt != null)
				employee.assign(dpt);
				
			mainControler.getCompany().add(employee);
			mainControler.notifyPeopleListModification();
		}
	}
	
	public class ApplyEvent extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {			
			String firstName = view.getFirstName();
			String lastName = view.getLastName();
			Department dpt = view.getDepartment();
			
			AbstractPerson person = mainControler.getCompany().findEmployee(lastSelectedID);
			if(person != null) {
				person.setfName(firstName);
				person.setlName(lastName);
				
				if(person instanceof Employee)
					((Employee)person).assign(dpt);

				mainControler.notifyPeopleListModification();
			}
		}
	}
	
	public class RemoveEvent extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			AbstractPerson emp = mainControler.getCompany().findEmployee(lastSelectedID);
			if(emp != null && emp instanceof Employee) {
				mainControler.getCompany().removeEmployee((Employee)emp);
				mainControler.notifyPeopleListModification();
			}
		}
	}
}
