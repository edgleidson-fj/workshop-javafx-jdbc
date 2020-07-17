package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

// Classe utilit�ria (Utils.java).
public class Utils {

	// Fun��o para retornar o Stage atual.
	public static Stage stageAtual(ActionEvent evento) {
		// Necess�rio fazer os Cast (Stage e Node).
		// O c�digo do evento vai est� dentro do parenteses junto com Cast (Node).
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}

	// Fun��o para transformar String em Inteiro.
	public static Integer stringParaInteiro(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	// Fun��o para formatar campo com Data. - Erro.
	public static <T> void formatTableColumnData(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format); //format
			
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
						}
				}
			};
			return cell;
		});
	}

	// Fun��o para formatar campo com Valores Decimais (Double).
	public static <T> void formatTableColumnValorDecimais(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {

				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};

			return cell;
		});
	}
	
	//--
}
