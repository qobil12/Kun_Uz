package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

// PROJECT NAME Kun_Uz
// TIME 17:02
// MONTH 06
// DAY 20

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AttachDTO {
    private String uuid;
    private String name;
    private String type;
    private String path;
    private LocalDateTime createdDate;
}
