/*
package com.example.demo.services.customer.web.security;

import com.example.demo.services.customer.exception.WebSecurityConfigException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Order(1)
    @Configuration
    public static class ActuatorSecurity extends WebSecurityConfigurerAdapter {

        private static final String ADMIN = "ADMIN";
        private final WebSecurityPasswordEncoder passwordEncoder;
        @Value("${actuator.encoded.user.name}")
        private String actuatorUserName;
        @Value("${actuator.encoded.user.password}")
        private String actuatorUserPassword;

        ActuatorSecurity(final WebSecurityPasswordEncoder webSecurityPasswordEncoder) {
            this.passwordEncoder = webSecurityPasswordEncoder;
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws WebSecurityConfigException {
            try {
                httpSecurity.requestMatcher(
                        EndpointRequest.to(LoggersEndpoint.class, MetricsEndpoint.class))
                        */
/*.antMatcher("/h2-console/**")*//*

                        .authorizeRequests().anyRequest()
                        .hasRole(ADMIN).and()
                        .httpBasic().and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .enableSessionUrlRewriting(false).and()
                        .csrf().disable();

            } catch (Exception e) {
                throw new WebSecurityConfigException(e.getMessage());
            }
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authBuilder) throws WebSecurityConfigException {
            try {
                authBuilder
                        .inMemoryAuthentication()
                        .passwordEncoder(this.passwordEncoder.passwordEncoder())
                        .withUser(this.actuatorUserName)
                        .password(this.actuatorUserPassword)
                        .roles(ADMIN);

            } catch (Exception e) {
                throw new WebSecurityConfigException(e.getMessage());
            }
        }
    }

    @Configuration
    public static class AppSecurity extends WebSecurityConfigurerAdapter {

        private static final String ADMIN = "ADMIN";
        private final WebSecurityPasswordEncoder passwordEncoder;
        @Value("${app.encoded.user.name}")
        private String appUserName;
        @Value("${app.encoded.user.password}")
        private String appUserPassword;

        AppSecurity(final WebSecurityPasswordEncoder webSecurityPasswordEncoder) {
            this.passwordEncoder = webSecurityPasswordEncoder;
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws WebSecurityConfigException {
            try {
                httpSecurity
                        .antMatcher("/api/**")
                        .authorizeRequests().anyRequest()
                        .hasRole(ADMIN).and()
                        .httpBasic().and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .enableSessionUrlRewriting(false).and()
                        .csrf().disable();

            } catch (Exception e) {
                throw new WebSecurityConfigException(e.getMessage());
            }
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authBuilder) throws WebSecurityConfigException {
            try {
                authBuilder
                        .inMemoryAuthentication()
                        .passwordEncoder(this.passwordEncoder.passwordEncoder())
                        .withUser(this.appUserName)
                        .password(this.appUserPassword)
                        .roles(ADMIN);

            } catch (Exception e) {
                throw new WebSecurityConfigException(e.getMessage());
            }
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/api/**");
        }
    }

}
*/
