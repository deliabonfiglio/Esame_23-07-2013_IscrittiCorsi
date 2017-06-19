package it.polito.esame ;

import java.io.IOException;

import it.polito.esame.bean.Model;
import it.polito.esame.gui.IscrittiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EsameT1 extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		
		FXMLLoader loader = new FXMLLoader( this.getClass().getResource("gui/iscrittiT1.fxml")) ;
		loader.load() ;
		IscrittiController controller = loader.getController();
		Model model = new Model();
		controller.setModel(model);
		
		Parent root = loader.getRoot() ;
		Scene scene = new Scene(root) ;
		primaryStage.setScene(scene) ;
		primaryStage.show() ;
		
	}


	public static void main(String[] args) {
		launch(args);
	}
}
