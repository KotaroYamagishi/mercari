package com.mercari.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mercari.domain.Category;
import com.mercari.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ajaxe")
public class RestItemdetailController {
    
    @Autowired
    private CategoryService categoryService;

    
    @RequestMapping("/edit-child-first")
    public String editChild(ModelMap modelMap,Model model,String parentCategory,String childCategory,String depth){
        Integer ancestorId=Integer.parseInt(parentCategory);
        Integer depthId=Integer.parseInt(depth);
        List<Category> categoryList=categoryService.findDepthCategoryByIdAndDepth(ancestorId, depthId);
        Map<Integer, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName(), (e1, e2) -> e1, LinkedHashMap::new));
        modelMap.addAttribute("selectAjaxCategoryMap",categoryMap);
        model.addAttribute("childId",Integer.parseInt(childCategory));
        // if(Objects.equals(depth, "2")){
        //     return "edit::selectAjaxChild";
        // }else if(Objects.equals(depth, "3")){
        //     return "edit::selectAjaxGrandChild";
        // }
        return "edit::selectAjaxChild";
    }
    @RequestMapping("/edit-child")
    public String editChild2(ModelMap modelMap,Model model,String parentCategory,String depth){
        Integer ancestorId=Integer.parseInt(parentCategory);
        Integer depthId=Integer.parseInt(depth);
        List<Category> categoryList=categoryService.findDepthCategoryByIdAndDepth(ancestorId, depthId);
        Map<Integer, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName(), (e1, e2) -> e1, LinkedHashMap::new));
        modelMap.addAttribute("selectAjaxCategoryMap",categoryMap);
        // if(Objects.equals(depth, "2")){
        //     return "edit::selectAjaxChild";
        // }else if(Objects.equals(depth, "3")){
        //     return "edit::selectAjaxGrandChild";
        // }
        return "edit::selectAjaxChild";
    }

    @RequestMapping("/edit-grandchild-first")
    public String editGrandChildFirst(ModelMap modelMap,Model model,String childCategory,String grandChildCategory,String depth){
        Integer ancestorId=Integer.parseInt(childCategory);
        Integer depthId=Integer.parseInt(depth);
        List<Category> categoryList=categoryService.findDepthCategoryByIdAndDepth(ancestorId, depthId);
        Map<Integer, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName(), (e1, e2) -> e1, LinkedHashMap::new));
        modelMap.addAttribute("selectAjaxCategoryMap",categoryMap);
        model.addAttribute("grandChildId",Integer.parseInt(grandChildCategory));
        return "edit::selectAjaxGrandChild";
    }
    @RequestMapping("/edit-grandchild")
    public String editGrandChild(ModelMap modelMap,Model model,String childCategory,String depth){
        Integer ancestorId=Integer.parseInt(childCategory);
        Integer depthId=Integer.parseInt(depth);
        List<Category> categoryList=categoryService.findDepthCategoryByIdAndDepth(ancestorId, depthId);
        Map<Integer, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName(), (e1, e2) -> e1, LinkedHashMap::new));
        modelMap.addAttribute("selectAjaxCategoryMap",categoryMap);
        return "edit::selectAjaxGrandChild";
    }
}