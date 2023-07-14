package com.example.automoto.Model;

public class CartList {

    String CartID, ItemName, ItemID, Price, Qty, ShopID, TotalPrice, date, time, ItemImage, Status;

    public CartList(){
    }

    public CartList(String CartID, String ItemName, String itemID, String Price, String qty, String ShopID, String TotalPrice, String date, String time, String ItemImage, String Status) {
        this.CartID = CartID;
        this.ItemName = ItemName;
        this.ItemID = itemID;
        this.Price = Price;
        this.Qty = qty;
        this.ShopID = ShopID;
        this.TotalPrice = TotalPrice;
        this.date = date;
        this.time = time;
        this.ItemImage = ItemImage;
        this.Status = Status;
    }

    public String getCartID() {
        return CartID;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
