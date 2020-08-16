package com.mercari.service;

import java.util.List;

import com.mercari.domain.Category;
import com.mercari.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    
    /** 
     * depthに該当するcategoryを全件取得するメソッド
     * 
     * @param depth
     * @return List<Category>
     */
    public List<Category> findDepthCategoryByDepth(Integer depth){
        List<Category> categoryList = categoryRepository.findDepthCategoryByDepth(depth);
        return categoryList;
    }
    
    /** 
     * 引数のカテゴリのidから自分を含める上の階層のカテゴリの情報を取得する
     * 
     * @param descendantId
     * @return List<Category>
     */
    public List<Category> findCategoryByDescendantId(Integer descendantId){
        List<Category> categories = categoryRepository.findCategoryByDescendantId(descendantId);
        return categories;
    }

    
    /**
     * ancestor_idとdepthから、該当するdepthのカテゴリーIDとカテゴリー名を取得する
     *  
     * @param id
     * @param depth
     * @return List<Category>
     */
    public List<Category> findDepthCategoryByIdAndDepth(Integer id,Integer depth){
        List<Category> categoryList =categoryRepository.findDepthCategoryByIdAndDepth(id, depth);
        return categoryList;
    }
}
