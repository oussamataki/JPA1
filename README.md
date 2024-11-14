# JPA1 (Products) Project

## Créateur
TAKI Oussama

## Introduction
Ce projet démontre l'utilisation de Spring Boot avec JPA pour la gestion de produits dans une base de données H2 et MySQL.

## Prérequis
- IntelliJ IDEA Ultimate
- Java Development Kit (JDK)
- Maven

## Configuration du Projet

1. **Installer IntelliJ Ultimate**
    - Téléchargez et installez IntelliJ IDEA Ultimate depuis le [site officiel de JetBrains](https://www.jetbrains.com/idea/download/).

2. **Créer un projet Spring Initializer**
    - Utilisez Spring Initializer pour créer un nouveau projet avec les dépendances suivantes :
        - Spring Data JPA
        - H2 Database
        - Spring Web
        - Lombok

3. **Configurer l'unité de persistance**
    - Ajoutez les configurations suivantes dans `src/main/resources/application.properties` :

   ```properties
   spring.application.name=JPA1
   server.port=8885
   spring.datasource.url=jdbc:h2:mem:productdb
   spring.h2.console.enabled=true
   ```

## Structure du Projet

```
src/
├── main/
│   ├── java/
│   │   └── org/
│   │       └── example/
│   │           └── jpa1/
│   │               ├── entities/
│   │               │   └── Product.java
│   │               ├── repositories/
│   │               │   └── ProductRepository.java
│   │               └── Jpa1Application.java
│   └── resources/
│       └── application.properties
```

## Implémentation

1. **Créer l'entité JPA Product**

```java
package org.example.jpa1.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private int quantity;
}
```

2. **Créer l'interface JPA Repository**

```java
package org.example.jpa1.repositories;

import org.example.jpa1.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
   List<Product> findByName(String name);
   List<Product> findByNameAndPrice(String name, Double price);
}
```

## Test des Opérations

Dans la classe principale `Jpa1Application.java`, implémentez la méthode `run()` pour tester les opérations CRUD :

```java
package org.example.jpa1;

import org.example.jpa1.entities.Product;
import org.example.jpa1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Jpa1Application implements CommandLineRunner {
    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(Jpa1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Ajouter des produits
        productRepository.save(new Product(null, "TV", 1000.0, 10));
        productRepository.save(new Product(null, "Mouse", 50.0, 100));
        productRepository.save(new Product(null, "PC", 3000.0, 20));

        // Afficher tous les produits
        System.out.println("Liste de tous les produits :");
        List<Product> products = productRepository.findAll();
        products.forEach(System.out::println);

        // Afficher un produit par id
        System.out.println("\nAffichage d'un produit par ID :");
        Product p = productRepository.findById(1L).orElseThrow();
        System.out.printf("ID: %d, Nom: %s, Prix: %.2f, Quantité: %d%n", 
                          p.getId(), p.getName(), p.getPrice(), p.getQuantity());

       //chercher des produits par nom
       List<Product> products1 = productRepository.findByName("Mouse");
       products1.forEach(System.out::println);
       
       //chercher des produits par nom et prix
       List<Product> products3 = productRepository.findByNameAndPrice("PC", 3000.0);
       products3.forEach(System.out::println);

       //chercher par mot clé
       List<Product> products4 = productRepository.findByName("M%");
       products4.forEach(System.out::println);

       //chercher par prix supérieur à
       List<Product> products5 = productRepository.findByPriceGreaterThan(1000.0);
       products5.forEach(System.out::println);

        // Mettre à jour un produit
        System.out.println("\nMise à jour d'un produit :");
        Product p2 = productRepository.findById(1L).orElseThrow();
        p2.setPrice(2000.0);
        productRepository.save(p2);
        System.out.printf("Produit modifié : ID: %d, Nom: %s, Nouveau prix: %.2f, Quantité: %d%n", 
                          p2.getId(), p2.getName(), p2.getPrice(), p2.getQuantity());

        // Supprimer un produit
        System.out.println("\nSuppression d'un produit :");
        productRepository.deleteById(2L);
        System.out.println("Produit avec id=2 supprimé");

        // Afficher tous les produits après modifications
        System.out.println("\nListe mise à jour de tous les produits :");
        List<Product> updatedProducts = productRepository.findAll();
        updatedProducts.forEach(System.out::println);
    }
}
```
##Migration vers MySQL

1. **Ajouter la dépendance MySQL**
    - Ajoutez la dépendance MySQL dans le fichier `pom.xml` :

   ```xml
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```
2. **Configurer la base de données MySQL**
    - Ajoutez les configurations suivantes dans `src/main/resources/application.properties` :

   ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/productsdb?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
    ```
3. **Commenter la configuration H2**
    - Commentez la configuration H2 dans `src/main/resources/application.properties` :

   ```properties
   #spring.datasource.url=jdbc:h2:mem:productdb
   #spring.h2.console.enabled=true
   ```
   
4.**Redémarrer l'application**
   - Redémarrez l'application pour appliquer les modifications.
