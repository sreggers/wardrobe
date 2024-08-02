package dev.kwispel.wardrobe.endpoint;

import dev.kwispel.wardrobe.command.CreateClothingCommand;
import dev.kwispel.wardrobe.domain.ClothingPiece;
import io.quarkus.mongodb.panache.common.reactive.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

@Path("clothing")
public class ClothingRestEndpoint {
    @GET
    public Multi<ClothingPiece> getClothing() {
        return ClothingPiece.streamAll();
    }

    @POST
    public Uni<RestResponse<ClothingPiece>> createClothing(@Context UriInfo uriInfo, CreateClothingCommand command) {
        var clothingPiece = new ClothingPiece();
        clothingPiece.name = command.name();
        clothingPiece.imageSrc = command.imageSrc();
        return Panache.withTransaction(clothingPiece::persist).map(cp ->
                ResponseBuilder
                        .create(Response.Status.CREATED, clothingPiece)
                        .header(HttpHeaders.LOCATION, uriInfo.getAbsolutePathBuilder().path(clothingPiece.id.toString()).build())
                        .build());
    }
}
