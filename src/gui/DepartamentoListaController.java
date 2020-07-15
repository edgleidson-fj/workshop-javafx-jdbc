package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import bd.BDIntegrityException;
import gui.listeners.AlteracaoDeDadosListener;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidade.Departamento;
import model.service.DepartamentoService;

public class DepartamentoListaController implements Initializable, AlteracaoDeDadosListener {

	// Dependencia service.
	private DepartamentoService service;

	@FXML // Tabela Departamento.
	private TableView<Departamento> tableViewDepartamento;

	@FXML // Coluna ID.
	private TableColumn<Departamento, Integer> tableColumnId;

	@FXML // Coluna Nome.
	private TableColumn<Departamento, String> tableColumnNome;

	@FXML // Coluna Editar.
	private TableColumn<Departamento, Departamento> tableColumnEditar;

	@FXML // Coluna Excluir.
	private TableColumn<Departamento, Departamento> tableColumnExcluir;

	@FXML
	private Button btNovo;

	// Atributo para associar com a tableView.
	private ObservableList<Departamento> obsLista;

	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		// Retornando Stage atual para caixa de dialogo.
		Stage stagePai = Utils.stageAtual(evento);
		Departamento obj = new Departamento();
		criarDialogForm(obj, "/gui/departamentoFormView.fxml", stagePai);
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

		// Para o tamanho da tableView acompanhar o tamanho da altura da tela. #Não
		// funcionou#
		// Pegando a referencia para (Stage) atual. Necessário fazer o Cast (Stage).
		Stage stage = (Stage) Main.pegarMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	// Injetar dependencia (Service) na classe.
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}

	// Método vai ser resposável por acessar o (Service) carregar os departamentos,
	// e jogar
	// os departamentos na ObservableList(), para depois ser associado com a
	// tableView.
	public void atualizarTableView() {
		if (service == null) {
			throw new IllegalStateException("Service nulo!");
		}
		List<Departamento> lista = service.buscarTudo();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsLista);
		criarBotaoEditar();
		criarBotaoExcluir();
	}

	// DepartamentoForm.
	public void criarDialogForm(Departamento obj, String nomeAbsoluto, Stage stagePai) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane painel = loader.load();

			// Referencia para controlador.
			DepartamentoFormController controle = loader.getController();
			controle.setDepartamento(obj);
			controle.setDepartamentoService(new DepartamentoService());
			controle.sobrescrevaRefreshDadosListener(this); // Refresh na TableView.
			controle.atualizarDialogForm();

			// Caixa de Dialogo.
			Stage stageDialog = new Stage();
			stageDialog.setTitle("Informe os dados do Departamento");
			stageDialog.setScene(new Scene(painel));
			stageDialog.setResizable(false); // Redimencionavel.
			stageDialog.initOwner(stagePai); // Stage pai da janela.
			stageDialog.initModality(Modality.WINDOW_MODAL); // Impedir o acesso de outras janela.
			stageDialog.showAndWait();
		} catch (IOException ex) {
			Alertas.mostrarAlerta("IO Exception", "Erro ao carregar View", ex.getMessage(), AlertType.ERROR);
		}
	}

	// Listener.
	@Override
	public void onRefreshDados() {
		atualizarTableView();
	}

	// Botão Editar.
	private void criarBotaoEditar() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button botao = new Button("Editar");

			@Override
			protected void updateItem(Departamento obj, boolean vazio) {
				super.updateItem(obj, vazio);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(botao);
				botao.setOnAction(
						evento -> criarDialogForm(obj, "/gui/DepartamentoFormView.fxml", Utils.stageAtual(evento)));
			}
		});
	}

	// Botão Excluir.
	private void criarBotaoExcluir() {
		tableColumnExcluir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnExcluir.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Departamento obj, boolean vazio) {
				super.updateItem(obj, vazio);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(evento -> excluirEntidade(obj));
			}
		});
	}

	/*/ Excluir. ***Atenção*** Apresentando erro. 
	private void excluirEntidade(Departamento obj) {
	 Optional<ButtonType> resultado =
			 Alertas.mostrarConfirmacao("Confirmação!", "Você tem certeza que deseja excluir?");
		
	 // Botão OK dentro do (Optional).
	 if(resultado.get() == ButtonType.OK) {
		 if(service == null) {
			 throw new IllegalStateException("Service nulo!");
		 }
		 try {
			 service.excluir(obj);
			 atualizarTableView();
		}
		 catch (BDIntegrityException ex) {
			Alertas.mostrarAlerta("Erro ao excluir objeto", null, ex.getMessage(), AlertType.ERROR);
		}
		 
	 }
	} */
	
	private void excluirEntidade(Departamento obj) {
		//***ATENÇÃO*** Está apresentando erro.
		//Optional<ButtonType> result = Alertas.showConfirmation("Confirmation", "Are you sure to delete?");

		//***ATENÇÃO***
		//---Estou usando os códigos abaixo porque está apresentando erro ao chamar da 
		// classe (gui.util.Alertas).
		Alert alerta = new Alert(AlertType.CONFIRMATION);
		alerta.setTitle("Confirmação!");
		alerta.setHeaderText(null);
		alerta.setContentText("Você tem certeza que deseja excluir?");
		Optional<ButtonType> resultado = alerta.showAndWait(); 
		//------------------------------------------------------		

		if (resultado.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.excluir(obj);
				atualizarTableView();
			}
			catch (BDIntegrityException e) {
				Alertas.mostrarAlerta("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}		
		}
	}
	
	
//--	
}
