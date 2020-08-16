package com.mercari.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.mercari.domain.Brand;
import com.mercari.domain.Category;
import com.mercari.domain.Relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MercariRepository {

  @Autowired
  private NamedParameterJdbcTemplate template;

  private static final RowMapper<Category> CAT_ROW_MAPPER = (rs, i) -> {
    Category category = new Category();
    category.setId(rs.getInt("id"));
    category.setName(rs.getString("name"));
    category.setNameAll(rs.getString("name_all"));
    category.setDepth(rs.getInt("depth"));
    return category;
  };

  private static final RowMapper<String> ORI_ROW_MAPPER = (rs, i) -> {
    return rs.getString("category_name");
  };

  private static final RowMapper<Brand> BRAND_ROW_MAPPER = (rs, i) ->{
    Brand brand = new Brand();
    brand.setId(rs.getInt("id"));
    brand.setName(rs.getString("name"));
    return brand;
  };


  private SimpleJdbcInsert insert;

  @PostConstruct
  public void init() {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations());
    SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("category");
    insert = withTableName.usingGeneratedKeyColumns("id");
  }

  public List<String> findAll() {
    String sql = "SELECT category_name FROM original ORDER BY id";
    SqlParameterSource param = new MapSqlParameterSource();
    List<String> catList = template.query(sql, param, ORI_ROW_MAPPER);
    return catList;
  }

  public List<Category> findAllCategory() {
    String sql = "SELECT * FROM category ORDER BY id";
    SqlParameterSource param = new MapSqlParameterSource();
    List<Category> catList = template.query(sql, param, CAT_ROW_MAPPER);
    return catList;
  }

  public Integer save(Category category) {
    SqlParameterSource param = new BeanPropertySqlParameterSource(category);
    Number key = insert.executeAndReturnKey(param);
    return key.intValue();
  }

  public void saveRelation(Relation relation) {
    String sql = "INSERT INTO relations(ancestor_id, descendant_id, depth) VALUES (:ancestorId, :descendantId, :depth);";
    SqlParameterSource param = new MapSqlParameterSource().addValue("ancestorId", relation.getAncestorId())
        .addValue("descendantId", relation.getDescendantId()).addValue("depth", relation.getDepth());
    template.update(sql, param);
  }

  public void saveBrand(String brandName){
    String sql="INSERT INTO brands(name) VALUES(:brandname)";
    SqlParameterSource param = new MapSqlParameterSource().addValue("brandname", brandName);
    template.update(sql, param);
  }

  public List<String> findAllBrandName(){
    String sql="SELECT * FROM items ORDER BY id";
    List<Brand> brandList=template.query(sql,BRAND_ROW_MAPPER);
    List<String> brandNameList=brandList.stream().map(brand->brand.getName()).collect(Collectors.toList());
    return brandNameList;
  }

  public List<Brand> findAllBrand(){
    String sql="SELECT * FROM brands ORDER BY id";
    List<Brand> brandList=template.query(sql,BRAND_ROW_MAPPER);
    return brandList;
  }



  // 仕様書のテーブルを作成するメソッド
  public void category() {
    //// 整形
    List<String> nameAllList = findAll();
    List<String> parentNameList;

    // keyを親カテゴリ名、valueを親カテゴリ名とするマップを作成
    Map<String, String> parentMap = new TreeMap<>();
    // keyを親カテゴリ名、valueを子カテゴリ名リストとするマップを作成
    Map<String, List<String>> childListMap = new TreeMap<>();

    Map<String, String> nameAllMap = new TreeMap<>();
    for (String nameAll : nameAllList) {
      if (Objects.nonNull(nameAll)) {
        String parentName = Arrays.asList(nameAll.split("/")).get(0);
        nameAllMap.put(
            Arrays.asList(nameAll.split("/")).get(0) + "/" + Arrays.asList(nameAll.split("/")).get(1) + "/"
                + Arrays.asList(nameAll.split("/")).get(2),
            Arrays.asList(nameAll.split("/")).get(0) + "/" + Arrays.asList(nameAll.split("/")).get(1) + "/"
                + Arrays.asList(nameAll.split("/")).get(2));
        parentMap.put(parentName, parentName);

        String childName = Arrays.asList(nameAll.split("/")).get(1);
        List<String> childList = childListMap.get(parentName);
        if (Objects.isNull(childList)) {
          childList = new ArrayList<>();
          childList.add(childName);
          childListMap.put(parentName, childList);
        } else if (!childList.contains(childName)) {
          childList.add(childName);
        }

      }
    }
    nameAllList = new ArrayList<>(nameAllMap.values());
    parentNameList = new ArrayList<>(parentMap.values());

    for (String parentName : parentNameList) {
      // 親カテゴリのinsert
      Category category = new Category();
      category.setName(parentName);
      category.setNameAll(parentName);
      category.setDepth(new Integer(1));
      Integer parentId = save(category);

      for (String childName : childListMap.get(parentName)) {
        // 子カテゴリのinsert
        category = new Category();
        category.setName(childName);
        category.setNameAll(parentName + "/" + childName);
        category.setDepth(new Integer(2));
        Integer childId = save(category);

        for (String nameAll : nameAllList) {
          // 孫カテゴリのinsert
          if (Objects.nonNull(nameAll) && Arrays.asList(nameAll.split("/")).get(0).equals(parentName)
              && Arrays.asList(nameAll.split("/")).get(1).equals(childName)) {
            category = new Category();
            category.setName(Arrays.asList(nameAll.split("/")).get(2));
            category.setNameAll(nameAll);
            category.setDepth(new Integer(3));
            save(category);

          }
        }
      }
    }
    System.out.println("OKOK");
  }

  public void relation() {
    List<Category> categoryList = findAllCategory();
    List<Category> parentList = categoryList.stream().filter(category -> category.getDepth() == 1)
        .collect(Collectors.toList());
    List<Category> childList = categoryList.stream().filter(category -> category.getDepth() == 2)
        .collect(Collectors.toList());
    List<Category> grandChildList = categoryList.stream().filter(category -> category.getDepth() == 3)
        .collect(Collectors.toList());

    for (Category parent : parentList) {
      // 親親
      Relation relation = new Relation();
      relation.setAncestorId(parent.getId());
      relation.setDescendantId(parent.getId());
      relation.setDepth(parent.getDepth());
      saveRelation(relation);
      for (Category child : childList) {
        String parentName = Arrays.asList(child.getNameAll().split("/")).get(0);
        if (parentName.equals(parent.getName())) {
          // 親子
          relation = new Relation();
          relation.setAncestorId(parent.getId());
          relation.setDescendantId(child.getId());
          relation.setDepth(child.getDepth());
          saveRelation(relation);
          for (Category grandChild : grandChildList) {
            parentName = Arrays.asList(grandChild.getNameAll().split("/")).get(0);
            String childName = Arrays.asList(grandChild.getNameAll().split("/")).get(1);
            if (parentName.equals(parent.getName()) && childName.equals(child.getName())) {
              // 親孫
              relation = new Relation();
              relation.setAncestorId(parent.getId());
              relation.setDescendantId(grandChild.getId());
              relation.setDepth(grandChild.getDepth());
              saveRelation(relation);
            }
          }
        }
      }
      for (Category child : childList) {
        String parentName = Arrays.asList(child.getNameAll().split("/")).get(0);
        if (parentName.equals(parent.getName())) {
          // 子子
          relation = new Relation();
          relation.setAncestorId(child.getId());
          relation.setDescendantId(child.getId());
          relation.setDepth(child.getDepth());
          saveRelation(relation);
          for (Category grandChild : grandChildList) {
            parentName = Arrays.asList(grandChild.getNameAll().split("/")).get(0);
            String childName = Arrays.asList(grandChild.getNameAll().split("/")).get(1);
            if (parentName.equals(parent.getName()) && childName.equals(child.getName())) {
              // 子孫
              relation = new Relation();
              relation.setAncestorId(child.getId());
              relation.setDescendantId(grandChild.getId());
              relation.setDepth(grandChild.getDepth());
              saveRelation(relation);
            }
          }
          for (Category grandChild : grandChildList) {
            parentName = Arrays.asList(grandChild.getNameAll().split("/")).get(0);
            String childName = Arrays.asList(grandChild.getNameAll().split("/")).get(1);
            if (parentName.equals(parent.getName()) && childName.equals(child.getName())) {
              // 孫孫
              relation = new Relation();
              relation.setAncestorId(grandChild.getId());
              relation.setDescendantId(grandChild.getId());
              relation.setDepth(grandChild.getDepth());
              saveRelation(relation);
            }
          }
        }

      }
    }
    System.out.println("OKOK");
  }

  // brandのマスタ化をするためのメソッド
  public void brand(){
    List<String> brandList =findAllBrandName();
    Map<String,String> brandMap=new TreeMap<String,String>();
    for(String brand:brandList){
      if(Objects.isNull(brand)){
        continue;
      }
      brandMap.put(brand,brand);
    }
    for(String brandName:brandMap.values()){
      saveBrand(brandName);
    }
    System.out.println("OKOK");
  }


  // // 仕様書通りのテーブルを作成するメソッド
  // public void category() {
  // //// 整形
  // List<String> nameAllList = findAll();
  // List<String> parentNameList;

  // // keyを親カテゴリ名、valueを親カテゴリ名とするマップを作成
  // Map<String, String> parentMap = new TreeMap<>();
  // // keyを親カテゴリ名、valueを子カテゴリ名リストとするマップを作成
  // Map<String, List<String>> childListMap = new TreeMap<>();

  // Map<String, String> nameAllMap = new TreeMap<>();
  // for (String nameAll : nameAllList) {
  // if (Objects.nonNull(nameAll)) {
  // String parentName = Arrays.asList(nameAll.split("/")).get(0);
  // nameAllMap.put(nameAll, nameAll);
  // parentMap.put(parentName, parentName);

  // String childName = Arrays.asList(nameAll.split("/")).get(1);
  // List<String> childList = childListMap.get(parentName);
  // if (Objects.isNull(childList)) {
  // childList = new ArrayList<>();
  // childList.add(childName);
  // childListMap.put(parentName, childList);
  // } else if (!childList.contains(childName)) {
  // childList.add(childName);
  // }

  // }
  // }
  // nameAllList = new ArrayList<>(nameAllMap.values());
  // parentNameList = new ArrayList<>(parentMap.values());

  // for (String parentName : parentNameList) {
  // // 親カテゴリのinsert
  // Category category = new Category();
  // category.setName(parentName);
  // Integer parentId = save(category);

  // for (String childName : childListMap.get(parentName)) {
  // // 子カテゴリのinsert
  // category = new Category();
  // category.setParent(parentId);
  // category.setName(childName);
  // Integer childId = save(category);

  // for (String nameAll : nameAllList) {
  // // 孫カテゴリのinsert
  // if (Objects.nonNull(nameAll) &&
  // Arrays.asList(nameAll.split("/")).get(0).equals(parentName)
  // && Arrays.asList(nameAll.split("/")).get(1).equals(childName)) {
  // category = new Category();
  // category.setParent(childId);
  // category.setName(Arrays.asList(nameAll.split("/")).get(2));
  // category.setNameAll(nameAll);
  // save(category);
  // }
  // }
  // }
  // }
  // System.out.println("OKOK");
  // }

}