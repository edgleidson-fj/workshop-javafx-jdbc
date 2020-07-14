package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entidade.Departamento;
import model.exceptions.ValidacaoException;
import model.service.DepartamentoService;

public class DepartamentoFormController implements Initializable {
	
	// Dependencia.
	private Departamento entidade;
	private DepartamentoService service;
//---------------------------------------------
	// Listener.
	private List<AlteracaoDeDadosListener> alteracaoDeDadosListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private Label labelErroNome;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;
	
	public void sobrescrevaRefreshDadosListener(AlteracaoDeDadosListener listener) {
		alteracaoDeDadosListeners.add(listener);
	}
	
	// Injeção da dependencia na Classe.
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}
	
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}
	//----------------------------------------------------------------

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		if(entidade == null) {
			throw new IllegalStateException("Entidade nulo!");
		}
		if(service == null) {
			throw new IllegalStateException("Service nulo!");
		}
		
		try {
			entidade = pegarDadosFormulario();
			service.salvarOuAtualizar(entidade);
			notificarAlteracaoDeDadosListener();
			Utils.stageAtual(evento).close(); // Fechar Janela.
		} 
		catch (BDException ex) {
			Alertas.mostrarAlerta("Erro ao salvar dados", null, ex.getMessage(), AlertType.ERROR);
		}
		catch (ValidacaoException ex) {
			setMensagemDeErro(ex.getErros()); // Exibir erro na tela.
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}
	
	// Listener.
	private void notificarAlteracaoDeDadosListener() {
		for(AlteracaoDeDadosListener listener : alteracaoDeDadosListeners) {
			listener.onRefreshDados();
		}
	}
	
	private Departamento pegarDadosFormulario() {
	Departamento obj = new Departamento();
	
	// Instanciando a excerção.
	ValidacaoException erro = new ValidacaoException("Erro de validação!");
	
	obj.setId(Utils.stringParaInteiro(txtId.getText()));
	
	// Campo Nome nulo ou vazio. - .trim()= Eliminar espaço em branco no início e no final. 
	if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
		erro.addErro("nome", "Nome não pode está vázio.");
	}
	obj.setNome(txtNome.getText());
	
	// Erro for maior que zero.
	if(erro.getErros().size() > 0) {
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
		Restricoes.setTextFieldTamanhoMaximo(txtNome, 30);
	}
	
	// Preencher os dados do (DialogForm).
	public void atualizarDialogForm() {
		if(entidade == null) {
			throw new IllegalStateException("Entidade nulo!");
		}
		txtId.setText(String.valueOf(entidade.getId())); // Transformar ID em String no TextFiled.
		txtNome.setText(entidade.getNome());
	}
	
	// Mensagem de Erro.
	private void setMensagemDeErro(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		if(campos.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}
	}

}
