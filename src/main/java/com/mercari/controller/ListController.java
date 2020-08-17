package com.mercari.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.mercari.domain.Brand;
import com.mercari.domain.Category;
import com.mercari.domain.Item;
import com.mercari.form.ItemSearchForm;
import com.mercari.form.PageForm;
import com.mercari.service.BrandService;
import com.mercari.service.CategoryService;
import com.mercari.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ListController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @ModelAttribute
    public PageForm setUpPageForm() {
        return new PageForm();
    }

    @ModelAttribute
    public ItemSearchForm setUpSearchForm() {
        return new ItemSearchForm();
    }

    @RequestMapping("")
    public String index(Model model, ItemSearchForm form, String page) {
        // pagingの処理
        Integer pageNum = null;
        if (Objects.isNull(page)) {
            page = "1";
        }
        pageNum = (Integer.parseInt(page) - 1) * 30;
        String name = form.getName();
        String categoryId = form.getGrandChildCategory();
        String brandName = form.getBrand();
        if (Objects.equals(name, "")) {
            name = null;
        }
        if (Objects.equals(categoryId, "") || Objects.equals(categoryId, "0")) {
            categoryId = null;
        }
        if (Objects.equals(brandName, "")) {
            brandName = null;
        }
        // 検索から来た場合、先にtotalPageが入っている可能性がある
        if (!(model.containsAttribute("totalPage"))) {
            Integer totalPage = itemService.totalPage(name, categoryId, brandName);
            model.addAttribute("totalPage", totalPage);
        }

        model.addAttribute("name", name);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("brandName", brandName);
        List<Item> itemList = itemService.showAll(name, categoryId, brandName, pageNum);
        itemList.forEach(item -> {
            List<Category> categoryList = categoryService.findCategoryByDescendantId(item.getCategory());
            item.setCategoryList(categoryList);
            // brandのリストを作成する
            if (!(Objects.equals(item.getBrand(), 0))) {
                Brand brand = brandService.findBrandName(item.getBrand());
                item.setBrandName(brand.getName());
            }
        });
        model.addAttribute("itemList", itemList);

        // 検索機能を表示するための処理
        List<Category> parentCategoryList = categoryService.findDepthCategoryByDepth(1);
        Map<Integer, String> parentMap = parentCategoryList.stream().collect(Collectors
                .toMap(category -> category.getId(), category -> category.getName(), (e1, e2) -> e1, TreeMap::new));
        parentMap.put(0, "---");
        model.addAttribute("parentMap", parentMap);

        model.addAttribute("page", page);

        return "list";
    }

    @RequestMapping("/paging")
    public String paging(Model model, String page, String num, String name, String categoryId, String brandName) {
        try {
            ItemSearchForm form = new ItemSearchForm();
            form.setName(name);
            form.setGrandChildCategory(categoryId);
            form.setBrand(brandName);
            Integer pageNum = Integer.parseInt(page) + Integer.parseInt(num);
            return index(model, form, String.valueOf(pageNum));
        } catch (Exception e) {
            ItemSearchForm form = new ItemSearchForm();
            form.setName(name);
            form.setGrandChildCategory(categoryId);
            form.setBrand(brandName);
            Integer pageNum = Integer.parseInt(page) + Integer.parseInt(num);
            return index(model, form, String.valueOf(pageNum));
        }
    }

    @RequestMapping("/paging-jump")
    public String pagingJump(Model model, PageForm form) {
        ItemSearchForm searchForm = new ItemSearchForm();
        String name = form.getName();
        String categoryId = form.getCategoryId();
        String brandName = form.getBrandName();
        searchForm.setName(name);
        searchForm.setGrandChildCategory(categoryId);
        searchForm.setBrand(brandName);
        try {
            if (Objects.equals(name, "")) {
                name = null;
            }
            if (Objects.equals(categoryId, "") || Objects.equals(categoryId, "0")) {
                categoryId = null;
            }
            if (Objects.equals(brandName, "")) {
                brandName = null;
            }
            Integer totalPage = itemService.totalPage(name, categoryId, brandName);
            Integer pageNum = Integer.parseInt(form.getPage());
            // 可能であればjavascriptでvalidationしたい
            if (pageNum > totalPage) {
                pageNum = 1;
            }
            return index(model, searchForm, String.valueOf(pageNum));
        } catch (Exception e) {
            if (Objects.equals(name, "")) {
                name = null;
            }
            if (Objects.equals(categoryId, "") || Objects.equals(categoryId, "0")) {
                categoryId = null;
            }
            if (Objects.equals(brandName, "")) {
                brandName = null;
            }
            Integer totalPage = itemService.totalPage(name, categoryId, brandName);
            Integer pageNum = Integer.parseInt(form.getPage());
            // 可能であればjavascriptでvalidationしたい
            if (pageNum > totalPage) {
                pageNum = 1;
            }
            return index(model, searchForm, String.valueOf(pageNum));
        }
    }

    @RequestMapping("/search")
    public String searchCategory(Model model, String name, String categoryId, String brandName) {
        ItemSearchForm form = new ItemSearchForm();
        form.setName(name);
        form.setGrandChildCategory(categoryId);
        form.setBrand(brandName);
        return index(model, form, null);
    }
}