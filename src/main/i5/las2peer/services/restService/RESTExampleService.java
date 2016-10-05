package i5.las2peer.services.restService;

import i5.las2peer.api.Context;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import i5.las2peer.security.UserAgent;
import i5.las2peer.services.restService.resource.VideoResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * las2peer Service
 * 
 * This is a example for a very basic las2peer service that uses the las2peer Web-Connector for RESTful access to it.
 * 
 */
// This is the base path where all resources are deployed:
@ServicePath("example")
public class RESTExampleService extends RESTService {

	/**
	 * Resources must be registered here. They cannot be registered at any later point.
	 */
	@Override
	protected void initResources() {
		getResourceConfig().register(VideoResource.class);
		getResourceConfig().register(MemberResource.class);
	}

	/**
	 * Static member classes can also be registered as resources.
	 * 
	 * NOTE: public class MemberResource {...} (without static) would not work!
	 */
	@Path("/")
	public static class MemberResource {
		/**
		 * Shows how to use the las2peer Context to get the user name.
		 */
		@GET
		@Path("/login")
		@Produces(MediaType.TEXT_PLAIN)
		public String getUserName() {
			UserAgent userAgent = (UserAgent) Context.getCurrent().getMainAgent();
			String name = userAgent.getLoginName();

			return "You are " + name + ".";
		}

		/**
		 * Shows how to get the raw post body.
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
