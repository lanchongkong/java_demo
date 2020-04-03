//package com.syk.common.security.config;
//
//import javax.servlet.Filter;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//
///**
// * @author zhouxiajie
// * @date 2019-02-03
// */
//@Configuration
//@ConditionalOnClass({UserDetailsService.class})
//@ConditionalOnBean(UserDetailsService.class)
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true, securedEnabled = true, jsr250Enabled = true)
//@ComponentScan(value = {"com.syk.common.security.**"})
//public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
//	private final UserDetailsService userDetailsService;
//
//	private final PasswordEncoder passwordEncoder;
//
//	public SecurityAutoConfiguration(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//		this.userDetailsService = userDetailsService;
//		this.passwordEncoder = passwordEncoder;
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable()
//      .anonymous().disable()
//      .logout().disable()
//      .formLogin().disable()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//      .and().authorizeRequests().anyRequest().authenticated()
//      .and().addFilterBefore(buildPermitAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//	}
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//	}
//
//	@Override
//	@Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//  /**
//   * 回环调用解决.
//   */
//	@Configuration
//  public static class DependencyAutoConfiguration {
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//      return new BCryptPasswordEncoder();
//    }
//  }
//}
