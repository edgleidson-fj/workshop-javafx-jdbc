package model.exceptions;

import java.util.HashMap;
import java.util.Map;

// Extends RuntimeException.

public class ValidacaoException extends RuntimeException{

		private static final long serialVersionUID = 1L;
		
		// Map<chave, valor>.
		Map<String, String> erros = new HashMap<>();

		public ValidacaoException (String msg) {
			super(msg);
		}
		
		public Map<String, String> getErros(){
			return erros;
		}
		
		// Adicionando erro.
		public void addErro (String nomeDoCampo, String mensagemDeErro) {
			erros.put(nomeDoCampo, mensagemDeErro);
		}
}
