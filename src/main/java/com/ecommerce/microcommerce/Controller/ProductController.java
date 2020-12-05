package com.ecommerce.microcommerce.Controller;

import com.ecommerce.microcommerce.Model.Product;
import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exceptions.ProduitIntrouvableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class ProductController {

    // je declare interface et non pas instancier, Springboot va recupere une instance ProductDAOImpl
    @Autowired
    private ProductDao productDao;

    //Produits
    @GetMapping(value = "Produits")
    public List<Product> listeProduits(){
        return productDao.findAll();
    }

    //Produits/{id}
    @GetMapping(value = "Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) throws ProduitIntrouvableException {

        Product produit = productDao.findById(id);

        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        return produit;

    }

    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product){

        Product product1 = productDao.save(product);

        if(product == null){
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "test/Produits/{prixLimit}")
    public List<Product> testDeRequete(@PathVariable int prixLimit){
        return productDao.findByPrixGreaterThan(prixLimit);
    }

}
