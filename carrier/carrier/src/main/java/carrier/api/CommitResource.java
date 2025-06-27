package carrier.api;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import carrier.service.MetadataService;
import carrier.model.CommitMetadata;

@Path("/commit")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommitResource {

    @Inject
    MetadataService metadataService;

    @POST
    @Path("/{oid}")
    public Response commit(@PathParam("oid") String oid, CommitMetadata meta) {
        meta.oid = oid;
        boolean ok = metadataService.commit(meta);
        return ok ? Response.ok().build() : Response.status(500).build();
    }
}
