package com.candle.system.repository;

import com.candle.system.configurations.Amount;
import com.candle.system.model.Candle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface CandleRepository extends CrudRepository<Candle, Long> {

    @Modifying
    @Query("Update Candle c SET c.quantity= :quantity WHERE c.id= :id")
    @Transactional
    public void updateQuantity(@Param("id") Long id, @Param("quantity") Long quantity);


    @Query(value = "SELECT new com.candle.system.configurations.Amount( sum(c.quantity), sum(c.price * c.quantity)) FROM Candle c")
    public Amount getAmount();
}
