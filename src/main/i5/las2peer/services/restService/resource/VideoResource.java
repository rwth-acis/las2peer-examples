package i5.las2peer.services.restService.resource;

import i5.las2peer.services.restService.data.Video;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 * This is a best effort resource showing how to deserialize and serialize content, structure a ŕesources and how to
 * document the API with Swagger.
 *
 */
//only classes annotated with @Api are scanned by Swagger:
@Api
@SwaggerDefinition(
		info = @Info(
				title = "las2peer Example Service",
				version = "0.1",
				description = "A las2peer Example Service for demonstration purposes.",
				termsOfService = "http://your-terms-of-service-url.com",
				contact = @Contact(
						name = "John Doe",
						url = "provider.com",
						email = "john.doe@provider.com"),
				license = @License(
						name = "your software license name",
						url = "http://your-software-license-url.com")))
@Path("/video")
public class VideoResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(
			value = { @ApiResponse(
					code = HttpURLConnection.HTTP_NOT_FOUND,
					message = "Video not found.") })
	@ApiOperation(
			value = "Video",
			notes = "Returns title, description and an URI of the video.",
			response = Video.class)
	public Response getVideo(@PathParam("id") int id) {
		Video video = null;
		if (id == 1) {
			video = new Video("Der jüngste Gewitter", "Ein sehr sehr guter Film!", "https://example.com/gewitter.ogv");
		} else if (id == 2) {
			video = new Video("Brügge sehen und sterben", "Auch ein sehr sehr guter Film!",
					"https://example.com/bruegge.ogv");
		}

		if (video == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			return Response.ok().entity(video).build();
		}

	}

	@POST
	@ApiOperation(
			value = "Create Video",
			notes = "Createsa new video")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response createVideo(@ApiParam(
			value = "The video object.",
			required = true) Video video, @javax.ws.rs.core.Context UriInfo uriInfo) throws URISyntaxException {
		System.out.println("Stored video " + video.title);

		int id = 3;
		URI createdUri = uriInfo.resolve(new URI("video/" + id));

		return Response.status(Status.CREATED).entity(createdUri.toString()).build();
	}

	/**
	 * Passes the request to a subresource.
	 */
	@Path("/{id}/actor")
	public ActorResource getActorResource(@PathParam("id") int id) {
		return new ActorResource(id);
	}
}
