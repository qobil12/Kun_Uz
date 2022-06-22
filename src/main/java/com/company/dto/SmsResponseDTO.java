package com.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsResponseDTO {

    private Boolean success;
    private String reason;
    private Integer resultCode;

}
