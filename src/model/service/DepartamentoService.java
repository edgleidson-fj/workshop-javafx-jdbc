package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entidade.Departamento;

public class DepartamentoService {

	public List<Departamento> buscarTudo(){
		List<Departamento> lista = new ArrayList<>();
		lista.add(new Departamento(1, "Livros"));
		lista.add(new Departamento(2, "Computadores"));
		lista.add(new Departamento(3, "Eletronicos"));
		lista.add(new Departamento(4, "Livros"));
		lista.add(new Departamento(5, "Computadores"));
		lista.add(new Departamento(6, "Eletronicos"));
		lista.add(new Departamento(7, "Livros"));
		lista.add(new Departamento(8, "Computadores"));
		lista.add(new Departamento(9, "Eletronicos"));
		lista.add(new Departamento(10, "Livros"));
		lista.add(new Departamento(11, "Computadores"));
		lista.add(new Departamento(12, "Eletronicos"));
		return lista;
	}
}
