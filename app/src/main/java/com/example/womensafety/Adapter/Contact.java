package com.example.womensafety.Adapter;

public class Contact {
    private String name, number;
    private  Boolean isChecked;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Contact(String name, String number, Boolean isChecked) {
        this.number = number;
        this.name=name;
        this.isChecked=isChecked;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
