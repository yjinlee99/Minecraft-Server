package com.minecraft.smallminecraft.userData.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserData {
    @Id @Column(name = "userdata_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int health;

    private int starve;

    private float positionX;

    private float positionY;

    private float positionZ;

    private String head;

    private String top;

    private String bottom;

    private String shoes;

    private String right;

    private String left;

    @Convert(converter = StringListConverter.class)
    private List<String> slots;

}
