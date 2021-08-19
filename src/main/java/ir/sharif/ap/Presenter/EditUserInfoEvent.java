package ir.sharif.ap.Presenter;

import java.time.LocalDate;

public class EditUserInfoEvent {
    private String firstName, lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber, bio;
    private byte[] userImage;

    public EditUserInfoEvent() {
        this.firstName = null;
        this.lastName = null;
        this.dateOfBirth = null;
        this.phoneNumber = null;
        this.bio = null;
        this.userImage = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }
}
