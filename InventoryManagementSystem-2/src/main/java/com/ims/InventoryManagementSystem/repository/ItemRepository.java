package com.ims.InventoryManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ims.InventoryManagementSystem.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{


}
