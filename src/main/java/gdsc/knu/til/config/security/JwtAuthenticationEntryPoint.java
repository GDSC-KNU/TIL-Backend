package gdsc.knu.til.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.knu.til.exception.ErrorCode;
import gdsc.knu.til.exception.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ErrorResponse errorResponse = ErrorResponse.builder()
			.code(ErrorCode.UNAUTHORIZED.name())
			.message(ErrorCode.UNAUTHORIZED.getMessage())
			.build();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		try (OutputStream os = response.getOutputStream()) {
			objectMapper.writeValue(os, errorResponse);
			os.flush();
		}
	}
}
