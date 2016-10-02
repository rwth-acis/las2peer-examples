package i5.las2peer.services.restService.resource;

import i5.las2peer.services.restService.data.Actor;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This is a simple subresource.
 *
 */
public class ActorResource {
	int videoId;

	public ActorResource(int videoId) {
		this.videoId = videoId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Actor> getActorList() {
		List<Actor> result = new ArrayList<>();
		// fetch actors by video id ...
		result.add(new Actor("Florentin Will"));
		result.add(new Actor("Katjana Gerz"));
		result.add(new Actor("Donald Trump"));
		return result;
	}

	@GET
	@Path("/{actorName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Actor getActor(@PathParam("actorName") String actorName) {
		// simply return the actor name ...
		return new Actor(actorName);
	}

}
