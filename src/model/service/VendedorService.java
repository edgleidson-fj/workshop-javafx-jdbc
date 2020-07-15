package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entidade.Vendedor;

public class VendedorService {

	// Dependencia DAO.
	private VendedorDao dao = DaoFactory.criarVendedorDao();
	
	public List<Vendedor> buscarTudo(){
		return dao.buscarTudo();
	}
	
	public void salvarOuAtualizar(Vendedor obj) {
		if(obj.getId() == null) {
			dao.inserir(obj);
		}else {
			dao.atualizar(obj);
		}
	}
	
	public void excluir (Vendedor obj) {
		dao.excluirPorId(obj.getId());
	}
}
