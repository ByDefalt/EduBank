package com.example.clientAPI.entity;


public class BankAccountParameterEntity {

    private Integer id = null;
    private Double overdraftLimit = null;
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(Double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BankAccountParameterEntity{" +
                "id=" + id +
                ", overdraftLimit=" + overdraftLimit +
                ", value='" + value + '\'' +
                '}';
    }
}
