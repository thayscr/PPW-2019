package br.edu.ifg.sistemacomercial.dao;

import br.edu.ifg.sistemacomercial.entity.Categoria;
import br.edu.ifg.sistemacomercial.entity.Produto;
import br.edu.ifg.sistemacomercial.util.FabricadeConexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void salvar(Produto entity) throws SQLException{
        //Ordem das colunas: senha, nome, login, id, email
        String sqlInsert = "insert into produto (nome, marca, codigoBarras, unidadeMedida, categoria, id) values (?, ?, ?, ?, ?, default)";
        String sqlUpdate = "update produto set nome = ?, marca = ?, codigoBarras = ?, unidadeMedida = ?, categoria = ?, where id = ?";
        
        PreparedStatement  ps;
        if(entity.getId() == null){
            ps = FabricadeConexao.getConexao().prepareStatement(sqlInsert);
        } else {
            ps = FabricadeConexao.getConexao().prepareStatement(sqlUpdate);
            ps.setLong(6, entity.getId());
        }
        ps.setString(1, entity.getNome());
        ps.setString(2, entity.getMarca());
        ps.setString(3, entity.getCodigoBarras());
        ps.setString(4, entity.getUnidadeMedida());
        ps.setInt(5, entity.getCategoria().getId());
        ps.execute();
        FabricadeConexao.fecharConexao();
    }
    
    public void deletar(Produto entity) throws SQLException{
        String sqlDelete = "delete from produto where id = ?";
        PreparedStatement ps = FabricadeConexao.getConexao().prepareStatement(sqlDelete);
        ps.setLong(1, entity.getId());
        ps.execute();
        FabricadeConexao.fecharConexao();
    }
    
    public List<Produto> listar() throws SQLException{
        String sqlQuery = "select * from produto";
        String sqlQueryCategoria = "select * from categoria where id = ?";
        PreparedStatement ps = FabricadeConexao.getConexao().prepareStatement(sqlQuery);
        //java.sql.ResultSet
        ResultSet rs = ps.executeQuery();
        List<Produto> produtos = new ArrayList<>();
        while(rs.next()){
            PreparedStatement psCategoria = FabricadeConexao.getConexao().prepareStatement(sqlQueryCategoria);
            Produto produto = new Produto();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setMarca(rs.getString("marca"));
            produto.setUnidadeMedida(rs.getString("unidadeMedida"));
            
            psCategoria.setInt(1, rs.getInt("categoria"));
            ResultSet rsCategoria = psCategoria.executeQuery();
            if(rsCategoria.next()){
                Categoria cat = new Categoria();
                cat.setId(rsCategoria.getInt("id"));
                cat.setNome(rsCategoria.getString("nome"));
                
                produto.setCategoria(cat);
            }
            produtos.add(produto);
        }
        FabricadeConexao.fecharConexao();
        return produtos;
    }
    
}
