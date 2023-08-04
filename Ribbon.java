import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

// Author Jadd, Nov 8 2020
// To create and style the buttons in the apps Ribbon
// This class assigns certain states to buttons so certain operation can be processed when triggered
public class Ribbon {
	
	private HBox ribbon; private BorderPane parentPane;
	private Button addCourse, deleteCourse, addTopic, editTopic, deleteTopic;
	
	// Initialize GUI features
	public Ribbon(BorderPane parentPane) {
		
		this.parentPane = parentPane;
		ribbon = new HBox();	
			
		addCourse = new Button("Add Course");
		deleteCourse = new Button("Delete Course");	
		addTopic = new Button("Add Topic");
		editTopic = new Button("Edit Topic");
		deleteTopic = new Button("Delete Topic");
		
		setBtnStyle();
		setBtnFunc();

		ribbon.setSpacing(5);
		ribbon.setPadding(new Insets(5, 5, 5, 5));
		ribbon.setStyle("-fx-background-color: #2A2A2A;");
		ribbon.getChildren().addAll(addTopic, editTopic, deleteTopic, addCourse, deleteCourse);	
	}
	
	private void setBtnStyle() {
		
		String style = "-fx-focus-color: transparent;"
				+ "-fx-faint-focus-color: transparent;"
				+ "-fx-background-color: #2E2E2E;"
				+ "-fx-border-color: #666;"
			    + "-fx-border-width: 1 1 1 1;"
				+ "-fx-text-fill: DDD;"
				+ "-fx-font: 15 Courier;";
	
		addCourse.setStyle(style);
		deleteCourse.setStyle(style);
		editTopic.setStyle(style);
		addTopic.setStyle(style);
		deleteTopic.setStyle(style);
	}
	
	// To reveal panels
	private void setBtnFunc() {
			
		addCourse.setOnAction(value -> {
			if(parentPane.getRight() == null) parentPane.setRight(new AddPanel("Add Course", parentPane).getContainer());
			else parentPane.setRight(null);
		});
		deleteCourse.setOnAction(value -> {
			if(parentPane.getRight() == null) parentPane.setRight(new AddPanel("Delete Course", parentPane).getContainer());
			else parentPane.setRight(null);
		});
		addTopic.setOnAction(value -> {
			if(parentPane.getRight() == null) parentPane.setRight(new AddPanel("Add Topic", parentPane).getContainer());
			else parentPane.setRight(null);
		});
		editTopic.setOnAction(value -> {
			if(parentPane.getRight() == null) parentPane.setRight(new AddPanel("Edit Topic", parentPane).getContainer());
			else parentPane.setRight(null);
		});
		deleteTopic.setOnAction(value -> {
			if(parentPane.getRight() == null) parentPane.setRight(new AddPanel("Delete Topic", parentPane).getContainer());
			else parentPane.setRight(null);
		});
	}
	
	// To return 'Ribbon' object
	public HBox getContainer() {return ribbon;}
}