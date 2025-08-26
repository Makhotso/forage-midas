package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    private long userId;
    private float amount;

    public Balance(long userId, float amount) {
        this.amount = amount;
        this.userId = userId;
    }

    public float getAmount() {
        return amount;
    }
    public long getUserId(){ return userId;}

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "<balance> userId=" + userId + ", amount=" + amount + " </balance>";
    }

    //public String toString() {
        //return "Balance {amount=" + amount + "}";
    //}
}