package com.example.securepro.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    private String userId;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String name;

    private String email;
    private String profilePicturePath;
    private String accessLevel;
    private String address;
    private String gender;
    private String mobileNo;
    private Integer age;

    @Ignore
    public User(){}

    @Ignore
    public User(String userId, String username, String password, String name, String email, String profilePicturePath) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profilePicturePath = profilePicturePath;
    }

    public User(@NonNull String userId, @NonNull String username, @NonNull String password, @NonNull String name, String email, String profilePicturePath, String accessLevel, String address, String gender, String mobileNo, Integer age) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profilePicturePath = profilePicturePath;
        this.accessLevel = accessLevel;
        this.address = address;
        this.gender = gender;
        this.mobileNo = mobileNo;
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public Integer getAge() {
        return age;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profilePicturePath='" + profilePicturePath + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", age=" + age +
                '}';
    }
}
