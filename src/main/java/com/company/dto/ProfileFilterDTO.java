package com.company.dto;


import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileFilterDTO {
    private Integer id;
    private String name;
    private String surName;
    private String email;
    private ProfileRole role;
    private ProfileStatus status;
    private String createdDateTo;
    private String createdDateFrom;
    private String jwt;

}
