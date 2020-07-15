package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entidade.Departamento;

public class DepartamentoService {

	// Dependencia DAO.
	private DepartamentoDao dao = DaoFactory.criarDepartamentoDao();
	
	public List<Departamento> buscarTudo(){
		return dao.buscarTudo();
	}
	
	public void salvarOuAtualizar(Departamento obj) {
		if(obj.getId() == null) {
			dao.inserir(obj);
		}else {
			dao.atualizar(obj);
		}
	}
	
	public void excluir (Departamento obj) {
		dao.excluirPorId(obj.getId());
	}
}
