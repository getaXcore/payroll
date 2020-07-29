package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatasourceFactory {
	
	private Connection connection;
	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://192.168.0.47:3306/jtoweb","adminer","IT5upport");
		connection.setAutoCommit(false);
		return connection;
	}
	
	public boolean createTables() throws SQLException{
		String query = "create table if not exists employee(id INT NOT NULL AUTO_INCREMENT,firstname VARCHAR(25),lastname VARCHAR(25),role VARCHAR(25))";
		return connection.createStatement().executeUpdate(query) == 0;
	}
}
