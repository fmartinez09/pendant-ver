package carrier.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import carrier.service.SeaweedService;

@Path("/upload")
public class UploadResource {

    @Inject
    SeaweedService seaweed;

    @POST
    @Path("/{oid}/{chunk}")
    @Consumes("application/octet-stream")
    public Response upload(@PathParam("oid") String oid,
                           @PathParam("chunk") String chunkN,
                           byte[] data) {
        boolean ok = seaweed.storeChunk(oid, chunkN, data);
        return ok ? Response.ok().build() : Response.serverError().build();
    }
}