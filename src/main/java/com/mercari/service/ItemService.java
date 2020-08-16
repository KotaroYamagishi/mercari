package com.mercari.service;

import java.util.List;

import com.mercari.domain.Brand;
import com.mercari.domain.Item;
import com.mercari.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;

    
    /** 
     * @return List<Item>
     */
    public List<Item> showAll(){
        List<Item> itemList=itemRepository.findAll();
        return itemList;
    }

    
    /** 
     * @param id
     * @return Item
     */
    public Item load(Integer id){
        Item item=itemRepository.load(id);
        return item;
    }

    
    /** 
     * itemを編集するためのメソッド
     * categoryにdescendantIdをセットしておく必要あり
     * 
     * @param item
     */
    public void edit(Item item){
        itemRepository.update(item);
    }

    public void insert(Item item){
        itemRepository.save(item);
    }

    public Integer insertId(){
        return itemRepository.insertId();
    }

}