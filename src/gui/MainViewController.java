package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.service.DepartamentoService;
import model.service.VendedorService;

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
		carregarView("/gui/VendedorListaView.fxml", (VendedorListaController controle) -> {
			controle.setVendedorService(new VendedorService());
			controle.atualizarTableView();
		});
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		// Na hora de passar a chamada (carregarView), incluir segundo parâmetro para passar
		// uma função com (Expressão Lambida) para inicializar o Controller.
		carregarView("/gui/DepartamentoListaView.fxml", (DepartamentoListaController controle) -> {
			controle.setDepartamentoService(new DepartamentoService());
			controle.atualizarTableView();
		});
	}

	@FXML
	public void onMenuItemSobreAction() {
		carregarView("/gui/SobreView.fxml", x -> {/* Vázio */});
	}

	// Método da Interface Initialiazable.
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	// Função para abrir outra tela com dois parâmetros (String, Consumer<T>).
	private synchronized <T> void carregarView(String nomeAbsoluto, Consumer<T> consumerAcaoDeInicializacao) {
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

			// Consumer <T> - Comando para executar as funções que for passada por
			// argumentos nos métodos(Eventos).
			T controle = loader.getController();
			consumerAcaoDeInicializacao.accept(controle);
		}
		catch (IOException ex) {
			Alertas.mostrarAlerta("IO Exception", "Erro ao carregar a tela!", ex.getMessage(), AlertType.ERROR);
		}
	}

}
