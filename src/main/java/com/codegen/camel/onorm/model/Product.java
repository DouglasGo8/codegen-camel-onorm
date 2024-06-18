package com.codegen.camel.onorm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@Table(name = "my_product")
@NamedQueries(
        {
                @NamedQuery(name = "findAll", query = "select p from Product p"),
                @NamedQuery(name = "findById", query = "select p from Product p where p.id = :id"),
                @NamedQuery(name = "findByName", query = "select p from Product p where p.productName = :name"),
                @NamedQuery(name = "findByNameAndColor",
                        query = "select p from Product p where p.productName like concat(:name, '%') and color = :color "),
        }
)
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "NAME")
  private String productName;
  @Column(name = "PRICE")
  private double price;
  @Column(name = "WEIGHT")
  private String weight;
  @Column(name = "COLOR")
  private String color;
}
