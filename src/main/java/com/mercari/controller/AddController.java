package com.mercari.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.mercari.domain.Brand;
import com.mercari.domain.Category;
import com.mercari.domain.Item;
import com.mercari.form.ItemAddForm;
import com.mercari.service.BrandService;
import com.mercari.service.CategoryService;
import com.mercari.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequestMapping("/item-add")
public class AddController {
    
    @ModelAttribute
    public ItemAddForm setUpForm(){
        return new ItemAddForm();
    }

    @Autowired
    private ItemService itemService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("")
    public String addPage(Model model){
        // 親categoryのselectMap
        List<Category> parentCategoryList=categoryService.findDepthCategoryByDepth(1);
        Map<Integer,String> parentMap=parentCategoryList.stream().collect(Collectors.toMap(category->category.getId(),category->category.getName(),(e1,e2) ->e1,TreeMap::new));
        model.addAttribute("parentMap", parentMap);
        return "add";
    }

    // 商品を追加する
    // categoryはgrandChildCategoryを取得しitemに格納
    // brandはまずテーブルにブランドがあるか検索する、なければ新しいブランドとしてinsertする処理がいる
    @RequestMapping("/add")
    public String add(@Validated ItemAddForm form,BindingResult result, Model model) {
        if(result.hasErrors()){
            return addPage(model);
        }
        Item item=new Item();
        Brand brand=brandService.findByBrandName(form.getBrand());
        // 新しいブランドとしてinsertし、自動裁判された番号を取得する
        if(Objects.isNull(brand)){
            Integer brandId=brandService.insert(form.getBrand());
            item.setBrand(brandId);
        }else{
            item.setBrand(brand.getId());
        }
        item.setId(itemService.insertId());
        item.setName(form.getName());
        item.setPrice(Integer.parseInt(form.getPrice()));
        item.setCategory(Integer.parseInt(form.getGrandChildCategory()));
        item.setCondition(Integer.parseInt(form.getCondition()));
        item.setShipping(0);
        item.setDescription(form.getDescription());

        itemService.insert(item);

        return "forward:/";
    }
}