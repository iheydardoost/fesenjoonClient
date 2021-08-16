package ir.sharif.ap.Presenter;

import java.time.LocalDate;

public class AuthFormEvent {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private String bio;
    private boolean loginReq;

    public AuthFormEvent() {
        this.userName = null;
        this.firstName = null;
        this.lastName = null;
        this.password = null;
        this.dateOfBirth = null;
        this.email = null;
        this.phoneNumber = null;
        this.bio = null;
        this.loginReq = true;
    }


    public AuthFormEvent setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public AuthFormEvent setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AuthFormEvent setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AuthFormEvent setPassword(String password) {
        this.password = password;
        return this;
    }

    public AuthFormEvent setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public AuthFormEvent setEmail(String email) {
        this.email = email;
        return this;
    }

    public AuthFormEvent setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public AuthFormEvent setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public AuthFormEvent setLoginReq(boolean loginReq) {
        this.loginReq = loginReq;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public boolean isLoginReq() {
        return loginReq;
    }
}
