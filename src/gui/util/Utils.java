package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage stageAtual(ActionEvent evento) {
		// Necess�rio fazer os Cast (Stage e Node).
		// O c�digo do evento vai est� dentro do parenteses junto com Cast (Node).
		return (Stage)((Node) evento.getSource()).getScene().getWindow();
	}
}
