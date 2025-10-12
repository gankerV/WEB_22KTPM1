package com.example.APIDemo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class FilmRequest {
    @NotBlank(message = "title không được để trống")
    @Size(max = 255, message = "title tối đa 255 ký tự")
    private String title;

    private String description;

    @Positive(message = "releaseYear phải > 0")
    @Max(value = 2100, message = "releaseYear không hợp lệ")
    private Integer releaseYear;

    @NotNull(message = "rentalDuration bắt buộc")
    @Positive(message = "rentalDuration phải > 0")
    private Integer rentalDuration;

    @NotNull(message = "rentalRate bắt buộc")
    @DecimalMin(value = "0.0", inclusive = false, message = "rentalRate phải > 0")
    private BigDecimal rentalRate;

    @Positive(message = "length phải > 0")
    private Integer length;

    @NotNull(message = "replacementCost bắt buộc")
    @DecimalMin(value = "0.0", inclusive = false, message = "replacementCost phải > 0")
    private BigDecimal replacementCost;

    private String rating;
    private String specialFeatures;

    public FilmRequest() {}

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
}
