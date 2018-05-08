package io.pivotal.web.config;

import io.pivotal.web.security.CustomAuthenticationProvider;
import io.pivotal.web.security.CustomCredentialsService;
import io.pivotal.web.security.LogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomCredentialsService customCredentialsService;
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
				.antMatchers(
						"/",
						"/registration",
						"/hystrix.stream"
				).permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
				.failureUrl("/error").permitAll()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
            .logout()
            .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
    }

	@Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/webjars/**")
				//.antMatchers("/image/**")
                .antMatchers("/images/**")
				.antMatchers("/css/**")
				.antMatchers("/js/**");
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
		auth.userDetailsService(customCredentialsService);
	}
}
