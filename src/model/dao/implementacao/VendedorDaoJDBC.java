package model.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.BD;
import bd.BDException;
import model.dao.VendedorDao;
import model.entidade.Departamento;
import model.entidade.Vendedor;

public class VendedorDaoJDBC implements VendedorDao {

	private Connection connection;

	// Força injeção de dependencia (Connection) dentro da Classe.
	public VendedorDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void inserir(Vendedor obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) " 
					+ "VALUES  (?, ?, ?, ?, ?) ",
					Statement.RETURN_GENERATED_KEYS); // Retornar o ID após o INSERT.

			ps.setString(1, obj.getNome());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getNascimento().getTime())); // Date.
			ps.setDouble(4, obj.getSalarioBase());
			ps.setInt(5, obj.getDepartamento().getId()); // Departamento.

			int linhasAfetadas = ps.executeUpdate();
			if (linhasAfetadas > 0) {
				ResultSet rs = ps.getGeneratedKeys(); // ID gerado no Insert.
				if (rs.next()) {
					int id = rs.getInt(1); // ID do insert.
					obj.setId(id);
				}
				BD.fecharResultSet(rs);
			} else {
				throw new BDException("Erro no INSERT, nenhuma linha foi afetada!");
			}
		} 
		catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} 
		finally {
			BD.fecharStatement(ps);
		}
	}

	@Override
	public void atualizar(Vendedor obj) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(
					"UPDATE seller  "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?  "
					+ "WHERE Id = ? ");

			ps.setString(1, obj.getNome());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getNascimento().getTime())); // Date.
			ps.setDouble(4, obj.getSalarioBase());
			ps.setInt(5, obj.getDepartamento().getId()); // Departamento.
			ps.setInt(6, obj.getId()); // ID do Vendedor.

			ps.executeUpdate();
			} 
		catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} 
		finally {
			BD.fecharStatement(ps);
		}
	}

	@Override
	public void excluirPorId(Integer id) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(
					"DELETE FROM seller  "
					+ "WHERE Id = ? ");
			ps.setInt(1, id);
			ps.execute();
		} 
		catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		}
		finally {
			BD.fecharStatement(ps);
		}
	}

	@Override
	public Vendedor buscarPorId(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ? ");
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Departamento dep = instanciarFuncaoDepartamento(rs);
				Vendedor vend = instaciarFuncaoVendedor(rs, dep);
				return vend;
			}
			return null;
		} catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
			BD.fecharResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> buscarTudo() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT seller.*,department.Name as DepName  FROM seller "
					+ "INNER JOIN department  ON seller.DepartmentId = department.Id " + "ORDER BY Name ");
			rs = ps.executeQuery();

			List<Vendedor> lista = new ArrayList<>();

			// Map<chave , valor> - Para controlar a não repetição do Departamento
			// desnecessáriamente.
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {
				Departamento depNaoRepetido = map.get(rs.getInt("DepartmentId"));

				// Se não existir Map com ID do Departamento o sistema cria um, se não, utiliza
				// o que já existe no Map.
				if (depNaoRepetido == null) {
					depNaoRepetido = instanciarFuncaoDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), depNaoRepetido); // Inserir no Map<chave , valor>.
				}
				Vendedor vend = instaciarFuncaoVendedor(rs, depNaoRepetido);
				lista.add(vend);
			}
			return lista;
		} catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
			BD.fecharResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> buscarPorDepartamento(Departamento departamento) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement("SELECT seller.*,department.Name as DepName  FROM seller "
					+ "INNER JOIN department  ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? "
					+ "ORDER BY Name ");
			ps.setInt(1, departamento.getId());
			rs = ps.executeQuery();

			List<Vendedor> lista = new ArrayList<>();

			// Map<chave , valor> - Para controlar a não repetição do Departamento
			// desnecessáriamente.
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {
				Departamento depNaoRepetido = map.get(rs.getInt("DepartmentId"));

				// Se não existir Map com ID do Departamento o sistema cria um, se não, utiliza
				// o que já existe no Map.
				if (depNaoRepetido == null) {
					depNaoRepetido = instanciarFuncaoDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), depNaoRepetido); // Inserir no Map<chave , valor>.
				}
				Vendedor vend = instaciarFuncaoVendedor(rs, depNaoRepetido);
				lista.add(vend);
			}
			return lista;
		} catch (SQLException ex) {
			throw new BDException(ex.getMessage());
		} finally {
			BD.fecharStatement(ps);
			BD.fecharResultSet(rs);
		}
	}

	// Função Departamento(ResultSet) - Propagando Exception.
	private Departamento instanciarFuncaoDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setNome(rs.getString("DepName"));
		return dep;
	}

	// Função Vendedor(ResultSet, Departamento).
	private Vendedor instaciarFuncaoVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor vend = new Vendedor();
		new Vendedor();
		vend.setId(rs.getInt("Id"));
		vend.setNome(rs.getString("Name"));
		vend.setEmail(rs.getString("Email"));
		vend.setSalarioBase(rs.getDouble("BaseSalary"));
		vend.setNascimento(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
		vend.setDepartamento(dep);
		return vend;
	}
}
