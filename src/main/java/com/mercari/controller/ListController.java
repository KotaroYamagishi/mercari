package com.mercari.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item-list")
public class ListController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    HttpSession session;

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
        session.getAttribute("user");
        // pagingの処理
        Integer pageNum = null;
        if (Objects.isNull(page)) {
            page = "1";
        }
        pageNum = (Integer.parseInt(page) - 1) * 30;
        String name = form.getName();
        String parentCategoryId = form.getParentCategory();
        String childCategoryId = form.getChildCategory();
        String grandChildCategoryId = form.getGrandChildCategory();
        String brandName = form.getBrand();
        // 最後に残った一番末端のカテゴリーidを格納する
        String categoryId = null;
        if (Objects.equals(name, "")) {
            name = null;
        }
        // ここおかしい、値がちゃんと入ってない
        if (Objects.isNull(form.getParentCategory())) {
            model.addAttribute("parentId", "0");
        } else {
            model.addAttribute("parentId", parentCategoryId);
        }
        if (Objects.isNull(form.getChildCategory())) {
            model.addAttribute("childId", "0");
        } else {
            model.addAttribute("childId", childCategoryId);
        }
        if (Objects.isNull(form.getGrandChildCategory())) {
            model.addAttribute("grandChildId", "0");
        } else {
            model.addAttribute("grandChildId", grandChildCategoryId);
        }
        if (Objects.equals(brandName, "")) {
            brandName = null;
        }
        // categoryId が""のパターンはない
        if (Objects.equals(parentCategoryId, "0") && Objects.equals(childCategoryId, "0")
                && Objects.equals(grandChildCategoryId, "0")) {
            categoryId = null;
        } else if (Objects.equals(childCategoryId, "0") && Objects.equals(grandChildCategoryId, "0")) {
            categoryId = parentCategoryId;
        } else if (Objects.equals(grandChildCategoryId, "0")) {
            categoryId = childCategoryId;
        } else {
            categoryId = grandChildCategoryId;
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
        if (itemList.size() == 0) {
            model.addAttribute("emptyMessage", "商品が見つかりませんでした");
        }
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

        model.addAttribute("itemSearchForm", form);

        return "list";
    }

    @RequestMapping("/paging")
    public String paging(Model model, String page, String num, String name, String parentCategory, String childCategory,
            String grandChildCategory, String brandName) {
        try {
            ItemSearchForm form = new ItemSearchForm();
            form.setName(name);
            form.setParentCategory(parentCategory);
            form.setChildCategory(childCategory);
            form.setGrandChildCategory(grandChildCategory);
            form.setBrand(brandName);
            Integer pageNum = Integer.parseInt(page) + Integer.parseInt(num);
            return index(model, form, String.valueOf(pageNum));
        } catch (Exception e) {
            ItemSearchForm form = new ItemSearchForm();
            form.setName(name);
            form.setParentCategory(parentCategory);
            form.setChildCategory(childCategory);
            form.setGrandChildCategory(grandChildCategory);
            form.setBrand(brandName);
            Integer pageNum = Integer.parseInt(page) + Integer.parseInt(num);
            return index(model, form, String.valueOf(pageNum));
        }
    }

    @RequestMapping("/paging-jump")
    public String pagingJump(Model model,@Validated PageForm form,BindingResult result) {
        ItemSearchForm searchForm = new ItemSearchForm();
        String name = form.getName();
        String parentCategoryId = form.getParentCategory();
        String childCategoryId = form.getChildCategory();
        String grandChildCategoryId = form.getGrandChildCategory();
        String brandName = form.getBrandName();
        String categoryId = null;
        searchForm.setName(name);
        searchForm.setParentCategory(parentCategoryId);
        searchForm.setChildCategory(childCategoryId);
        searchForm.setGrandChildCategory(grandChildCategoryId);
        searchForm.setBrand(brandName);
        if (Objects.equals(name, "")) {
            name = null;
        }
        if (Objects.equals(brandName, "")) {
            brandName = null;
        }
        if (Objects.equals(parentCategoryId, "0") && Objects.equals(childCategoryId, "0")
                && Objects.equals(grandChildCategoryId, "0")) {
            categoryId = null;
        } else if (Objects.equals(childCategoryId, "0") && Objects.equals(grandChildCategoryId, "0")) {
            categoryId = parentCategoryId;
        } else if (Objects.equals(grandChildCategoryId, "0")) {
            categoryId = childCategoryId;
        } else {
            categoryId = grandChildCategoryId;
        }
        if(result.hasErrors()){
            return index(model, searchForm, "1");
        }
        try {
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

    // 取得したcategoryIdとdepthから、他のparentCategory,childCategory,grandChildCategoryにもあたいをいれてあげる
    @RequestMapping("/search-category")
    public String searchCategory(Model model, String name, String categoryId, String depth, String brandName) {
        ItemSearchForm form = new ItemSearchForm();
        form.setName(name);
        form.setBrand(brandName);
        if (Objects.equals(depth, "1")) {
            form.setParentCategory(categoryId);
            form.setChildCategory("0");
            form.setGrandChildCategory("0");
        } else if (Objects.equals(depth, "2")) {
            List<Category> categoryList = categoryService.findCategoryByDescendantId(Integer.parseInt(categoryId));
            form.setParentCategory(String.valueOf(categoryList.get(0).getId()));
            form.setChildCategory(categoryId);
            form.setGrandChildCategory("0");
        } else if (Objects.equals(depth, "3")) {
            List<Category> categoryList = categoryService.findCategoryByDescendantId(Integer.parseInt(categoryId));
            form.setParentCategory(String.valueOf(categoryList.get(0).getId()));
            form.setChildCategory(String.valueOf(categoryList.get(1).getId()));
            form.setGrandChildCategory(categoryId);
        }
        return index(model, form, null);
    }

    @RequestMapping("/search-brand")
    public String searchBrand(Model model, String name, String parentCategory, String childCategory,
            String grandChildCategory, String brandName) {
        ItemSearchForm form = new ItemSearchForm();
        form.setName(name);
        form.setParentCategory(parentCategory);
        form.setChildCategory(childCategory);
        form.setGrandChildCategory(grandChildCategory);
        form.setBrand(brandName);
        return index(model, form, null);
    }
}