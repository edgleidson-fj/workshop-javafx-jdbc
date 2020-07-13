package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Restricoes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entidade.Departamento;

public class DepartamentoFormController implements Initializable {
	
	//Dependencia da Entidade Departamento.
	private Departamento entidade;

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
	
	// Injeção da dependendia na Classe.
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}

	@FXML
	public void onBtSalvarAction() {
		System.out.println("onBtSalvarAction()");
	}

	@FXML
	public void onBtCancelarAction() {
		System.out.println("onBtCancelarAction()");
	}

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
