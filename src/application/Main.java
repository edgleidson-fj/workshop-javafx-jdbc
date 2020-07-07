package application;
//Aula 268 -Design da Tela MainView.	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {														 //Diretório da View.		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			//Código para deixar ScrollPane ajustado a janela.
			scrollPane.setFitToHeight(true); //Altura.
			scrollPane.setFitToWidth(true); //Largura.
			
			Scene mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Exemplo de Aplicação JavaFX");
			primaryStage.show();
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
