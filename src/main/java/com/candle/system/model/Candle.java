package com.candle.system.model;

import javax.persistence.*;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "candle")
public class Candle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Boolean diy;

    private Boolean aroma;

    private Long weight;

    private Long quantity;

    @Transient
    private BigDecimal outPrice;

    public void setOutPrice(BigDecimal outPrice) {
        this.outPrice = outPrice;
    }

    //    @JsonInclude
//    @Transient
    @Column(name = "incoming_price")
    private BigDecimal price;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "box_id")
    private Box box;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "glass_id")
    private Glass glass;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "prices_id")
    private Prices prices;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice() throws Exception{
        BigDecimal wax;
        if (this.glass != null){
            if (this.glass.getWeight() < this.weight){
                wax = BigDecimal.valueOf(this.weight - this.glass.getWeight()).
                        multiply(this.prices.getWax().divide(BigDecimal.valueOf(1000L))).add(this.glass.getPrice());
            } else {
                throw new Exception("свеча должна весть больше чем её посуда!");
            }
        } else {
            wax = BigDecimal.valueOf(this.weight).multiply(this.prices.getWax().divide(BigDecimal.valueOf(1000L)));
        }
        BigDecimal sum;
        BigDecimal label = this.label != null ? this.label.getPrice() : BigDecimal.ZERO;
        BigDecimal box = this.box != null ? this.box.getPrice() :  BigDecimal.ZERO;
        BigDecimal aroma = this.aroma != null ? this.prices.getAroma() :  BigDecimal.ZERO;
        BigDecimal diy = this.diy != null ? this.prices.getDiy() :  BigDecimal.ZERO;

        sum = label.add(box).add(aroma).add(diy).add(wax).add(this.prices.getWick());

        this.price = sum;
    }


    public Candle() {
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDiy() {
        return diy;
    }

    public void setDiy(Boolean diy) {
        this.diy = diy;
    }

    public Boolean getAroma() {
        return aroma;
    }

    public void setAroma(Boolean aroma) {
        this.aroma = aroma;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Glass getGlass() {
        return glass;
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public Prices getPrices() {
        return prices;
    }

    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
