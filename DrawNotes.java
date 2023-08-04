import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

// Author Jadd, Nov 8 2020
// To illustrate the contents of the database, the description of tasks are assigned to buttons
public class DrawNotes {	
	
	private BorderPane pane;
	
	public DrawNotes(String courseID, BorderPane pane) {
		
		this.pane = pane;
		
		DBControl db = new DBControl();
		StackPane board = new StackPane();
		pane.setCenter(board);

		ResultSet result = db.getFromDB("id LIKE '" + courseID.substring(0, courseID.indexOf(".")) + "%'");	// To get all notes specified by course

		try {
			
			int xCounter = 0;	// Initialize coordinates of buttons and remember
			int yCounter = 0;
			
			while(result.next()) {
				
				if((result.getString("id").substring(result.getString("id").indexOf(".")+1)).equals("0")) continue;	// Ignore placeholder IDs like 4466.0
				Button topic = new Button(result.getString("title"));	
				VBox studycard = assignStudyCard(result.getString("title"), result.getString("id"), result.getString("description"));	// To create a card assigned to button
				topic.setOnAction(value -> {pane.setCenter(studycard);});	// To set display note card onscreen when triggered
				
				setTopicStyle(topic, xCounter, yCounter);
				board.getChildren().add(topic);

				xCounter += 1;	// To calculate button placement
				if(xCounter % 6 == 0) yCounter += 1;
			}
		}
		catch (SQLException e) {
			
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("DATABASE PULL ERROR");
			errorAlert.setContentText("UNABLE TO LOAD NOTES!");
			errorAlert.showAndWait();
		}
	}
	
	private void setTopicStyle(Button topic, int xCounter, int yCounter) {
		
		topic.setPrefWidth(150); topic.setPrefHeight(30);
		topic.setTranslateX(-500 + ((200 * xCounter) % 1200));
		topic.setTranslateY(-315 + (yCounter * 50));	
		
		topic.setStyle("-fx-focus-color: transparent;"
				+ "-fx-faint-focus-color: transparent;"
				+ "-fx-background-color: #2E2E2E;"
				+ "-fx-border-color: #666;"
			    + "-fx-border-width: 1 1 1 1;"
				+ "-fx-text-fill: DDD;"
				+ "-fx-font: 15 Courier;");
	}
	
	// Load card with note data
	private VBox assignStudyCard(String title, String id, String description) {
	
		VBox studyCard = new VBox();
		studyCard.setStyle("-fx-focus-color: transparent;"
				+ "-fx-faint-focus-color: transparent;"
				+ "-fx-background-color: #2A2A2A;");
				
		Button close = new Button("x");
		Label topicTitle = new Label("TITLE: " + title);
		Label topicID = new Label("ID: " + id );
		Label topicDescription = new Label("----------------------------------------------------------------------\n" + description);
		
		
		close.setOnAction(value -> {pane.setCenter(null);});	
		
		String style = "-fx-focus-color: transparent;"
				+ "-fx-faint-focus-color: transparent;"
				+ "-fx-background-color: #2A2A2A;"
				+ "-fx-text-fill: DDD;"
				+ "-fx-padding: 5 5 5 5;";
		
		close.setStyle(style + "-fx-font: 15 Courier;");
		topicTitle.setStyle(style + "-fx-font: 20 Courier;");
		topicID.setStyle(style + "-fx-font: 15 Courier;");
		topicDescription.setStyle(style + "-fx-font: 15.8 Courier;");
		
		studyCard.getChildren().addAll(close, topicTitle, topicID, topicDescription);

		studyCard.setMaxWidth(700);
		studyCard.setMaxHeight(700);

		return studyCard;
	}
}