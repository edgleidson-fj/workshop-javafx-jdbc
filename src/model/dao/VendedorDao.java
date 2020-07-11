package model.dao;

import java.util.List;

import model.entidade.Departamento;
import model.entidade.Vendedor;

public interface VendedorDao {

	void inserir(Vendedor obj);
	void atualizar(Vendedor obj);
	void excluirPorId(Integer id);
	Vendedor buscarPorId(Integer id);
	List<Vendedor> buscarTudo();
	List<Vendedor> buscarPorDepartamento(Departamento departamento);
}
