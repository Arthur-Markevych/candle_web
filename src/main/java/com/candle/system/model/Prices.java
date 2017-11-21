package com.candle.system.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "used_prices")
public class Prices {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private BigDecimal wax;

    @Column(name = "candlewick")
    private BigDecimal wick;

    private BigDecimal diy;

    private BigDecimal aroma;

    public Prices() {
    }

    public Prices(String name, BigDecimal wax, BigDecimal wick, BigDecimal diy, BigDecimal aroma) {
        this.name = name;
        this.wax = wax;
        this.wick = wick;
        this.diy = diy;
        this.aroma = aroma;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getWax() {
        return wax;
    }

    public void setWax(BigDecimal wax) {
        this.wax = wax;
    }

    public BigDecimal getWick() {
        return wick;
    }

    public void setWick(BigDecimal wick) {
        this.wick = wick;
    }

    public BigDecimal getDiy() {
        return diy;
    }

    public void setDiy(BigDecimal diy) {
        this.diy = diy;
    }

    public BigDecimal getAroma() {
        return aroma;
    }

    public void setAroma(BigDecimal aroma) {
        this.aroma = aroma;
    }
}
