package com.example.banshee.dto.interiorDto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Response {
    private Profile profile;
    private ArrayList<DestinyMemberships> destinyMemberships = new ArrayList< DestinyMemberships >();
    private Inventory inventory;
    private Inventory equipment;

}