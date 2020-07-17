package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entidade.Vendedor;
import model.exceptions.ValidacaoException;
import model.service.VendedorService;

public class VendedorFormController implements Initializable {

	// Dependencia.
	private Vendedor entidade;
	private VendedorService service;
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

	public void sobrescrevaRefreshDadosListener(AlteracaoDeDadosListener listener) {
		alteracaoDeDadosListeners.add(listener);
	}

	// Injeção da dependencia na Classe.
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	public void setVendedorService(VendedorService service) {
		this.service = service;
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

		// Campo Nome nulo ou vazio. - .trim()= Eliminar espaço em branco no início e no
		// final.
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			erro.addErro("nome", "Nome não pode está vázio.");
		}
		obj.setNome(txtNome.getText());

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
					.setValue(LocalDate.ofInstant(
							entidade.getNascimento().toInstant(), ZoneId.systemDefault()));
		}
	}

	// Mensagem de Erro.
	private void setMensagemDeErro(Map<String, String> erros) {
		Set<String> campos = erros.keySet();

		if (campos.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}
	}

}
