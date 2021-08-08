package com.example.banshee.controller;

import com.example.banshee.dto.AuthenticationResponse;
import com.google.gson.Gson;
import kong.unirest.JsonResponse;
import kong.unirest.Unirest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/auth")
public class AuthorizationRestController {

    private final String ACESS_TOKEN_KEY = "CLIENT_ACESS_TOKEN";

    @GetMapping
    public ResponseEntity<?> redirectAccessToken(String code){

        Gson gson  = new Gson();
        JsonResponse jsonResponse = (JsonResponse) Unirest.post("https://www.bungie.net/Platform/App/OAuth/Token/")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .basicAuth("37250", "pwhNYnwF.M.cLwjkS1YeMMOitPbNByTzwTa0cHiC9HM")
                .field("grant_type", "authorization_code")
                .field("code", code).asJson();

        AuthenticationResponse authenticationResponse = gson.fromJson(jsonResponse.getBody().toString(), AuthenticationResponse.class);
        saveAcessToken(authenticationResponse.getAccess_token());
        return ResponseEntity.ok().build();
    }


    public void saveAcessToken(String acessToken){
        Jedis jedis = new Jedis();
        jedis.set(ACESS_TOKEN_KEY, acessToken);
        System.out.println("Salvei acess Token");
    }


    public String getAcessToken(){
        Jedis jedis = new Jedis();
        return jedis.get(ACESS_TOKEN_KEY);
    }




}
