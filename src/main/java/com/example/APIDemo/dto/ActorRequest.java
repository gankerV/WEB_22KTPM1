package com.example.APIDemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActorRequest {
    @NotBlank(message = "firstName không được để trống")
    @Size(max = 255, message = "firstName tối đa 255 ký tự")
    private String firstName;

    @NotBlank(message = "lastName không được để trống")
    @Size(max = 255, message = "lastName tối đa 255 ký tự")
    private String lastName;

    public ActorRequest() {} // no-args cho Jackson

    public ActorRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
