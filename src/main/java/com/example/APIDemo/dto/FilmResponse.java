package com.example.APIDemo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FilmResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer rentalDuration;
    private BigDecimal rentalRate;
    private Integer length;
    private BigDecimal replacementCost;
    private String rating;
    private String specialFeatures;
    private LocalDateTime lastUpdate;

    public FilmResponse() {}

    public FilmResponse(Integer id, String title, String description, Integer releaseYear,
                        Integer rentalDuration, BigDecimal rentalRate, Integer length,
                        BigDecimal replacementCost, String rating, String specialFeatures,
                        LocalDateTime lastUpdate) {
        this.id = id; this.title = title; this.description = description; this.releaseYear = releaseYear;
        this.rentalDuration = rentalDuration; this.rentalRate = rentalRate; this.length = length;
        this.replacementCost = replacementCost; this.rating = rating; this.specialFeatures = specialFeatures;
        this.lastUpdate = lastUpdate;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public Integer getRentalDuration() { return rentalDuration; }
    public void setRentalDuration(Integer rentalDuration) { this.rentalDuration = rentalDuration; }
    public BigDecimal getRentalRate() { return rentalRate; }
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }
    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }
    public BigDecimal getReplacementCost() { return replacementCost; }
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public String getSpecialFeatures() { return specialFeatures; }
    public void setSpecialFeatures(String specialFeatures) { this.specialFeatures = specialFeatures; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
