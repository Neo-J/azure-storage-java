package com.neo.azuretabletest.entity;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class PeopleEntity extends TableServiceEntity {

    public PeopleEntity(String age, String email, String phoneNumber, String name) {
        this.partitionKey = age;
        this.rowKey = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public PeopleEntity(String age, String name) {
        this.partitionKey = age;
        this.rowKey = name;
    }

    public PeopleEntity() {
    }

    private String email;
    private String phoneNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
