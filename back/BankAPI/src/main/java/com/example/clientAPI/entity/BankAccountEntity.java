package com.example.clientAPI.entity;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.*;
import javax.validation.Valid;

public class BankAccountEntity {

    private String id = null;
    private Integer parameterId = null;
    private Integer typeId = null;
    private Double sold = null;
    private String iban = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Double getSold() {
        return sold;
    }

    public void setSold(Double sold) {
        this.sold = sold;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public String toString() {
        return "BankAccountEntity{" +
                "id='" + id + '\'' +
                ", parameterId=" + parameterId +
                ", typeId=" + typeId +
                ", sold=" + sold +
                ", iban='" + iban + '\'' +
                '}';
    }
}
