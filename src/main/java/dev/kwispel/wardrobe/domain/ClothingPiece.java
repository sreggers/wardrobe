package dev.kwispel.wardrobe.domain;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

@MongoEntity(database = "wardrobe", collection = "clothing")
public class ClothingPiece extends ReactivePanacheMongoEntity {
    public String name;
    public String imageSrc;
}
