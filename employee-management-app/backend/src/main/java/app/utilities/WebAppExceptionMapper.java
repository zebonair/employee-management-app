package app.utilities;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class WebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException e) {
        Response.StatusType statusInfo = e.getResponse() != null
                ? e.getResponse().getStatusInfo()
                : Response.Status.INTERNAL_SERVER_ERROR;

        String message = e.getMessage();
        if (message == null || message.isBlank()) {
            message = statusInfo.getReasonPhrase();
        }

        MessageResponse body = new MessageResponse(message);

        return Response.status(statusInfo.getStatusCode())
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }
}

record MessageResponse(String message) {
}
