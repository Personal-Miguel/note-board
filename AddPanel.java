import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

// Author Jadd, Nov 8 2020
// To create a form in which multiple operation can be operated dependent on the state of user's interest
public class AddPanel {
	
	private BorderPane pane; private String state;
	private DBControl db; private  VBox addPanel; private Button finish, getDescription; 
	private Text header, id, title, description;
	private TextField editID, editTitle;	
	private TextArea editDescription;	
	
	
	// This initiates the 'AddPanel', its buttons and simple stylings
	public AddPanel(String state, BorderPane pane) {
		
		this.state = state;
		this.pane = pane;
		
		db = new DBControl();
		addPanel = new VBox();
		
		header = new Text();
		title = new Text("TITLE");
		editTitle = new TextField(); 
		id = new Text("ID"); 
		editID = new TextField(); 
		getDescription = new Button("Get Description to EDIT");
		description = new Text("NOTES"); 
		editDescription = new TextArea();	
		finish = new Button("ENTER");
		
		editDescription.setPrefHeight(500);
		editDescription.setPrefWidth(500);
		
		setVisibles();
		setNodeStyle();
		setNodeFunc();
		enableGetDescript();

		addPanel.setSpacing(10);
		addPanel.setPadding(new Insets(10, 10, 10, 10));
		addPanel.setStyle("-fx-background-color: #2A2A2A;"
				+ "-fx-border-color: #666;"
			    + "-fx-border-width: 1 1 1 1;"
				);
		addPanel.getChildren().addAll(header, title, editTitle, id, editID, getDescription, description, editDescription, finish);

	}
	
	private void setNodeStyle() {
		
		String style = "-fx-focus-color: transparent;"
				+ "-fx-faint-focus-color: transparent;"
				+ "-fx-background-color: #2E2E2E;"
				+ "-fx-border-color: #666;"
			    + "-fx-border-width: 1 1 1 1;"
				+ "-fx-text-fill: DDD;"
				+ "-fx-font: 15 Courier;";
		
		header.setStyle(style);
		title.setStyle(style);
		description.setStyle(style);
		id.setStyle(style);
		id.setFill(Color.rgb(221, 221, 221));
		header.setFill(Color.rgb(221, 221, 221));
		title.setFill(Color.rgb(221, 221, 221));
		description.setFill(Color.rgb(221, 221, 221));
		getDescription.setStyle(style);
		finish.setStyle(style);
	}
	
	private void setVisibles() {
						
		// Dependent on the user's request, some buttons are not needed
		getDescription.setVisible(false);
		if(state.equals("Add Course") || state.equals("Delete Course") || state.equals("Delete Topic")) {
			header.setText(state);
			title.setVisible(false);
			editTitle.setVisible(false);
			description.setVisible(false);
			editDescription.setVisible(false);
		}
		else if(state.equals("Edit Topic")) {
			getDescription.setVisible(true);	// To get description to edit
			header.setText(state);
		}
		else header.setText(state);
	}
	
	// Get Description
	private void enableGetDescript() {
		
		getDescription.setOnAction(value -> {
			
			ResultSet description = db.getFromDB("id LIKE '" + editID.getText() + "'");	// To retrieve description to edit
			try {
				editDescription.setText(description.getString("description"));	// Set labels
				editTitle.setText(description.getString("title"));
			}
			catch (SQLException e) {editDescription.setText("CONSOLE: DOES NOT EXIST!");}		
		});
	}
	
	// To reveal panel base of state of user's request like add or delete
	private void setNodeFunc() {
		
		finish.setOnAction(value -> {
			if(state.equals("Add Course") || state.equals("Add Topic")) {
				
				String idValue = editID.getText();
				if(state.equals("Add Course")) {
					idValue += ".0";	// Each course is identified by course#.note# ex. 4434.07, when a course is created a placeholder note 
										// that end with '.0' like 4434.0 is made
					db.addToDB(idValue, editTitle.getText(), editDescription.getText());
				}
				
				else if(state.equals("Add Topic")) {
					try {
						ResultSet result = db.getFromDB("id LIKE '" + idValue + "%'");	// I retrieve the notes from the course the topic requested
																						// to be added to, to confirm the course exists
						idValue += "." + db.generateID(idValue);	// To generate a unique id for a note
						if(result.next()) db.addToDB(idValue, editTitle.getText(), editDescription.getText());

					}
					catch (SQLException e) {
						Alert errorAlert = new Alert(AlertType.ERROR);
						errorAlert.setHeaderText("DATABASE PUSH ERROR");
						errorAlert.setContentText("CONSOLE: UNABLE TO ADD TOPIC FROM PANEL!");
						errorAlert.showAndWait();
					}
				}				
			}
			else if(state.equals("Edit Topic")) {
				db.deleteFromDB(editID.getText());	// To delete old note and replace it when editing
				db.addToDB(editID.getText(), editTitle.getText(), editDescription.getText());			
			}
			else if(state.equals("Delete Topic")) db.deleteFromDB(editID.getText());
			else if(state.equals("Delete Course")) db.deleteFromDB(editID.getText() + "%");
			
			// --------------------------------------------------------------------------------------------------------------
			try {
				pane.setLeft(new CoursePanel(pane).getContainer());	// To reset 'CoursePanel' when a course is added or deleted
				pane.setRight(null);
			}
			catch (SQLException e) {
				
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("DATABASE PULL ERROR");
				errorAlert.setContentText("CONSOLE: UNABLE TO LOAD COURSES TO COURSEPANEL!");
				errorAlert.showAndWait();
			}
		});
	}
	
	// To return 'AddPanel' object
	public VBox getContainer() {return addPanel;}
}
