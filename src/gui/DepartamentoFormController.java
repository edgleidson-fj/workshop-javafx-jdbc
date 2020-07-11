package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Restricoes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartamentoFormController implements Initializable {

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

}
