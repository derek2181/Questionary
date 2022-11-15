package com.piaweb.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.piaweb.models.Category;

public class CategoriesDB {
	private DataSource source;
	public CategoriesDB(DataSource source) {
		this.source = source;
	}
	
	public Category getCategoryById(int id) throws SQLException {
		Category category = new Category();
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Categorias(?,?)}");
			st.setString(1, "I");
			st.setInt(2, id);
			ResultSet result = st.executeQuery();
			result.next();
			category.setID_Categoria(result.getInt("ID_Categoria"));
			category.setNombre(result.getString("nombre"));
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			connection.close();
			st.close();			
		}
		
		return category;
	}
	
	public List<Category> getAllCategories() throws SQLException{
		List<Category> listOfCategories = new ArrayList<Category>();
		Connection connection = null;
		CallableStatement st = null;
		
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Categorias(?, null)}");
			st.setString(1, "G");
			ResultSet result = st.executeQuery();
			while(result.next()) {
				Category category = new Category();
				category.setID_Categoria(result.getInt("ID_Categoria"));
				category.setNombre(result.getString("nombre"));
				listOfCategories.add(category);
			}
			
			
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			connection.close();
			st.close();
			
		}
		
		return listOfCategories;
	}
	
}
