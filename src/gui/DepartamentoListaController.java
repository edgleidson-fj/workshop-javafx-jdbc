package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidade.Departamento;

public class DepartamentoListaController implements Initializable{

	@FXML  // Tabela Departamento.
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML    // Coluna ID.
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML   // Coluna Nome;
	private TableColumn<Departamento, String> tableColumnNome;
	
	@FXML
	private Button btNovo; 
	
	@FXML
	public void onBtNovoAction() {
		System.out.println("onBtNovoAction()");
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

}
