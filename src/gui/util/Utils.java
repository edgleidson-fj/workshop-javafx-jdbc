package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

// Classe utilit�ria (Utils.java).
public class Utils {

	// Fun��o para retornar o Stage atual.
	public static Stage stageAtual(ActionEvent evento) {
		
		// Necess�rio fazer os Cast (Stage e Node).
		// O c�digo do evento vai est� dentro do parenteses junto com Cast (Node).
		return (Stage)((Node) evento.getSource()).getScene().getWindow();
	}
}
