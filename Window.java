import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Author Jadd, Nov 8 2020
// This class sets up a 'Window' for the graphical interface
public class Window extends Application {

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) throws Exception {		
		
		// Uses 'Borderpane" as panels align on the edges
		BorderPane pane = new BorderPane();	
		pane.setStyle("-fx-background-color: #222;");
		
		pane.setLeft(new CoursePanel(pane).getContainer());
		pane.setTop(new Ribbon(pane).getContainer());
		
		primaryStage.setScene(new Scene(pane));
		primaryStage.setWidth(1300);
		primaryStage.setHeight(800);
		primaryStage.show();
	}
}