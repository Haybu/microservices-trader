package io.pivotal.web.service;

import io.pivotal.web.domain.Account;
import io.pivotal.web.domain.AuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
@RefreshScope
public class AccountService {
	private static final Logger logger = LoggerFactory
			.getLogger(AccountService.class);


	@Autowired OAuth2RestTemplate restTemplate;
	
	public void createAccount(Account account) {
		logger.debug("Saving account with userId: " + account.getUserid());
		String status = restTemplate.postForObject("http://accounts-service/account/", account, String.class);
		logger.info("Status from registering account for "+ account.getUserid()+ " is " + status);
	}
	
	public Map<String,Object> login(AuthenticationRequest request){
		logger.debug("logging in with userId:" + request.getUsername());
		Map<String,Object> result = (Map<String, Object>) restTemplate.postForObject("http://accountsService/login/".toString(), request, Map.class);
		return result;
	}
	
	//TODO: change to /account/{user}
	public Account getAccount() {
		logger.debug("Looking for authenticated user's account");
		//String email = this.getUserEmail(authentication);
		//return this.getAccount(email);

		Map<String, Object> claims = this.getUserClaims();
		Account account = new Account();
		account.setUserid((String)claims.get("sub"));
		account.setFullname((String)claims.get("given_name") + " " + (String)claims.get("family_name"));
		account.setId((String)claims.get("id"));
		account.setCreationdate(new Date(Long.valueOf((Integer)claims.get("updated_at"))));
		account.setEmail((String)claims.get("email"));
		account.setBalance(new BigDecimal(0));

		return account;
	}

	public Account getAccount(String email) {
		logger.debug("Looking for account with email: " + email);
		Account account = restTemplate.getForObject("http://accounts-service/account/?email={email}", Account.class, email);
		logger.debug("Got Account: " + account);
		return account;
	}
	
	public void logout(String user) {
		logger.debug("logging out account with userId: " + user);
		
	    ResponseEntity<?> response = restTemplate.getForEntity("http://accounts-service/logout/{user}", String.class, user);
	    logger.debug("Logout response: " + response.getStatusCode());
	}


	public String getUserEmail() {
		logger.debug("Retrieving email of the authenticated user");
		Map<String, Object> claims = this.getUserClaims();
		return (String)claims.get("email");
	}

	/**
	public Map<String, Object> getUserClaims(OAuth2Authentication authentication) {
		logger.debug("Retrieving claims for userId: " + authentication.getName());
		OAuth2AuthenticationDetails details
				= (OAuth2AuthenticationDetails) authentication.getDetails();
		OAuth2AccessToken accessToken = tokenStore
				.readAccessToken(details.getTokenValue());
		return accessToken.getAdditionalInformation();
	}
	 */

	public Map<String, Object> getUserClaims() {
		OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder
				.getContext().getAuthentication();
		Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
		Map<String, Object> details = (Map<String, Object>) userAuthentication.getDetails();
		return details;
	}

}
