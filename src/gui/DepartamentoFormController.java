package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import bd.BDException;
import gui.listeners.DataChangeListener;
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
import model.service.DepartamentoService;

public class DepartamentoFormController implements Initializable {
	
	//Dependencia.
	private Departamento entidade;
	private DepartamentoService service;
//---------------------------------------------
	private List<DataChangeListener> refreshListeners = new ArrayList<>();
	
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
	
	public void sobrescrevaRefreshDadosListener(DataChangeListener listener) {
		refreshListeners.add(listener);
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
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}
	
	// Listener.
	private void notificarAlteracaoDeDadosListener() {
		for(DataChangeListener listener : refreshListeners) {
			listener.onDataChanged();
		}
	}
	
	private Departamento pegarDadosFormulario() {
	Departamento obj = new Departamento();
	
	obj.setId(Utils.stringParaInteiro(txtId.getText()));
	obj.setNome(txtNome.getText());
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

}
