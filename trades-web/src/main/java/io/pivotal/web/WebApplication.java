package io.pivotal.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker
@EnableScheduling
@EnableZuulProxy
@EnableOAuth2Sso
public class WebApplication {
	
	//@Bean
	//public Sampler<?> defaultSampler() {
		//return new AlwaysSampler();
	//}
	
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

	@Bean
	public OAuth2RestTemplate oauth2RestTemplate
			(UserInfoRestTemplateFactory userInfoRestTemplateFactory) {
		return userInfoRestTemplateFactory.getUserInfoRestTemplate();
	}
}
