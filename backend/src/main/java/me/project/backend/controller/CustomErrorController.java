package me.project.backend.controller;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.exception.ErrorResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
@Slf4j
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ErrorResponse error(WebRequest request) {
        log.debug("Process error request");
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        log.debug(attributes.toString());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(attributes.get("error").toString());
        errorResponse.setStatus((Integer) attributes.get("status"));
        return errorResponse;
    }
}
