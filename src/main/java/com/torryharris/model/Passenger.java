package com.torryharris.model;
//Passenger POJO Class
public class Passenger implements Comparable<Passenger>{
    String name;
    int age;
    char gender;
    double price;
    String aadhaarId;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Passenger(String name, int age, char gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    public Passenger(String name, int age, char gender, double price, String aadhaarId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.price = price;
        this.aadhaarId=aadhaarId;
    }

    public String getAadhaarId() {
        return aadhaarId;
    }

    public void setAadhaarId(String aadhaarId) {
        this.aadhaarId = aadhaarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Passenger() {
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }

    @Override
    public int compareTo(Passenger o) {
        return this.getName().compareTo(o.getName());
    }
}
