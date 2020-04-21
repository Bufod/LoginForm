package com.example.loginform;

import java.io.Serializable;

public class User implements Serializable {
    public static final String SEX_WOMAN = "женский",
            SEX_MAN = "мужской";
    private Integer id;
    private String login,
            password,
            firstname,
            lastname,
            address,
            sex;

    public User(Integer id, String login, String password, String firstname,
                String lastname, String address, Integer sex) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.sex = sex == 0 ? SEX_WOMAN : SEX_MAN;
    }

    public User(Integer id, String login, String firstname,
                String lastname, String address, Integer sex){
        this(id, login, null, firstname, lastname, address, sex);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIntSex() {
        return sex.equals(SEX_WOMAN) ? 0 : 1;
    }

    public void setSex(Integer sex) {
        this.sex = sex == 0 ? SEX_WOMAN : SEX_MAN;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
