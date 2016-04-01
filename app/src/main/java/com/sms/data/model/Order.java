package com.sms.data.model;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import java.util.Date;
import java.util.Set;

public class Order {

    private Long id;
    private Date dateTime;
    private String description;
    private Firm firm;
    private Route route;
    private Set<OrderRow> rows;
    private Double sum;
    private Date shippingDate;
    private Date paymentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Set<OrderRow> getRows() {
        return rows;
    }

    public void setRows(Set<OrderRow> rows) {
        this.rows = rows;
    }

}
