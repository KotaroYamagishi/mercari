package com.mercari.repository;

import java.util.List;

import com.mercari.domain.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Category> CAT_ROW_MAPPER = (rs, i) -> {
        Category category=new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setNameAll(rs.getString("name_all"));
        category.setDepth(rs.getInt("depth"));
        return category;
    };

    
    /** 
     * depthに該当するcategoryを全件取得するメソッド
     * 
     * @param depth
     * @return List<Category>
     */
    public List<Category> findDepthCategoryByDepth(Integer depth){
        String sql="select * from category where depth=:depth ORDER BY depth";
        SqlParameterSource param = new MapSqlParameterSource().addValue("depth", depth);
        List<Category> depthCategoryList=template.query(sql, param,CAT_ROW_MAPPER);
        return depthCategoryList;
    }

    
    /** 
     * descendantIdを用いて、そのdescendantIdの親となる階層のカテゴリーの情報を取得するメソッド
     * 
     * @param descendantId
     * @return List<Category>
     */
    public List<Category> findCategoryByDescendantId(Integer descendantId){
        String sql="select * from category where id in(select ancestor_id from relations where descendant_id=:descendantId) ORDER BY depth";
        SqlParameterSource param = new MapSqlParameterSource().addValue("descendantId",descendantId);
        List<Category> categoryList= template.query(sql, param,CAT_ROW_MAPPER);

        return categoryList;
    }

    
    /** 
     * ancestorIdとdepthを用いて、そのancestorIdの子でかつdepthで指定した階層のカテゴリーの情報を取得するメソッド
     * 
     * @param id
     * @param depth
     * @return List<Category>
     */
    public List<Category> findDepthCategoryByIdAndDepth(Integer ancestorId,Integer depth){
        String sql="select * from category where id in(select descendant_id from relations where ancestor_id=:id AND depth=:depth) ORDER BY depth";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id",ancestorId).addValue("depth", depth);
        List<Category> depthCategoryList=template.query(sql,param,CAT_ROW_MAPPER);
        return depthCategoryList;
    }
}