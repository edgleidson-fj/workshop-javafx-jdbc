package application;
//Aula 292 - Salvando Vendedor. 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {														 //Diret�rio da View.		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			//C�digo para deixar ScrollPane ajustado a janela.
			scrollPane.setFitToHeight(true); //Altura.
			scrollPane.setFitToWidth(true); //Largura.
			
			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Exemplo de Aplica��o JavaFX");
			primaryStage.show();
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// Pegar MainScene da classe, para disponibilizar para outras classes.
	public static Scene pegarMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
