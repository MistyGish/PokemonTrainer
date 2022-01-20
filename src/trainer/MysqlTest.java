package trainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlTest {

	public static void main(String[] args) {
		// create a new connection
		Connection connection = getConnection();
		System.out.println("connection: " + connection);
		ResultSet rs = null;

		// query all pokemon and print them
		Statement stmt2 = null;
		try {
			stmt2 = connection.createStatement();
			String query = "SELECT * FROM pokemon";
			rs = stmt2.executeQuery(query);
			
			while (rs.next()) {
				String type = rs.getString("type");
				System.out.println(type);
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		

		// close the connection
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

	private static Connection getConnection() {
		try {
			String url = "jdbc:mysql://localhost:3306/pokemondb";
			String username = "root";
			String password = "123456";
			Connection connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}
}