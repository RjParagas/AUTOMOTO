package com.example.automoto.Model;

public class ShopFeedback {
    String ShopID, UserID, Comment, RatingText,RatingValue, Date, Time;

    public ShopFeedback(){
    }

    public ShopFeedback(String shopID, String userID, String comment, String ratingText, String ratingValue, String date, String time) {
        this.ShopID = shopID;
        this.UserID = userID;
        this.Comment = comment;
        this.RatingText = ratingText;
        this.RatingValue = ratingValue;
        this.Date = date;
        this.Time = time;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getRatingText() {
        return RatingText;
    }

    public void setRatingText(String ratingText) {
        RatingText = ratingText;
    }

    public String getRatingValue() {
        return RatingValue;
    }

    public void setRatingValue(String ratingValue) {
        RatingValue = ratingValue;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
