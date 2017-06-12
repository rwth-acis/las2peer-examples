package i5.las2peer.services.restService;

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

import i5.las2peer.api.Context;
import i5.las2peer.api.security.UserAgent;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import i5.las2peer.services.restService.data.Video;
import i5.las2peer.services.restService.resource.ActorResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

/**
 * las2peer Service
 * 
 * This is a example for a very basic las2peer service that uses the las2peer Web-Connector for RESTful access to it.
 * 
 */
// This is the base path where all resources are deployed:
@ServicePath("video")
public class RESTExampleService extends RESTService {

	/**
	 * Resources must be registered here. They cannot be registered at any later point.
	 */
	@Override
	protected void initResources() {
		// usually you define only a single root resource here
		getResourceConfig().register(RootResource.class);
	}

	/**
	 * This the root resource of your service. It shows how to deserialize and serialize content, structure a ŕesources
	 * and how to document the API with Swagger.
	 * 
	 * NOTE: Member classes must be static in order to be registered as resource. For example public class RootResource
	 * {...} (without static) would not work!
	 *
	 */
	// only classes annotated with @Api are scanned by Swagger:
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
	@Path("/")
	public static class RootResource {

		/**
		 * A reference to the current service.
		 */
		private final RESTExampleService service = Context.getCurrent().getService(RESTExampleService.class);

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
				video = new Video("Der jüngste Gewitter", "Ein sehr sehr guter Film!",
						"https://example.com/gewitter.ogv");
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
		 * 
		 * @param id An id to identify the resource.
		 * @return Returns the requested resource identified by the given id.
		 */
		@Path("/{id}/actor")
		public ActorResource getActorResource(@PathParam("id") int id) {
			return new ActorResource(id);
		}

		// these two methods show some more features of Jersey:

		/**
		 * Shows how to use the las2peer Context to get the user name.
		 * 
		 * @return Returns the username for the currently active agent.
		 */
		@GET
		@Path("/username")
		@Produces(MediaType.TEXT_PLAIN)
		public String getUserName() {
			UserAgent userAgent = (UserAgent) Context.getCurrent().getMainAgent();
			String name = userAgent.getLoginName();

			return "You are " + name + ".";
		}

		/**
		 * Shows how to get the raw post body.
		 * 
		 * @param content A value that is echoed back in the response.
		 * @return Returns the given content string as response.
		 */
		@POST
		@Path("/echo")
		@Consumes(MediaType.TEXT_PLAIN)
		// required to get raw content
		@Produces(MediaType.TEXT_PLAIN)
		public String echoRawContent(String content) {
			return content;
		}
	}

}
