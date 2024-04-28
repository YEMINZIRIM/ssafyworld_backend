package com.yeminjilim.ssafyworld.member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequestDto {

    private String sub;
    private String provider;
}
