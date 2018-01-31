package dupol.dupol.model;

/**
 * Created by Toshiba C55B on 1/26/2018.
 */

public class UserModel {

    public String userid, image_url, nama, email, phone ,address, gender, status,ts;

    public UserModel(){

    }

    public UserModel(String userid, String email, String status) {
        this.userid = userid;
        this.email = email;
        this.status = status;
    }



    public UserModel(String userid, String nama, String address, String gender) {
        this.userid = userid;
        this.nama = nama;
        this.address = address;
        this.gender = gender;
    }

    public UserModel(String userid, String image_url, String nama, String email, String phone, String address, String gender, String status, String ts) {
        this.userid = userid;
        this.image_url = image_url;
        this.nama = nama;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.status = status;
        this.ts = ts;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
