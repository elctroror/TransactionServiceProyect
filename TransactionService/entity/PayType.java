package com.TransactionService.TransactionService.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class PayType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

/*@Column
    private String[] type = {
            "accountTransference","CreditCard","DebitCard"
    };*/

}
