package com.example.login;

//import com.itextpdf.text.pdf.PdfPCell;

import java.util.Date;

/**

 The Sale class represents a sale transaction made by an advisor for a customer.
 It includes information such as the ticket number, total amount, currency, exchange rate,
 tax, payment type, commission rate, advisor ID, customer email, sale ID, and date of sale.
 This class provides various constructors to initialize the object, as well as getter and
 setter methods for accessing and modifying the instance variables.
 @author [Sykat Howlader]
 */

public class Sale {
        private long ticketNumber;
        private float totAmount;
        private String currency;
        private float exchangeRate;
        private float tax;
        private String paymentType;
        private String commissionRate;
        private int advisorID;
        private String customer_email;
        private int sale_id;
        private Date sell_date;

        public Sale(int sale_id, String customer_email, Date sell_date){
            this.sale_id=sale_id;
            this.customer_email=customer_email;
            this.sell_date=sell_date;
        }

        public Sale(long ticketNumber, float totAmount, String currency, float exchangeRate
                    , String paymentType, String commissionRate) {
            this.ticketNumber = ticketNumber;
            this.totAmount = totAmount;
            this.currency = currency;
            this.exchangeRate = exchangeRate;
            this.tax = totAmount*0.20f;
            this.paymentType = paymentType;
            this.commissionRate = commissionRate;
        }

        public Sale(int advisorID, long ticketNumber, float totAmount, String currency, float exchangeRate
                , String paymentType, String commissionRate) {

            this.advisorID= advisorID;
            this.ticketNumber = ticketNumber;
            this.totAmount = totAmount;
            this.currency = currency;
            this.exchangeRate = exchangeRate;
            this.tax = totAmount*0.20f;
            this.paymentType = paymentType;
            this.commissionRate = commissionRate;
        }

    // Getter methods
    public long getTicketNumber() {
        return ticketNumber;
    }

    public float getTotAmount() {
        return totAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public float getExchangeRate() {
        return exchangeRate;
    }

    public float getTax() {
        return tax;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public int getAdvisorID() {
        return advisorID;
    }

    // Setter methods
    public void setTicketNumber(long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setTotAmount(float totAmount) {
        this.totAmount = totAmount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void setAdvisorID(int advisorID) {
        this.advisorID = advisorID;
    }
    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public Date getSell_date() {
        return sell_date;
    }

    public void setSell_date(Date sell_date) {
        this.sell_date = sell_date;
    }



    }

