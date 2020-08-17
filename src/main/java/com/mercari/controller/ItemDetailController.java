package com.mercari.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.mercari.domain.Brand;
import com.mercari.domain.Category;
import com.mercari.domain.Item;
import com.mercari.form.ItemDetailForm;
import com.mercari.service.BrandService;
import com.mercari.service.CategoryService;
import com.mercari.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item-detail")
public class ItemDetailController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @ModelAttribute
    public ItemDetailForm setUpForm() {
        return new ItemDetailForm();
    }

    /**
     * itemの詳細画面を表示するためのメソッド
     * 
     * @param id
     * @param model
     * @return String
     */
    @RequestMapping("")
    public String itemDetail(String id, Model model) {
        Item item = itemService.load(Integer.parseInt(id));
        List<Category> categoryList = categoryService.findCategoryByDescendantId(item.getCategory());
        item.setCategoryList(categoryList);
        if(!(Objects.equals(item.getBrand(),0))){
        Brand brand=brandService.findBrandName(item.getBrand());
        item.setBrandName(brand.getName());
        }
        model.addAttribute("item", item);

        return "detail";
    }

    @RequestMapping("/edit-page")
    public String detailEditPage(Model model, String id) {
        Item item = itemService.load(Integer.parseInt(id));
        List<Category> categoryList = categoryService.findCategoryByDescendantId(item.getCategory());
        item.setCategoryList(categoryList);
        if(!(Objects.equals(item.getBrand(),0))){
            Brand brand=brandService.findBrandName(item.getBrand());
            item.setBrandName(brand.getName());
            }
        model.addAttribute("item", item);
        // ajaxのselectbocに初期値を入れるため
        model.addAttribute("parentId", categoryList.get(0).getId());
        model.addAttribute("childId", categoryList.get(1).getId());
        model.addAttribute("grandChildId", categoryList.get(2).getId());
        // 親categoryのselectMap
        List<Category> parentCategoryList = categoryService.findDepthCategoryByDepth(1);
        Map<Integer, String> parentMap = parentCategoryList.stream().collect(Collectors
                .toMap(category -> category.getId(), category -> category.getName(), (e1, e2) -> e1, TreeMap::new));
        model.addAttribute("parentMap", parentMap);
        //
        Map<Integer, Integer> conditionMap = new HashMap<Integer, Integer>();
        conditionMap.put(1, 1);
        conditionMap.put(2, 2);
        conditionMap.put(3, 3);
        model.addAttribute("conditionMap", conditionMap);

        return "edit";
    }

    @RequestMapping("/edit")
    public String detailEdit(Model model, ItemDetailForm form) {
        Item item = new Item();
        item.setId(Integer.parseInt(form.getId()));
        item.setName(form.getName());
        item.setPrice(Integer.parseInt(form.getPrice()));
        item.setCategory(Integer.parseInt(form.getGrandChildCategory()));
        // 新しいブランドとしてinsertし、自動裁判された番号を取得する
        Brand brand = brandService.findByBrandName(form.getBrand());
        if(Objects.equals(form.getBrand(),"")){
            item.setBrand(null);
        }else if (Objects.isNull(brand)) {
            Integer brandId=brandService.insert(form.getBrand());
            item.setBrand(brandId);
        } else {
            item.setBrand(brand.getId());
        }
        item.setCondition(Integer.parseInt(form.getCondition()));
        item.setDescription(form.getDescription());

        itemService.edit(item);
        return itemDetail(form.getId(),model);
    }
}