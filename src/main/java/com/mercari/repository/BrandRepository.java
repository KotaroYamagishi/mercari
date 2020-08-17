package com.mercari.repository;

import com.mercari.domain.Brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BrandRepository {

    private static final RowMapper<Brand> BRAND_ROW_MAPPER = (rs, i) -> {
        Brand brand = new Brand();
        brand.setId(rs.getInt("id"));
        brand.setName(rs.getString("name"));
        return brand;
      };

      @Autowired
  private NamedParameterJdbcTemplate template;

    /** 
   * brandIdを引数にしてbrandNameを取得するメソッド
   * 
   * @param brandId
   */
  public Brand findById(Integer brandId){
    try{
      String sql="SELECT * FROM brands WHERE id=:id";
      SqlParameterSource param = new MapSqlParameterSource().addValue("id", brandId);
      Brand brand=template.queryForObject(sql, param,BRAND_ROW_MAPPER);
      return brand;
    }catch(Exception e){
      return null;
    }
  }

  public Brand findByBrandName(String brandName){
    String sql="SELECT * FROM brands WHERE name=:name";
    SqlParameterSource param=new MapSqlParameterSource().addValue("name", brandName);
    try{
        Brand brand=template.queryForObject(sql, param,BRAND_ROW_MAPPER);
        return brand;
    }catch(EmptyResultDataAccessException e){
        return null;
    }
  }

  public Integer insert(String brandName){
      String insertSql="INSERT INTO brands(name) values (:name) RETURNING id";
      SqlParameterSource param = new MapSqlParameterSource().addValue("name",brandName);
      Integer barndId=template.queryForObject(insertSql, param,Integer.class);
      return barndId;
  }
}