package com.boot.cut_costs.config.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ACCOUNT_ROLE")
public class CustomRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ROLE_ID")
    private long id;

    @Column(name="ROLE")
    private String role;

    public long getId() {
        return id;
    }

    //delete if not necessary
    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public CustomRole() {} //for jpa
    
    public CustomRole(String role) {
    	this.role = role;
    }
}