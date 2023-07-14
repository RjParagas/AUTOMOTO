package com.example.automoto.Model;

public class RidersBooking {

    String rid, Sid, bookingtype, concern, date, time, image, rideraddress, ridername, ridercontact, servicetype, Status;

    public RidersBooking(){

    }

    public RidersBooking(String rid, String sid, String bookingtype, String concern, String date, String time, String image, String rideraddress, String ridername, String ridercontact, String servicetype, String Status) {
        this.rid = rid;
        this.Sid = sid;
        this.bookingtype = bookingtype;
        this.concern = concern;
        this.date = date;
        this.time = time;
        this.image = image;
        this.rideraddress = rideraddress;
        this.ridername = ridername;
        this.ridercontact = ridercontact;
        this.servicetype = servicetype;
        this.Status = Status;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getBookingtype() {
        return bookingtype;
    }

    public void setBookingtype(String bookingtype) {
        this.bookingtype = bookingtype;
    }

    public String getConcern() {
        return concern;
    }

    public void setConcern(String concern) {
        this.concern = concern;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRideraddress() {
        return rideraddress;
    }

    public void setRideraddress(String rideraddress) {
        this.rideraddress = rideraddress;
    }

    public String getRidername() {
        return ridername;
    }

    public void setRidername(String ridername) {
        this.ridername = ridername;
    }

    public String getRidercontact() {
        return ridercontact;
    }

    public void setRidercontact(String ridercontact) {
        this.ridercontact = ridercontact;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
