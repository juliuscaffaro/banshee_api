package com.example.banshee.dto.interiorDto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Data{

    public List<String> characterIds;
    public long currentSeasonHash;
    public ArrayList<Item> items;

}