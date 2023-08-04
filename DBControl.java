import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// Author Jadd, Nov 8 2020
public class DBControl {
	
	private Connection conn;
	private boolean hasData = false;

	private void getConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:notes.db");
		
		if(!hasData) {

			hasData = true;			
			conn.createStatement()
				.execute("CREATE TABLE IF NOT EXISTS notes(id varchar(10),"
						+ "title varchar(10), "
						+ "description varchar(600)"
						+ ");");		
		}
	}
	
	public void addToDB(String id, String title, String description) {
		
		PreparedStatement query = null;	
		if(conn == null)
			try {getConnection();}
			catch (ClassNotFoundException | SQLException connError) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("DATABASE CONN ERROR");
				errorAlert.setContentText("CONSOLE: CONNECTION TO DATABASE FAILED!");
				errorAlert.showAndWait();
			}
		
		ResultSet check = getFromDB("id LIKE " + id);		
		try {
			
			if(check.next()) throw new SQLException("Exists");
			
			query = conn.prepareStatement("INSERT INTO notes(id, title, description) VALUES(?,?,?)");
			query.setString(1, id);
			query.setString(2, title);
			query.setString(3, description);
			query.execute();
			conn.close();

		}
		catch (SQLException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("DATABASE PUSh ERROR");
			errorAlert.setContentText("CONSOLE: UNABLE TO ADD TOPIC TO COURSEPANEL!");
			errorAlert.showAndWait();
		}
	}
	
	public ResultSet getFromDB(String request) {
		
		ResultSet result = null;
		if(conn == null)
			try {getConnection();}
			catch (ClassNotFoundException | SQLException connError) {connError.printStackTrace();}

		try {result = conn.createStatement().executeQuery("SELECT id, title, description FROM notes WHERE " + request);}
		catch (SQLException e) {	
			
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("DATABASE PULL ERROR");
			errorAlert.setContentText("CONSOLE: UNABLE TO LOAD COURSES FROM DATABASE!");
			errorAlert.showAndWait();
			}
		
        return result;	
	}
	
	public void deleteFromDB(String request) {
		
		if(conn == null)
			try {getConnection();}
			catch (ClassNotFoundException | SQLException connError) {connError.printStackTrace();}

		try {

			String sql = "DELETE FROM notes WHERE id LIKE ?";
			PreparedStatement query = conn.prepareStatement(sql);
			query.setString(1, request);
			query.execute();

		}
		catch (SQLException e) {	
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("DATABASE PULL ERROR");
			errorAlert.setContentText("UNABLE TO DELETE NOTES IN DATABASE!");
			errorAlert.showAndWait();
			}
	}
	
	public String generateID(String courseID) throws SQLException {
			
		for (int i = 0; i < 5; i++) {
			
			int identification = new Random().nextInt(100) + 1;
			ResultSet dataSet = getFromDB("id LIKE " + courseID + "." + String.valueOf(identification));

			if(!dataSet.next()) return String.valueOf(identification);
		}	
		
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText("DATABASE PULL ERROR");
		errorAlert.setContentText("CONSOLE: ID GENERATING EXHAUSTED!");
		errorAlert.showAndWait();
		return "0"; // returning 0 is harmless as it acts as a placeholder when an ID can't be generated and duplicates won't be added
	}
}
