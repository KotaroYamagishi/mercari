package com.mercari.repository;

import java.util.List;

import com.mercari.domain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OriginalRepository {
  
  @Autowired
  private NamedParameterJdbcTemplate template;

  private static final RowMapper<Original> ORI_ROW_MAPPER = (rs, i) -> {
    Original original = new Original();
    original.setId(rs.getInt("id"));
    original.setName(rs.getString("name"));
    original.setCondition(rs.getInt("condition_id"));
    original.setCategoryName(rs.getString("category_name"));
    original.setBrand(rs.getString("brand"));
    original.setPrice(rs.getInt("price"));
    original.setShipping(rs.getInt("shipping"));
    original.setDescription(rs.getString("description"));
    return original;
  };

  public List<Original> findAll(){
    // String sql = "SELECT * FROM original ORDER BY id LIMIT 400000 OFFSET 0";
    // String sql = "SELECT * FROM original ORDER BY id LIMIT 400000 OFFSET 400001";
    // String sql = "SELECT * FROM original ORDER BY id LIMIT 400000 OFFSET 800001";
    String sql = "SELECT * FROM original ORDER BY id LIMIT 400000 OFFSET 1200001";
    SqlParameterSource param = new MapSqlParameterSource();
    List<Original> originalList = template.query(sql, param, ORI_ROW_MAPPER);
    return originalList;
  }
}