package edu.cloudnative.poc;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class MyEntityResource {

    @GET
    @Path("/entities")
    public List<MyEntity> get() {
        return MyEntity.listAll();
    }

    @GET
    @Path("/entities/{id}")
    public MyEntity getSingle(@PathParam Long id) {
    	MyEntity entity = MyEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("MyEntity with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    @Path("/save")
    public Response create(MyEntity entity) {
        if (entity.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        entity.persist();
        return Response.ok(entity).status(201).build();
    }

    @PUT
    @Path("/update/{id}")
    @Transactional
    public MyEntity update(@PathParam Long id, MyEntity newEntity) {
//        if (fruit.name == null) {
//            throw new WebApplicationException("Fruit Name was not set on request.", 422);
//        }

        MyEntity entity = MyEntity.findById(id);

        if (entity == null) {
            throw new WebApplicationException("MyEntity with id of " + id + " does not exist.", 404);
        }

        entity.f1 = newEntity.f1;

        return entity;
    }

    @DELETE
    @Path("/delete/{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
    	MyEntity entity = MyEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

    	@Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }
            return Response.status(code)
                    .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
                    .build();
        }

    }
}
