package org.restler.http;

import org.restler.ServiceConfig;
import org.restler.factory.ControllerMethodDescription;
import org.restler.factory.ControllerMethodExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

public class HttpControllerMethodExecutor implements ControllerMethodExecutor {

    private ServiceConfig serviceConfig;

    public HttpControllerMethodExecutor(ServiceConfig config){
        this.serviceConfig = config;
    }

    @Override
    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    @Override
    public <T> T execute(ControllerMethodDescription<T> method, Object requestBody, Map<String, ?> pathVariables, MultiValueMap<String, String> requestParams) {

        URI target = UriComponentsBuilder.fromHttpUrl(serviceConfig.getBaseUrl()).path(method.getUriTemplate()).queryParams(requestParams).buildAndExpand(pathVariables).toUri();

        ExecutableRequest<T> request = new ExecutableRequest<>(target,method.getHttpMethod(), requestBody,serviceConfig.getRequestExecutor(),method.getReturnType());

        ResponseEntity<T> response = serviceConfig.getAuthenticationStrategy().executeAuthenticatedRequest(request);

        return response.getBody();
    }
}
