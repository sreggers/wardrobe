package dev.kwispel.wardrobe.domain;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import java.util.Set;

@MongoEntity(database = "wardrobe", collection = "clothing")
public class ClothingPiece extends ReactivePanacheMongoEntity {
    public String name;
    public String imageSrc;
    public Set<Tag> tags;
    public Set<Color> colors;
}
