package model.dao;

import bd.BD;
import model.dao.implementacao.DepartamentoDaoJDBC;
import model.dao.implementacao.VendedorDaoJDBC;

public class DaoFactory {

	public static VendedorDao criarVendedorDao() {
		return new VendedorDaoJDBC(BD.abrirConexao());
	}
	
	public static DepartamentoDao criarDepartamentoDao() {
		return new DepartamentoDaoJDBC(BD.abrirConexao());
	}
}
