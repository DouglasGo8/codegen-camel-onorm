package com.codegen.camel.onorm.mediation;

import com.codegen.camel.onorm.model.Product;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
@NoArgsConstructor
public class CodeGenCamelORMRoute extends RouteBuilder {

  @Override
  public void configure() {

    from("timer://codeGenTimer?period=10s&fixedRate=true")
            .log(LoggingLevel.INFO, ":: Apache Camel JPA/ORM ::")
            // --------------------------- BEGIN QUERY findAll ---------------------------------------------------------
              .to("jpa://" + Product.class.getName() + "?resultClass=" + Product.class.getName() + "&namedQuery=findAll")
              .split(body()).streaming()
                .log("HOORAY!!! ${body.productName}: ${body.price}")
              .end() // end split
              // --------------------------- END QUERY findAll -----------------------------------------------------------

              // --------------------------- BEGIN QUERY findById --------------------------------------------------------
              .process(e -> {
                var registry = e.getContext().getRegistry();
                var map = registry.lookupByNameAndType("params", Map.class);
                // ---------
                map.clear();
                // ---------
                map.put("id", 1);
              })
              .to("jpa://" + Product.class.getName() + "?resultClass=" + Product.class.getName() + "&namedQuery=findById&parameters=#params")
              .log("HOORAY!!! ${body[0].productName}: ${body[0].price}")
              // --------------------------- END QUERY findById -----------------------------------------------------------

              // --------------------------- BEGIN QUERY findByName --------------------------------------------------------
              .process(e -> {
                var registry = e.getContext().getRegistry();
                var map = registry.lookupByNameAndType("params", Map.class);
                // ---------
                map.clear();
                // ---------
                map.put("name", "Legion Go 512GB e 16GB");
              })
              .to("jpa://" + Product.class.getName() + "?resultClass=" + Product.class.getName() + "&namedQuery=findByName&parameters=#params")
              .split(body()).streaming()
                .log("HOORAY!!! ${body.productName}: ${body.price}")
              .end() // end split
              // --------------------------- END QUERY findByName -----------------------------------------------------------

              // --------------------------- BEGIN QUERY findByNameAndColor --------------------------------------------------------
              .process(e -> {
                var registry = e.getContext().getRegistry();
                var map = registry.lookupByNameAndType("params", Map.class);
                // ---------
                map.clear();
                // ---------
                map.put("name", "ASUS");
                map.put("color", "White");
              })
              .to("jpa://" + Product.class.getName() + "?resultClass=" + Product.class.getName() + "&namedQuery=findByNameAndColor&parameters=#params")
              .split(body()).streaming()
                .log("HOORAY!!! ${body.productName}: ${body.price}")
              .end() // end split
              // --------------------------- END QUERY findByNameAndColor -----------------------------------------------------------

              // --------------------------- BEGIN QUERY nativeQuery --------------------------------------------------------

              .to("jpa://" + Product.class.getName() + "?resultClass=" + Product.class.getName() + "&nativeQuery=SELECT * FROM MY_PRODUCT")
              .split(body()).streaming()
                .log("HOORAY!!! ${body.productName}: ${body.price}")
              .end() // end split
              // --------------------------- END QUERY nativeQuery -----------------------------------------------------------

            .setVariable("color", constant("White"))
            .setVariable("pname", constant("Legion Go 512GB e 16GB"))
            // EIP Capability, can execute two queries in parallel with an aggregated result
            .multicast().aggregationStrategy(AggregationStrategies.flexible(Product[].class).accumulateInCollection(ArrayList.class))
              .parallelProcessing()
              .toD("{{custom.queries.findBy}}&query=SELECT p FROM Product p WHERE p.productName='${variable.pname}'")
              .toD("{{custom.queries.findBy}}&nativeQuery=SELECT p.* FROM MY_PRODUCT p WHERE p.COLOR='${variable.color}'")
            .end() // end multicast
            .log("${body}")
    .end();
    //.toD()
    // Test usePersist=true
    // Test useExecuteUpdate=true to test Update and Delete Methods
  }
}
