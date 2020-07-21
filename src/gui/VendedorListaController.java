package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import model.entidade.Vendedor;
import model.service.DepartamentoService;
import model.service.VendedorService;

public class VendedorListaController implements Initializable, AlteracaoDeDadosListener {

	// Dependencia service.
	private VendedorService service;

	@FXML // Tabela Vendedor.
	private TableView<Vendedor> tableViewVendedor;

	@FXML // Coluna ID.
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML // Coluna Nome.
	private TableColumn<Vendedor, String> tableColumnNome;
	
	@FXML // Coluna Email.
	private TableColumn<Vendedor, String> tableColumnEmail;
	
	@FXML // Coluna Data de Nascimento.
	private TableColumn<Vendedor, Date> tableColumnNascimento;
	
	@FXML // Coluna Salário Base.
	private TableColumn<Vendedor, Double> tableColumnSalarioBase;

	@FXML // Coluna Editar.
	private TableColumn<Vendedor, Vendedor> tableColumnEditar;

	@FXML // Coluna Excluir.
	private TableColumn<Vendedor, Vendedor> tableColumnExcluir;
	
	@FXML // Coluna Departamento.
	private TableColumn<Departamento, Departamento> tableColumnDep;

	@FXML
	private Button btNovo;

	private ObservableList<Vendedor> obsLista;

	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		Stage stagePai = Utils.stageAtual(evento);
		Vendedor obj = new Vendedor();
		criarDialogForm(obj, "/gui/VendedorFormView.fxml", stagePai);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
		}

	private void inicializarNodes() {
		tableColumnDep.setCellValueFactory(new PropertyValueFactory<>("departamento"));
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnNascimento.setCellValueFactory(new PropertyValueFactory<>("nascimento"));
		Utils.formatTableColumnData(tableColumnNascimento, "dd/MM/yyyy"); //Formatar Data.
		tableColumnSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		Utils.formatTableColumnValorDecimais(tableColumnSalarioBase, 2); //Formatar com (0,00).

		Stage stage = (Stage) Main.pegarMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void setVendedorService(VendedorService service) {
		this.service = service;
	}

	public void atualizarTableView() {
		if (service == null) {
			throw new IllegalStateException("Service nulo!");
		}
		List<Vendedor> lista = service.buscarTudo();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewVendedor.setItems(obsLista);
		criarBotaoEditar();
		criarBotaoExcluir();
	}

	// VendedorForm.
	public void criarDialogForm(Vendedor obj, String nomeAbsoluto, Stage stagePai) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane painel = loader.load();

			VendedorFormController controle = loader.getController();
			controle.setVendedor(obj);
			controle.setServices(new VendedorService(), new DepartamentoService());
			controle.carregarObjetosAssociados(); // Lista de Departamentos.
			controle.sobrescrevaRefreshDadosListener(this); // Refresh na TableView.
			controle.atualizarDialogForm();

			Stage stageDialog = new Stage();
			stageDialog.setTitle("Informe os dados do Vendedor");
			stageDialog.setScene(new Scene(painel));
			stageDialog.setResizable(false); // Redimencionavel.
			stageDialog.initOwner(stagePai); // Stage pai da janela.
			stageDialog.initModality(Modality.WINDOW_MODAL); // Impedir o acesso de outras janela.
			stageDialog.showAndWait();
		} catch (IOException ex) {
			ex.printStackTrace();
			Alertas.mostrarAlerta("IO Exception", "Erro ao carregar View", ex.getMessage(), AlertType.ERROR);
		}
	} 

	@Override
	public void onRefreshDados() {
		atualizarTableView();
	}

	private void criarBotaoEditar() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button botao = new Button("Editar");

			@Override
			protected void updateItem(Vendedor obj, boolean vazio) {
				super.updateItem(obj, vazio);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(botao);
				botao.setOnAction(
						evento -> criarDialogForm(obj, "/gui/VendedorFormView.fxml", Utils.stageAtual(evento)));
			}
		});
	}   

	
	private void criarBotaoExcluir() {
		tableColumnExcluir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnExcluir.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Vendedor obj, boolean vazio) {
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

	private void excluirEntidade(Vendedor obj) {
		Optional<ButtonType> resultado = Alertas.mostrarConfirmacao("Confirmação", "Você tem certeza que deseja excluir?");

		if (resultado.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service nulo?");
			}
			try {
				service.excluir(obj);
				atualizarTableView();
			}
			catch (BDIntegrityException ex) {
				Alertas.mostrarAlerta("Erro ao excluir objeto", null, ex.getMessage(), AlertType.ERROR);
			}			
		}
	}
	
	
//--	
}
