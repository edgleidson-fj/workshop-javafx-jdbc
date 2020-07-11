package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entidade.Departamento;

public class DepartamentoService {

	// Dependencia
	private DepartamentoDao dao = DaoFactory.criarDepartamentoDao();
	
	public List<Departamento> buscarTudo(){
		return dao.buscarTudo();
	}
}
