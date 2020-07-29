package statment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import payroll.Employee;



public class EmployeeStatment {
	
	private final Connection connection;
	
	public EmployeeStatment(Connection connection) {
		this.connection = connection;
	}
	
	public Optional<Employee> getById(Long id) throws SQLException{
		String query = "SELECT id,firstname,lastname,role from employee where id="+id;
		
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		
		if(resultSet.first()) {
			Employee result = new Employee(resultSet.getLong("id"),resultSet.getString("firstname"),resultSet.getString("lastname"),resultSet.getString("role"));
			return Optional.of(result);
		}else {
			return Optional.empty();
		}
	}
	
	public void insert(Employee employee) throws SQLException{
		String query = "INSERT INTO employee(firstname,lastname,role) values('"+employee.getFirstname()+"','"+employee.getLastname()+"','"+employee.getRole()+"')";
		
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
	}
	
	public void insert(List<Employee> employees) throws SQLException{
		for(Employee employee : employees) {
			insert(employee);
		}
	}
	
	public void update(Employee employee) throws SQLException{
		String query = "UPDATE employee SET firstname='"+employee.getFirstname()+"',lastname='"+employee.getLastname()+"',role='"+employee.getRole()+"' WHERE id="+employee.getId();
		
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
	}
	
	public void deleteById(Employee employee) throws SQLException{
		String query = "DELETE FROM employee WHERE id="+employee.getId();
		
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
	}
	
	public List<Employee> getAll() throws SQLException{
		String query = "SELECT id,firstname,lastname,role FROM employee";
		
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		List<Employee> result = new ArrayList<>();
		while (resultSet.next()) {
			result.add(new Employee(resultSet.getLong("id"),resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getNString("role")));
			}
		
		return result;
	}
}
