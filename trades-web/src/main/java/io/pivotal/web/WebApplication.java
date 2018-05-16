package io.pivotal.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
@EnableCircuitBreaker
@EnableScheduling
public class WebApplication {
	
	//@Bean
	//public Sampler<?> defaultSampler() {
		//return new AlwaysSampler();
	//}
	
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
	public RestTemplate restTemplate(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
		return new RestTemplateBuilder().interceptors(new ClientHttpRequestInterceptor() {
			@Override public ClientHttpResponse intercept(HttpRequest request,
					byte[] body, ClientHttpRequestExecution execution)
					throws IOException {

				OAuth2AuthenticationToken token =
						OAuth2AuthenticationToken.class.cast(SecurityContextHolder.getContext()
								.getAuthentication());

				OAuth2AuthorizedClient client =
						oAuth2AuthorizedClientService.loadAuthorizedClient(
								token.getAuthorizedClientRegistrationId(),
								token.getName()
						);

				request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " +
						client.getAccessToken().getTokenValue());

				return execution.execute(request, body);
			}
		}).build();
	}

}
