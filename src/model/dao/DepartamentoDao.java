package model.dao;

import java.util.List;

import model.entidade.Departamento;

public interface DepartamentoDao {

	void inserir(Departamento obj);
	void atualizar(Departamento obj);
	void excluirPorId(Integer id);
	Departamento buscarPorId(Integer id);
	List<Departamento> buscarTudo();
}
