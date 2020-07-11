package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage stageAtual(ActionEvent evento) {
		// Necessário fazer os Cast (Stage e Node).
		// O código do evento vai está dentro do parenteses junto com Cast (Node).
		return (Stage)((Node) evento.getSource()).getScene().getWindow();
	}
}
