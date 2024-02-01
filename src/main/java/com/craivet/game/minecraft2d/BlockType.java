package com.craivet.game.minecraft2d;

public enum BlockType {

    // Almacena la ubicacion de las imagenes con el tipo de bloque
    AIR("textures/air.png"), GRASS("textures/grass.png"), DIRT("textures/dirt.png"), STONE("textures/stone.png"),
    BRICK("textures/brick.png");

    public final String location;

    BlockType(String location) {
        this.location = location;
    }

}