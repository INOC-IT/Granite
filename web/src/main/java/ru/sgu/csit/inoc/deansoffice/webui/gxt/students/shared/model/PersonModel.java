package ru.sgu.csit.inoc.deansoffice.webui.gxt.students.shared.model;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Khurtin Denis (KhurtinDN@gmail.com)
 * Date: 2/8/11
 * Time: 11:03 AM
 */
public class PersonModel extends DtoModel {
    private Sex sex;

    public PersonModel() {
    }

    public PersonModel(Long id) {
        super(id);
    }

    public String getFirstName() {
        return get("firstName");
    }

    public void setFirstName(String firstName) {
        set("firstName", firstName);
    }

    public String getMiddleName() {
        return get("middleName");
    }

    public void setMiddleName(String middleName) {
        set("middleName", middleName);
    }

    public String getLastName() {
        return get("lastName");
    }

    public void setLastName(String lastName) {
        set("lastName", lastName);
    }

    public Date getBirthday() {
        return get("birthday");
    }

    public void setBirthday(Date birthday) {
        set("birthday", birthday);
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
        set("sex", sex);
    }

    public enum Sex implements Serializable { MALE, FEMALE }
}
