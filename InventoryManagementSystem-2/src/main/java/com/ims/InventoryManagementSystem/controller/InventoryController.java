package com.ims.InventoryManagementSystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ims.InventoryManagementSystem.entity.Item;
import com.ims.InventoryManagementSystem.repository.ItemRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class InventoryController {
	 
	 private final ItemRepository itemRepository;

	    public InventoryController(ItemRepository itemRepository) {
	        this.itemRepository = itemRepository;
	    }

	   
	    @GetMapping("/login")
	    public String loginPage() {
	        return "login.html";
	    }

	   
	    @PostMapping("/login")
	    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
	        if ("admin".equals(username) && "admin123".equals(password)) {
	            session.setAttribute("user", "admin");
	            return "redirect:/items";
	        }
	        return "redirect:/login?error=true";
	    }

	    
	    @GetMapping("/logout")
	    public String logout(HttpSession session) {
	        session.invalidate();
	        return "redirect:/login";
	    }

	    
	    @GetMapping("/items")
	    public String itemsPage(HttpSession session) {
	        if (session.getAttribute("user") == null) {
	            return "redirect:/login";
	        }
	        return "items.html";
	    }

	    
	    private boolean isLoggedIn(HttpSession session) {
	        return session.getAttribute("user") != null;
	    }

	    
	    @GetMapping("/api/items")
	    @ResponseBody
	    public ResponseEntity<List<Item>> getAllItems(HttpSession session) {
	        if (!isLoggedIn(session)) {
	            return ResponseEntity.status(401).build();
	        }
	        return ResponseEntity.ok(itemRepository.findAll());
	    }

	    
	    @GetMapping("/api/items/{id}")
	    @ResponseBody
	    public ResponseEntity<Item> getItemById(@PathVariable Long id, HttpSession session) {
	        if (!isLoggedIn(session)) {
	            return ResponseEntity.status(401).build();
	        }
	        Optional<Item> item = itemRepository.findById(id);
	        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	    
	    @PostMapping("/api/items")
	    @ResponseBody
	    public ResponseEntity<Item> addItem(@RequestBody Item item, HttpSession session) {
	        if (!isLoggedIn(session)) {
	            return ResponseEntity.status(401).build();
	        }
	        Item saved = itemRepository.save(item);
	        return ResponseEntity.ok(saved);
	    }

	    
	    @PutMapping("/api/items/{id}")
	    @ResponseBody
	    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item, HttpSession session) {
	        if (!isLoggedIn(session)) {
	            return ResponseEntity.status(401).build();
	        }
	        Optional<Item> existing = itemRepository.findById(id);
	        if (existing.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }
	        Item e = existing.get();
	        e.setName(item.getName());
	        e.setQuantity(item.getQuantity());
	        e.setPrice(item.getPrice());
	        e.setCategory(item.getCategory());
	        itemRepository.save(e);
	        return ResponseEntity.ok(e);
	    }

	   
	    @DeleteMapping("/api/items/{id}")
	    @ResponseBody
	    public ResponseEntity<Void> deleteItem(@PathVariable Long id, HttpSession session) {
	        if (!isLoggedIn(session)) {
	            return ResponseEntity.status(401).build();
	        }
	        if (!itemRepository.existsById(id)) {
	            return ResponseEntity.notFound().build();
	        }
	        itemRepository.deleteById(id);
	        return ResponseEntity.ok().build();
	    }
	}