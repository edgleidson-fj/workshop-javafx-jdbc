package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

// Classe utilitária (Utils.java).
public class Utils {

	// Função para retornar o Stage atual.
	public static Stage stageAtual(ActionEvent evento) {
		
		// Necessário fazer os Cast (Stage e Node).
		// O código do evento vai está dentro do parenteses junto com Cast (Node).
		return (Stage)((Node) evento.getSource()).getScene().getWindow();
	}
}
