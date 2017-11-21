package com.candle.system.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "costs")
public class Costs {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private BigDecimal cash;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationDate;

    @Transient
    public String dateView(){
        int day = this.creationDate.get(Calendar.DAY_OF_MONTH);
        int month = this.creationDate.get(Calendar.MONTH) +1;
        int year = this.creationDate.get(Calendar.YEAR);
        int hour = this.creationDate.get(Calendar.HOUR_OF_DAY);
        int minute = this.creationDate.get(Calendar.MINUTE);

        return day + "." + month + "." + year + " " + hour +":"+ minute;
    }

    public Costs() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }
}
