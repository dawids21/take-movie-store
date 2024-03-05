package xyz.stasiak.takemoviestore;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

interface ShoppingCartRepository extends Repository<ShoppingCart, Long> {

    @Query("select s from ShoppingCart s")
    ShoppingCart find();

    ShoppingCart save(ShoppingCart shoppingCart);
}
