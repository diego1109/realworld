package com.yang.realworld.api.security;

import static java.util.Arrays.asList;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public JwtTokenFilter jwtTokenFilter(){
    return new JwtTokenFilter();
  }

  protected void configure(HttpSecurity http) throws Exception{
    http.csrf().disable() //关闭默认的csrf保护。
        .cors()
        .and()
        .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .antMatchers(HttpMethod.GET, "/articles/feed").authenticated()
        .antMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
        .antMatchers(HttpMethod.GET, "/articles/**", "/profiles/**", "/tags").permitAll()
        .anyRequest().authenticated();
    http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  //这块是用来配置CORS的，哪些origin的哪些请求可以跨域访问这个应用
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(asList("*"));
    configuration.setAllowedMethods(asList("HEAD",
                                           "GET", "POST", "PUT", "DELETE", "PATCH"));
    // setAllowCredentials(true) is important, otherwise:
    // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
    configuration.setAllowCredentials(true);
    // setAllowedHeaders is important! Without it, OPTIONS preflight request
    // will fail with 403 Invalid CORS request
    configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
