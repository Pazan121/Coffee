package com.coffemachine.coffemachine.dto;

import java.util.List;

public class CartItem {

    private Long drinkId;
    private String drinkName;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
    private List<Long> ingredientIds;
    private String ingredientsInfo;
    private String pickupLocation;

    public CartItem() {
    }

    public CartItem(Long drinkId,
                    String drinkName,
                    Integer quantity,
                    Double price,
                    Double totalPrice,
                    List<Long> ingredientIds,
                    String ingredientsInfo,
                    String pickupLocation) {
        this.drinkId = drinkId;
        this.drinkName = drinkName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.ingredientIds = ingredientIds;
        this.ingredientsInfo = ingredientsInfo;
        this.pickupLocation = pickupLocation;
    }

    public Long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(Long drinkId) {
        this.drinkId = drinkId;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }

    public String getIngredientsInfo() {
        return ingredientsInfo;
    }

    public void setIngredientsInfo(String ingredientsInfo) {
        this.ingredientsInfo = ingredientsInfo;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
}