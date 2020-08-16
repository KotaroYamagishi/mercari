package com.mercari.controller;

import com.mercari.service.MercariService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ins")
public class MercariController {
  @Autowired
  MercariService mercariService;

  // @RequestMapping("/create-category")
  // public String createCategory(Model model) {
  //   mercariService.createCategory(model);
  //   return "index";
  // }

  // @RequestMapping("/create-relation")
  // public String createRelation(Model model) {
  //   mercariService.createRelation(model);
  //   return "index";
  // }

  // @RequestMapping("/create-items")
  // public String createItems() {
  //   mercariService.createItem();
  //   return "index";
  // }

  // @RequestMapping("/create-brands")
  // public String createBrands(Model model) {
  //   mercariService.createBrand(model);
  //   return "index";
  // }

  // @RequestMapping("/update-itembrand")
  // public String updateItemBrand(){
  //   mercariService.updateItemBrand();
  //   return "index";
  // }
}