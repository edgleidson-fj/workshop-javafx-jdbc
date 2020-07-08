package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemSobre;

	// Evento.
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("onMenuItemVendedorAction()");
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println("onMenuItemDepartamentoAction()");
	}

	@FXML
	public void onMenuItemSobreAction() {
		carregarView("/gui/SobreView.fxml");
	}

	// Método da Interface Initialiazable.
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	// Função para abrir outra tela.
	private synchronized void carregarView(String nomeAbsoluto) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox novoVBox = loader.load(); 
			
			// Definir uma Scene com a referencia da mainScene da classe Main.
			Scene mainScene = Main.pegarMainScene();
			
			// Referencia para VBox da janela principal (MainView).
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			// Referencia para o Menu principal.
			Node mainMenu = mainVBox.getChildren().get(0);
			
			// Limpar os conteúdos (Filhos) da tela anterior.
			mainVBox.getChildren().clear();
			
			// Adicionar os conteúdos (Filho) da nova tela.
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(novoVBox);
		} 
		catch (IOException ex) {
			Alertas.mostrarAlerta("IO Exception", "Erro ao carregar a tela!", ex.getMessage(), AlertType.ERROR);
		}
	}

}
