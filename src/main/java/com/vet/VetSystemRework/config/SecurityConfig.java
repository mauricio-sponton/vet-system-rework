package com.vet.VetSystemRework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.vet.VetSystemRework.service.UsuarioService;



@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	@Autowired
	UsuarioService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.authorizeRequests()
		.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
		.antMatchers("/").permitAll()
		.antMatchers("/u/p/**").permitAll()
		.antMatchers("/perfis/**").hasAuthority("ADMIN_WRITE")
		.antMatchers("/clientes/**").hasAuthority("CLIENTE_WRITE")
		.antMatchers("/clientes/listar").hasAnyAuthority("CLIENTE_WRITE, CLIENTE_READ")
		
		.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/home", true)
			.failureUrl("/login-error")
			.permitAll()
		.and()
			.logout()
			.logoutSuccessUrl("/login")
		.and()
			.exceptionHandling()
			.accessDeniedPage("/acesso-negado")
		.and()
			.rememberMe();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
}
