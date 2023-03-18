package vn.fs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import vn.fs.service.UserDetailService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailService userDetailService;

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.sessionFixation().migrateSession()
				.maximumSessions(1).maxSessionsPreventsLogin(false)
				.expiredUrl("/login?expired");
		
		http.csrf().disable();
		
		// Admin page
		http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
		// If you are not logged in, you will be redirected to the /login page.
		http.authorizeRequests().antMatchers("/checkout").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/addToCart").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/doFavorite").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/doUnFavorite").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/product/{productId}/comment").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/product/favorite/**").access("hasRole('ROLE_USER')");
		http.authorizeRequests()
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginProcessingUrl("/doLogin")
			.loginPage("/login")
			.defaultSuccessUrl("/?login_success")
			.successHandler(new SuccessHandler()).failureUrl("/login?error=true")
			.failureUrl("/login?error=true")
			.permitAll()
			.and()
		.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/?logout_success")
			.permitAll();
			
		 // remember-me
		http.rememberMe()
			.rememberMeParameter("remember");
		
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**");
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
	}

}
