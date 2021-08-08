package com.example.banshee.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {

    private String access_token;
    private String token_type;
    private float expires_in;
    private String refresh_token;
    private float refresh_expires_in;
    private String membership_id;

}

