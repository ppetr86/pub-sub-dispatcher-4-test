package combase.pubsubpublisher.security;

import combase.pubsubpublisher.globalexceptionhandling.ErrorDTO;
import combase.pubsubpublisher.util.MyObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {

    private final MyObjectMapper myObjectMapper;

    public AuthenticationExceptionHandler(MyObjectMapper myObjectMapper) {
        this.myObjectMapper = myObjectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String responseMsg = myObjectMapper //
                .getDefaultObjectMapper() //
                .writeValueAsString(new ErrorDTO("Unauthorised request."));

        response.getWriter().write(responseMsg);
        response.addHeader("Content-Type", "application/json");
        response.setStatus(401);
    }
}
