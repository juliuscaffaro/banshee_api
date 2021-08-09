package com.example.banshee.controller;

import com.example.banshee.dto.ApiResponse;
import com.example.banshee.dto.interiorDto.DestinyMemberships;
import com.example.banshee.dto.interiorDto.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kong.unirest.JsonResponse;
import kong.unirest.Unirest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final String ACESS_TOKEN_KEY = "CLIENT_ACESS_TOKEN";

    @GetMapping
    public ResponseEntity<?> getItem(){
        DestinyMemberships destinyMemberships = getMembership();
        String membershipType = String.valueOf(Math.round(destinyMemberships.getMembershipType()));
        String membershipId = String.valueOf(destinyMemberships.getMembershipId());

        String characterId = getCharacterId(membershipType, membershipId);
        ArrayList<Item> items = getItems(membershipType, membershipId, characterId);
        return null;
    }


    private DestinyMemberships getMembership(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonResponse jsonResponse = (JsonResponse) Unirest.get("https://www.bungie.net/Platform/User/GetMembershipsForCurrentUser/")
                .header("X-API-Key", "c736d5787fbf419cab68da9553919b32")
                .header("Authorization", "Bearer " + getAcessToken())
                .asJson();
        ApiResponse apiResponse = gson.fromJson(jsonResponse.getBody().toString(), ApiResponse.class);

        return apiResponse.getResponse().getDestinyMemberships().get(0);
    }

    private String getCharacterId(String membershipType, String membershipId){
        Gson gson = new Gson();
        JsonResponse jsonResponse = (JsonResponse) Unirest.get("https://www.bungie.net/Platform/Destiny2/" +  membershipType + "/Profile/"+ membershipId + "/?components=Profiles")
                .header("X-API-Key", "c736d5787fbf419cab68da9553919b32")
                .header("Authorization", "Bearer " + getAcessToken())
                .asJson();

        ApiResponse response = gson.fromJson(jsonResponse.getBody().toString(), ApiResponse.class);
        return response.getResponse().getProfile().getData().getCharacterIds().get(0);
    }

    private ArrayList<Item> getItems(String membershipType, String membershipId, String characterId){
        Gson gson = new Gson();
        JsonResponse jsonResponse = (JsonResponse) Unirest.get("https://www.bungie.net/Platform/Destiny2/" +  membershipType + "/Profile/"+ membershipId + "/Character/" + characterId +"/?components=CharacterInventories")
                .header("X-API-Key", "c736d5787fbf419cab68da9553919b32")
                .header("Authorization", "Bearer " + getAcessToken())
                .asJson();

        ApiResponse response = gson.fromJson(jsonResponse.getBody().toString(), ApiResponse.class);
        return response.getResponse().getInventory().getData().getItems();
    }

//    private String getItemName(ArrayList<Item> items){
//        Gson gson = new Gson();
//
//        JsonResponse jsonResponse = (JsonResponse) Unirest.get("https://www.bungie.net/Platform/Destiny2/" +  membershipType + "/Profile/"+ membershipId + "/Character/" + characterId +"/?components=CharacterInventories,CharacterEquipment")
//                .header("X-API-Key", "c736d5787fbf419cab68da9553919b32")
//                .header("Authorization", "Bearer " + getAcessToken())
//                .asJson();
//
//        ApiResponse response = gson.fromJson(jsonResponse.getBody().toString(), ApiResponse.class);
//        return response.getResponse().getInventory().getData().getItems();
//    }

    public String getAcessToken(){
        Jedis jedis = new Jedis();
        return jedis.get(ACESS_TOKEN_KEY);
    }
}
