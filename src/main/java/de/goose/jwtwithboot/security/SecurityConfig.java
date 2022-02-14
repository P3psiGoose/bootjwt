package de.goose.jwtwithboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.goose.jwtwithboot.JwtwithbootApplication;
import de.goose.jwtwithboot.security.filter.CustomAuthenticationFilter;
import de.goose.jwtwithboot.security.filter.CustomAuthorisationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService; // gets MyUserServiceImpl bc it implements UserDetailsService
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * Overriding this method to expose the AuthenticationManagerBean
	 *
	 * @return the authentication manager
	 * @throws Exception the exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManagerBean());
		filter.setFilterProcessesUrl("/api/login"); // bc we can change login URL

		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**").permitAll(); // particular on the top
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority(JwtwithbootApplication.ROLE_USER);
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/save/**").hasAnyAuthority(JwtwithbootApplication.ROLE_ADMIN);
		http.authorizeRequests().anyRequest().authenticated(); // common on the bottom
		http.addFilter(filter);
		http.addFilterBefore(new CustomAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
