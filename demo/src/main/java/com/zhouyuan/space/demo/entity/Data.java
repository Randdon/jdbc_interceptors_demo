package com.zhouyuan.space.demo.entity;

public class Data {

    private Integer id;

    private String name;

    private String description;

    private String detail;

    private Integer age;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", detail='" + detail + '\'' +
                ", age=" + age +
                '}';
    }

    public Data(String name, String description, String detail, Integer age, int id) {
        this.name = name;
        this.description = description;
        this.detail = detail;
        this.age = age;
        this.id =id;
    }

    public Data() {
    }
}
