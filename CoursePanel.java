import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

// Author Jadd, Nov 8 2020
// To display created courses
public class CoursePanel {
	
	private VBox coursePanel;
	
	public CoursePanel(BorderPane parentPane) throws SQLException {
		
		DBControl db = new DBControl();
		ResultSet dataSet = db.getFromDB("1=1");	// To retrieve all notes from the database
		coursePanel = new VBox();
		
		while(dataSet.next()) {
			
			String courseID = dataSet.getString("id");		
			if(!(courseID.substring(courseID.indexOf(".")+1)).equals("0") || courseID.indexOf(".") == -1) continue;	// Each notes is identified by course#.note# ex. 4434.07, when a course is created a placeholder note 4434.0
			Button course = new Button(courseID.substring(0, courseID.indexOf(".")));	// is made and that is what these lines of code parse and look for to get course buttons.
			
			course.setPrefWidth(75);
			course.setPrefHeight(30);
			course.setStyle("-fx-focus-color: transparent;"
							+ "-fx-faint-focus-color: transparent;"
							+ "-fx-background-color: #2E2E2E;"
							+ "-fx-border-color: #666;"
						    + "-fx-border-width: 1 1 1 1;"
							+ "-fx-text-fill: DDD;"
							+ "-fx-font: 15 Courier;");
						
			course.setOnAction(value -> {new DrawNotes(courseID, parentPane);});	// When the button corresponding to a course is triggered, it displays all its notes			
			coursePanel.getChildren().add(course);		
			}
		
		coursePanel.setSpacing(5);
		coursePanel.setPadding(new Insets(5, 5, 5, 5));
		coursePanel.setStyle("-fx-background-color: #2A2A2A;");
		
		dataSet.close();
	}
	
	// To return 'CoursePanel'object
	public VBox getContainer() {return coursePanel;}
}
