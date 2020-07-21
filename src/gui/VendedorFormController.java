package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import bd.BDException;
import gui.listeners.AlteracaoDeDadosListener;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entidade.Departamento;
import model.entidade.Vendedor;
import model.exceptions.ValidacaoException;
import model.service.DepartamentoService;
import model.service.VendedorService;

public class VendedorFormController implements Initializable {

	// Dependencia.
	private Vendedor entidade;
	private VendedorService service;
	private DepartamentoService departamentoService;
//---------------------------------------------
	// Listener.
	private List<AlteracaoDeDadosListener> alteracaoDeDadosListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML // Data.
	private DatePicker datePickerNascimento;

	@FXML
	private TextField txtSalarioBase;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML // Label de Erros.
	private Label labelErroNome;

	@FXML
	private Label labelErroEmail;

	@FXML
	private Label labelErroNascimento;

	@FXML
	private Label labelErroSalarioBase;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	// Lista de Departamentos.
	private ObservableList<Departamento> obsLista;

	public void sobrescrevaRefreshDadosListener(AlteracaoDeDadosListener listener) {
		alteracaoDeDadosListeners.add(listener);
	}

	// Injeção da dependencia na Classe.
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	// Injeção da dependência Service (Vendedor - Departamento).
	public void setServices(VendedorService service, DepartamentoService departamentoService) {
		this.service = service;
		this.departamentoService = departamentoService;
	}
	// ----------------------------------------------------------------

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nulo!");
		}
		if (service == null) {
			throw new IllegalStateException("Service nulo!");
		}

		try {
			entidade = pegarDadosFormulario();
			service.salvarOuAtualizar(entidade);
			notificarAlteracaoDeDadosListener();
			Utils.stageAtual(evento).close(); // Fechar Janela.
		} catch (BDException ex) {
			Alertas.mostrarAlerta("Erro ao salvar dados", null, ex.getMessage(), AlertType.ERROR);
		} catch (ValidacaoException ex) {
			setMensagemDeErro(ex.getErros()); // Exibir erro na tela.
		}
	}

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}

	// Listener.
	private void notificarAlteracaoDeDadosListener() {
		for (AlteracaoDeDadosListener listener : alteracaoDeDadosListeners) {
			listener.onRefreshDados();
		}
	}

	private Vendedor pegarDadosFormulario() {
		Vendedor obj = new Vendedor();

		// Instanciando a excerção.
		ValidacaoException erro = new ValidacaoException("Erro de validação!");

		obj.setId(Utils.stringParaInteiro(txtId.getText()));

		// Nome. 
		// .trim()= Eliminar espaço em branco no início e no final.
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			erro.addErro("nome", "Campo não pode está vazio.");
		}
		obj.setNome(txtNome.getText());
		
		// Email.
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			erro.addErro("email", "Campo não pode está vazio.");
		}
		obj.setEmail(txtEmail.getText());
		
		// Nascimento (DatePicker).
		if(datePickerNascimento.getValue() == null) {
			erro.addErro("nascimento", "Campo não pode está vazio.");
		}
		else {
		Instant instant = Instant.from(datePickerNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setNascimento(Date.from(instant));
		}
		
		// Salário base (Double).
		if (txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")) {
			erro.addErro("salarioBase", "Campo não pode está vazio.");
		}
		obj.setSalarioBase(Utils.stringParaDouble(txtSalarioBase.getText()));
		
		// Departamento (ComboBox).
		obj.setDepartamento(comboBoxDepartamento.getValue());

		// Erro for maior que zero.
		if (erro.getErros().size() > 0) {
			throw erro;
		}

		return obj;
	}

	// Initializable.
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
	}

	private void inicializarNodes() {
		Restricoes.setTextFieldInteger(txtId);
		Restricoes.setTextFieldTamanhoMaximo(txtNome, 50);
		Restricoes.setTextFieldDouble(txtSalarioBase);
		Restricoes.setTextFieldTamanhoMaximo(txtEmail, 50);
		Utils.formatDatePicker(datePickerNascimento, "dd/MM/yyyy");
		inicializarComboBoxDepartamento(); // Departamento.
	}

	// Preencher os dados do (DialogForm).
	public void atualizarDialogForm() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nulo!");
		}
		txtId.setText(String.valueOf(entidade.getId())); // Transformar ID em String no TextFiled.
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US); // Para definir pontos ao invés de virgula.
		txtSalarioBase.setText(String.format("%.2f", entidade.getSalarioBase()));

		// Fuso horário do sistema.
		if (entidade.getNascimento() != null) {
			datePickerNascimento
					.setValue(LocalDate.ofInstant(entidade.getNascimento().toInstant(), ZoneId.systemDefault()));
		}

		// Preencher o ComboBox (Departamento).
		if (entidade.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().selectFirst(); // Selecione o primeiro.
		} else {
			comboBoxDepartamento.setValue(entidade.getDepartamento());
		}
	}

	// Carregar a lista de Departamento no ComboBox.
	public void carregarObjetosAssociados() {
		if (departamentoService == null) {
			throw new IllegalStateException("DepartamentoService nulo!");
		}
		List<Departamento> lista = departamentoService.buscarTudo();
		obsLista = FXCollections.observableArrayList(lista);
		comboBoxDepartamento.setItems(obsLista);
	}

	// Mensagem de Erro no campo vázio.
	private void setMensagemDeErro(Map<String, String> erros) {
		Set<String> campos = erros.keySet();

		if (campos.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}
		else {
			labelErroNome.setText(erros.get(""));
		}
		
		if (campos.contains("email")) {
			labelErroEmail.setText(erros.get("email"));
		}
		else {
			labelErroEmail.setText(erros.get(""));
		}
		
		if (campos.contains("nascimento")) {
			labelErroNascimento.setText(erros.get("nascimento"));
		}
		else {
			labelErroNascimento.setText(erros.get(""));
		}
		
		if (campos.contains("salarioBase")) {
			labelErroSalarioBase.setText(erros.get("salarioBase"));
		}
		else {
			labelErroSalarioBase.setText(erros.get(""));
		}
	}

	// Inicializar ComboBox Departamento.
	private void inicializarComboBoxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean vazio) {
				super.updateItem(item, vazio);
				setText(vazio ? "" : item.getNome());
			}
		};

		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}
