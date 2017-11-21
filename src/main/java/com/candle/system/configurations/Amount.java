package com.candle.system.configurations;

import java.math.BigDecimal;

public class Amount {

    private Long countOfItems;

    private BigDecimal spentMoney;

    private BigDecimal saleMoney;

    public Amount(Long countOfItems, BigDecimal spentMoney) {
        this.countOfItems = countOfItems;
        this.spentMoney = spentMoney;
        this.saleMoney = spentMoney.multiply(BigDecimal.valueOf(3L));
    }

    public Amount() {
    }

    public Long getCountOfItems() {
        return countOfItems;
    }

    public void setCountOfItems(Long countOfItems) {
        this.countOfItems = countOfItems;
    }

    public BigDecimal getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(BigDecimal spentMoney) {
        this.spentMoney = spentMoney;
    }

    public BigDecimal getSaleMoney() {
        return saleMoney;
    }

    public void setSaleMoney(BigDecimal saleMoney) {
        this.saleMoney = saleMoney;
    }
}
