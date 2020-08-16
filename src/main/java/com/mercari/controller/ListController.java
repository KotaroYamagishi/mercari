package com.mercari.controller;

import java.util.ArrayList;
import java.util.List;

import com.mercari.domain.Category;
import com.mercari.domain.Item;
import com.mercari.service.CategoryService;
import com.mercari.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ListController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    
    @RequestMapping("")
    public String index(Model model){
        List<Item> itemList =itemService.showAll();
        itemList.forEach(item->{
            List<Category> categoryList=categoryService.findCategoryByDescendantId(item.getCategory());
            List<String> categoryNameList=new ArrayList<>();
            categoryList.forEach(category->{
                categoryNameList.add(category.getName());
            });
            item.setCategoryNameList(categoryNameList);
        });

        model.addAttribute("itemList", itemList);

        return "list";
    }
}