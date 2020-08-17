package com.mercari.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  private static final RowMapper<Integer> COUNT_ROW_MAPPER= (rs, i) -> {
    return rs.getInt("count");
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
  public List<Item> findAll(Integer page){
    String sql="select i.id,i.name,i.condition,i.category,i.brand,"
    +"(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description "
    +"from items as i ORDER BY i.id LIMIT 30 OFFSET :page";
    SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
    List<Item> itemList=template.query(sql,param,ITEM_ROW_MAPPER);
    return itemList; 
  }

  // searchboxのためのメソッド
  public List<Item> findAll(String name,String categoryId,String brandName,Integer page){
    String nameCategoryBrandSql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid) and i.name=:name and i.brand in(select id from brands where name=:brandname) ORDER BY i.id LIMIT 30 OFFSET :page;";
    String nameCategorySql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid) and i.name=:name ORDER BY i.id LIMIT 30 OFFSET :page;";
    String nameBrandSql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i where i.name=:name and i.brand in(select id from brands where name=:brandname) ORDER BY i.id LIMIT 30 OFFSET :page;";
    String categoryBrandSql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid) and i.brand in(select id from brands where name=:brandname) ORDER BY i.id LIMIT 30 OFFSET :page;";
    String nameSql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i where i.name=:name ORDER BY i.id LIMIT 30 OFFSET :page;";
    String brandSql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i where i.brand in(select id from brands where name=:brandname) ORDER BY i.id LIMIT 30 OFFSET :page;";
    String categorySql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid) ORDER BY i.id LIMIT 30 OFFSET :page";
    String sql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i ORDER BY i.id LIMIT 30 OFFSET :page";

    List<Item> itemList = new ArrayList<Item>();
    if(Objects.nonNull(name)&&Objects.nonNull(categoryId)&&Objects.nonNull(brandName)){
      // name,category,brandが全て検索
      SqlParameterSource param = new MapSqlParameterSource().addValue("categoryid", Integer.parseInt(categoryId)).addValue("name", name).addValue("brandname", brandName).addValue("page",page);
      itemList=template.query(nameCategoryBrandSql,param,ITEM_ROW_MAPPER);
    }else if(Objects.nonNull(name)&&Objects.nonNull(categoryId)){
      // name,category
      SqlParameterSource param = new MapSqlParameterSource().addValue("name",name).addValue("categoryid", Integer.parseInt(categoryId)).addValue("page",page);
      itemList=template.query(nameCategorySql,param,ITEM_ROW_MAPPER);
    }else if(Objects.nonNull(name)&&Objects.nonNull(brandName)){
      // name,brand
      SqlParameterSource param = new MapSqlParameterSource().addValue("name",name).addValue("brandname",brandName).addValue("page",page);
      itemList=template.query(nameBrandSql,param,ITEM_ROW_MAPPER);
    }else if(Objects.nonNull(categoryId)&&Objects.nonNull(brandName)){
      // category,brand
      SqlParameterSource param = new MapSqlParameterSource().addValue("categoryid", Integer.parseInt(categoryId)).addValue("brandname", brandName).addValue("page",page);
      itemList=template.query(categoryBrandSql,param,ITEM_ROW_MAPPER);
    }else if(Objects.nonNull(name)){
      // name
      SqlParameterSource param = new MapSqlParameterSource().addValue("name",name).addValue("page",page);
      itemList=template.query(nameSql,param,ITEM_ROW_MAPPER);
    }else if(Objects.nonNull(brandName)){
      SqlParameterSource param = new MapSqlParameterSource().addValue("brandname",brandName).addValue("page",page);
      itemList=template.query(brandSql,param,ITEM_ROW_MAPPER);
    }else if(Objects.nonNull(categoryId)){
      SqlParameterSource param = new MapSqlParameterSource().addValue("categoryid",Integer.parseInt(categoryId)).addValue("page",page);
      itemList=template.query(categorySql,param,ITEM_ROW_MAPPER);
    }else{
      SqlParameterSource param = new MapSqlParameterSource().addValue("page",page);
      itemList=template.query(sql,param,ITEM_ROW_MAPPER);
    }
    
    return itemList;

  }


  public Integer totalCount(String name,String categoryId,String brandName){
    String nameCategoryBrandSql="select count(*) from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid) and i.name=:name and i.brand in(select id from brands where name=:brandname)";
    String nameCategorySql="select count(*) from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid) and i.name=:name";
    String nameBrandSql="select count(*) from items as i where i.name=:name and i.brand in(select id from brands where name=:brandname)";
    String categoryBrandSql="select count(*) from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid) and i.brand in(select id from brands where name=:brandname)";
    String nameSql="select count(*) from items as i where i.name=:name";
    String brandSql="select count(*) from items as i where i.brand in(select id from brands where name=:brandname)";
    String categorySql="select count(*) from items as i where i.category in(select descendant_id from relations where ancestor_id=:categoryid)";
    String sql="select count(*) from items";

    List<Integer> countList = new ArrayList<>();
    if(Objects.nonNull(name)&&Objects.nonNull(categoryId)&&Objects.nonNull(brandName)){
      // name,category,brandが全て検索
      SqlParameterSource param = new MapSqlParameterSource().addValue("categoryid", Integer.parseInt(categoryId)).addValue("name", name).addValue("brandname", brandName);
      countList=template.query(nameCategoryBrandSql,param,COUNT_ROW_MAPPER);
    }else if(Objects.nonNull(name)&&Objects.nonNull(categoryId)){
      // name,category
      SqlParameterSource param = new MapSqlParameterSource().addValue("name",name).addValue("categoryid", Integer.parseInt(categoryId));
      countList=template.query(nameCategorySql,param,COUNT_ROW_MAPPER);
    }else if(Objects.nonNull(name)&&Objects.nonNull(brandName)){
      // name,brand
      SqlParameterSource param = new MapSqlParameterSource().addValue("name",name).addValue("brandname",brandName);
      countList=template.query(nameBrandSql,param,COUNT_ROW_MAPPER);
    }else if(Objects.nonNull(categoryId)&&Objects.nonNull(brandName)){
      // category,brand
      SqlParameterSource param = new MapSqlParameterSource().addValue("categoryid", Integer.parseInt(categoryId)).addValue("brandname", brandName);
      countList=template.query(categoryBrandSql,param,COUNT_ROW_MAPPER);
    }else if(Objects.nonNull(name)){
      // name
      SqlParameterSource param = new MapSqlParameterSource().addValue("name",name);
      countList=template.query(nameSql,param,COUNT_ROW_MAPPER);
    }else if(Objects.nonNull(brandName)){
      SqlParameterSource param = new MapSqlParameterSource().addValue("brandname",brandName);
      countList=template.query(brandSql,param,COUNT_ROW_MAPPER);
    }else if(Objects.nonNull(categoryId)){
      SqlParameterSource param = new MapSqlParameterSource().addValue("categoryid",Integer.parseInt(categoryId));
      countList=template.query(categorySql,param,COUNT_ROW_MAPPER);
    }else{
      countList=template.query(sql,COUNT_ROW_MAPPER);
    }
    return countList.get(0);
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
    String sql="select i.id,i.name,i.condition,i.category,i.brand,(select b.name from brands as b where i.brand=b.id) as brandname,i.price,i.shipping,i.description from items as i order by id desc LIMIT 1 OFFSET 0";
    List<Item> itemList=template.query(sql,ITEM_ROW_MAPPER);
    Integer id=itemList.get(0).getId()+1;
    return id;
  }
  
}