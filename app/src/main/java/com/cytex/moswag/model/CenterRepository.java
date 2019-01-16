/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.model;

import com.cytex.moswag.model.entities.Product;
import com.cytex.moswag.model.entities.ProductCategoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CenterRepository {

    private static CenterRepository centerRepository;

    private ArrayList<ProductCategoryModel> listOfCategory = new ArrayList<ProductCategoryModel>();
    private ConcurrentHashMap<String, ArrayList<Product>> mapOfProductsInCategory = new ConcurrentHashMap<String, ArrayList<Product>>();
    private List<Product> listOfProductsInShoppingList = Collections.synchronizedList(new ArrayList<Product>());
    private List<Set<String>> listOfItemSetsForDataMining = new ArrayList<>();

    public static CenterRepository getCenterRepository() {

        if (null == centerRepository) {
            centerRepository = new CenterRepository();
        }
        return centerRepository;
    }


    public List<Product> getListOfProductsInShoppingList() {
        return listOfProductsInShoppingList;
    }

    public void setListOfProductsInShoppingList(ArrayList<Product> getShoppingList) {
        this.listOfProductsInShoppingList = getShoppingList;
    }

    public Map<String, ArrayList<Product>> getMapOfProductsInCategory() {

        return mapOfProductsInCategory;
    }

    public void setMapOfProductsInCategory(ConcurrentHashMap<String, ArrayList<Product>> mapOfProductsInCategory) {
        this.mapOfProductsInCategory = mapOfProductsInCategory;
    }

    public ArrayList<ProductCategoryModel> getListOfCategory() {

        return listOfCategory;
    }

    public void setListOfCategory(ArrayList<ProductCategoryModel> listOfCategory) {
        this.listOfCategory = listOfCategory;
    }

    public List<Set<String>> getItemSetList() {

        return listOfItemSetsForDataMining;
    }

    public void addToItemSetList(HashSet list) {
        listOfItemSetsForDataMining.add(list);
    }

}
