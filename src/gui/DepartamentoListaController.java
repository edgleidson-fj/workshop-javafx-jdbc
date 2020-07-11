package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidade.Departamento;
import model.service.DepartamentoService;

public class DepartamentoListaController implements Initializable{
	
	// Dependencia service.
	private DepartamentoService service;

	@FXML  // Tabela Departamento.
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML    // Coluna ID.
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML   // Coluna Nome;
	private TableColumn<Departamento, String> tableColumnNome;
	
	@FXML
	private Button btNovo; 
	
	// Atributo para associar com a tableView.
	private ObservableList<Departamento> obsLista;
	
	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		Stage stagePai = Utils.stageAtual(evento);
		criarDialogForm("/gui/departamentoFormView.fxml", stagePai);
	}
	
	// Método do Initializable.
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		inicializarNodes();			
	}

	// Inicializar o comportamento das colunas da tabela.
	private void inicializarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		// Para o tamanho da tableView acompanhar o tamanho da altura da tela.   #Não funcionou#
		// Pegando a referencia para (Stage) atual. Necessário fazer o Cast (Stage).
		Stage stage = (Stage) Main.pegarMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	
	// Injetar dependencia (Service) na classe.
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}
	
	// Método vai ser resposável por acessar o (Service) carregar os departamentos, e jogar 
	// os departamentos na ObservableList(), para depois ser associado coma tableView.
	public void atualizarTableView() {
		if(service == null) {
			throw new IllegalStateException("Service nulo!");
		}
		List<Departamento> lista = service.buscarTudo();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsLista);
	}
	
	// DepartamentoForm.
	public void criarDialogForm(String nomeAbsoluto, Stage stagePai) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane painel = loader.load();
			
			// Caixa de Dialogo.
			Stage stageDialog = new Stage();
			stageDialog.setTitle("Informe os dados do Departamento");
			stageDialog.setScene(new Scene(painel));
			stageDialog.setResizable(false); // Redimencionavel.
			stageDialog.initOwner(stagePai); // Stage pai da janela.
			stageDialog.initModality(Modality.WINDOW_MODAL); // Impedir o acesso de outras janela.
			stageDialog.showAndWait();
		} 
		catch (IOException ex) {
			Alertas.mostrarAlerta("IO Exception", "Erro ao carregar View", ex.getMessage(), AlertType.ERROR);
		}
	}

}
