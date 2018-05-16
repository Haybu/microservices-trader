package io.pivotal.web.service;

import io.pivotal.web.domain.Account;
import io.pivotal.web.domain.AuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RefreshScope
public class AccountService {
	private static final Logger logger = LoggerFactory
			.getLogger(AccountService.class);

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${pivotal.accountsService.name}")
	private String accountsService;
	
	public void createAccount(Account account) {
		logger.debug("Saving account with userId: " + account.getUserid());
		String status =
				restTemplate.postForObject("http://" + accountsService + "/account/", account, String.class);
		logger.info("Status from registering account for "+ account.getUserid()+ " is " + status);
	}
	
	public Map<String,Object> login(AuthenticationRequest request){
		logger.debug("logging in with userId:" + request.getUsername());
		Map<String,Object> result =
				(Map<String, Object>) restTemplate.postForObject("http://" + accountsService + "/login/".toString(), request, Map.class);
		return result;
	}
	
	//TODO: change to /account/{user}
	public Account getAccount(OAuth2AuthenticationToken authentication) {
		logger.debug("Looking for account with userId: " + authentication.getName());
		Map userAttributes = this.userInfo(authentication);
		return new Account(userAttributes);
	}
	
	public void logout(String user) {
		logger.debug("logging out account with userId: " + user);
		
	    ResponseEntity<?> response = restTemplate.getForEntity("http://" + accountsService + "/logout/{user}", String.class, user);
	    logger.debug("Logout response: " + response.getStatusCode());
	}

	public Map userInfo(OAuth2AuthenticationToken authentication) {
		OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(authentication);
		//Map userAttributes = Collections.emptyMap();
		String userInfoEndpointUri = authorizedClient.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUri();
		if (!StringUtils.isEmpty(userInfoEndpointUri)) {	// userInfoEndpointUri is optional for OIDC Clients
			ResponseEntity<Map> response = restTemplate
					.exchange(userInfoEndpointUri, HttpMethod.GET, null, Map.class);

			return response.getBody();
		}
		return null;
	}

	private String getOauth2UserInfoUrl(OAuth2AuthorizedClient client) {
		return client.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUri();
	}

	private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken token) {
		return this.authorizedClientService.loadAuthorizedClient(
				token.getAuthorizedClientRegistrationId()
				, token.getName());
	}
	
}
