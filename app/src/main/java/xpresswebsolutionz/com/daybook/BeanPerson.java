package xpresswebsolutionz.com.daybook;

public class BeanPerson {

    int personID,userID;
    String name,phone,email,address,note;

    public BeanPerson() {

    }

    public BeanPerson(String name) {
        this.name = name;
    }

    public BeanPerson(int personID, int userID, String name, String phone, String email, String address, String note) {
        this.personID = personID;
        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.note = note;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
