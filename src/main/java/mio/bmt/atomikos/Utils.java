package mio.bmt.atomikos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public static Connection getConnection(String schema){
		
		String driver = "com.mysql.jdbc.Driver";

		String url = "jdbc:mysql://127.0.0.1:3306/" + schema;

		String user = "root";

		String password = "comedsh006";
			 
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return conn;
		
	}
	
	public static void execute( Connection conn, String sql ) throws SQLException{
		
		Statement statement = conn.createStatement();
		
		statement.executeUpdate(sql);
		
	}
	
}
