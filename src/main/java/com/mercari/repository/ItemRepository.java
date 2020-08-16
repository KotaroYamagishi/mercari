package com.mercari.repository;

import java.util.List;
import java.util.Objects;

import com.mercari.domain.Brand;
import com.mercari.domain.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {
  @Autowired
  private NamedParameterJdbcTemplate template;

  private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
    Item item = new Item();
    item.setId(rs.getInt("id"));
    item.setName(rs.getString("name"));
    item.setCondition(rs.getInt("condition"));
    item.setCategory(rs.getInt("category"));
    item.setBrand(rs.getInt("brand"));
    item.setPrice(rs.getInt("price"));
    item.setShipping(rs.getInt("shipping"));
    item.setDescription(rs.getString("description"));
    return item;
  };


  
  /** 
   * itemテーブルに新しいitemをinsertするためのメソッド
   * 
   * @param item
   * @return Integer
   */
  public void save(Item item){
    String sql = "INSERT INTO items(id,name,condition,category,brand,price,shipping,description) VALUES (:id,:name, :condition, :category, :brand, :price, :shipping,:description)";
    SqlParameterSource param = new MapSqlParameterSource().addValue("id", item.getId()).addValue("name",item.getName()).addValue("condition",item.getCondition()).addValue("category",item.getCategory()).addValue("brand",item.getBrand())
                                  .addValue("price", item.getPrice()).addValue("shipping",item.getShipping()).addValue("description",item.getDescription());
    Integer count = template.update(sql, param);
  }

  
  // paging機能が導入できるまではlimitで取得できる情報を制限する
  /** 
   * itemsテーブルの全情報を取得するためのメソッド
   * 
   * @return List<Item>
   */
  public List<Item> findAll(){
    String sql="select i.id,i.name,i.condition,i.category,i.brand,"
    +"(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description "
    +"from items as i LIMIT 30 OFFSET 0";
    List<Item> itemList=template.query(sql,ITEM_ROW_MAPPER);
    return itemList; 
  }

  
  /** 
   * idから該当するitemの情報を取得するメソッド
   * 
   * @param id
   * @return Item
   */
  public Item load(Integer id){
    String sql="select i.id,i.name,i.condition,i.category,i.brand,"
    +"(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description "
    +"from items as i where i.id=:id";
    SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
    Item item=template.queryForObject(sql, param, ITEM_ROW_MAPPER);
    return item;
  }

  
  /** 
   * itemの情報を編集するためのメソッド
   * 
   * @param item
   */
  public void update(Item item){
    String sql="UPDATE items SET name=:name,condition=:condition,category=:category,brand=:brand,price=:price,description=:description WHERE id=:id";
    SqlParameterSource param = new BeanPropertySqlParameterSource(item);
    template.update(sql, param);
  }

  public Integer insertId(){
    String sql="select * from items order by id desc LIMIT 1 OFFSET 0";
    List<Item> itemList=template.query(sql,ITEM_ROW_MAPPER);
    Integer id=itemList.get(0).getId()+1;
    return id;
  }
  
}