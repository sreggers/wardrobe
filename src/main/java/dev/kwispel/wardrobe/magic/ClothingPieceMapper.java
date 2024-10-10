package dev.kwispel.wardrobe.magic;

import dev.kwispel.wardrobe.command.CreateClothingCommand;
import dev.kwispel.wardrobe.command.UpdateClothingCommand;
import dev.kwispel.wardrobe.domain.ClothingPiece;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "cdi", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClothingPieceMapper {
    ClothingPiece create(CreateClothingCommand command);
    ClothingPiece update(UpdateClothingCommand command, @MappingTarget ClothingPiece clothingPiece);
}
