package com.mercari.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.mercari.domain.Brand;
import com.mercari.domain.Category;
import com.mercari.domain.Item;
import com.mercari.domain.Original;
import com.mercari.repository.ItemRepository;
import com.mercari.repository.MercariRepository;
import com.mercari.repository.OriginalRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MercariService {

  @Autowired
  private MercariRepository marcariRepository;
  @Autowired
  private OriginalRepository originalRepository;
  @Autowired
  private ItemRepository itemRepository;


  public void createCategory(Model model) {
    marcariRepository.category();
  }

  public void createRelation(Model model) {
    marcariRepository.relation();
  }

  public void createBrand(Model model){
    marcariRepository.brand();
  }

  public void createItem(){
    List<Original> originalList = originalRepository.findAll();
    List<Category> categoryList = marcariRepository.findAllCategory();
    
    Map<String, Integer> pathMap = new TreeMap<>();
    for(Category category : categoryList){
      String path = category.getNameAll();
      Integer id = category.getId();
      pathMap.put(path, id);
    }

    for(Original original : originalList){
      Item item = new Item();
      BeanUtils.copyProperties(original, item);
      try {
        item.setCategory(pathMap.get(original.getCategoryName()));
      } catch (NullPointerException e) {
        item.setCategory(null);
      }
      itemRepository.save(item);
    }
  }

  public void updateItemBrand(){
    List<Item> itemOriginalList=itemRepository.findAll();
    List<Brand> brandList=marcariRepository.findAllBrand();

    for(Item item:itemOriginalList){
      for(Brand brand:brandList){
        if(Objects.isNull(item.getBrandName())){
          continue;
        }
        if(Objects.equals(item.getBrandName(),brand.getName())){
          item.setBrand(brand.getId());
        }
      }
    }
    for(Item originalItem : itemOriginalList){
      Item item = new Item();
      BeanUtils.copyProperties(originalItem, item);
      itemRepository.save(item);
    }
  }
}