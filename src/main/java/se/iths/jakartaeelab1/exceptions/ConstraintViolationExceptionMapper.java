package se.iths.jakartaeelab1.exceptions;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Set;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                .add("host", uriInfo.getAbsolutePath().getHost())
                .add("resource", uriInfo.getAbsolutePath().getPath())
                .add("title", "Validation Errors");

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (ConstraintViolation<?> constraint : constraintViolations) {
            String message = constraint.getMessage();
            String propertyPath = constraint.getPropertyPath().toString();
            String[] pathParts = propertyPath.split("\\.");

            //seeing if path has at least 3 parts to avoid IndexOutOfBoundsException
            String violatedField = pathParts.length > 2 ? pathParts[2] : "unknown";

            JsonObject jsonError = Json.createObjectBuilder()
                    .add("field", violatedField)
                    .add("violationMessage", message)
                    .build();
            jsonArrayBuilder.add(jsonError);
        }

        JsonObject errorJsonEntity = jsonObjectBuilder
                .add("errors", jsonArrayBuilder.build())
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorJsonEntity)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
