package com.mercari.service;

import com.mercari.domain.Brand;
import com.mercari.repository.BrandRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;
    
    public Brand findBrandName(Integer brandId){
        Brand brand=brandRepository.findById(brandId);
        return brand;
    }

    public Brand findByBrandName(String brandName){
        Brand brandId=brandRepository.findByBrandName(brandName);
        return brandId;
    }

    public Integer insert(String brandName){
        Integer brandId=brandRepository.insert(brandName);
        return brandId;
    }
}