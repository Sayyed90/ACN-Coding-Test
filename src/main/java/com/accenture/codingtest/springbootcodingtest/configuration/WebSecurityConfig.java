package com.accenture.codingtest.springbootcodingtest.configuration;


import com.accenture.codingtest.springbootcodingtest.ErrorHandler.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
/*@EnableOAuth2Client*/
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider =new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // We call the service and the service will call the database
        provider.setPasswordEncoder(new BCryptPasswordEncoder()); // password later to be configured for encryption
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()//
                /*.antMatchers("/api/v1/users").hasAuthority(ADMIN)
                 .antMatchers("/api/v1/tasks").hasAnyAuthority(PRODUCT_OWNER)
                .antMatchers("/api/v1/projects").hasAnyAuthority(PRODUCT_OWNER)
                .antMatchers("/api/v1/assign").hasAnyAuthority(PRODUCT_OWNER)
                .antMatchers("/api/v1/members").hasAnyAuthority(MEMBER)*/
                .antMatchers("/resources/**","/static/**","/css/**","/js/**").permitAll()
                .anyRequest().authenticated()//
                .and()
                .formLogin()
                .loginPage("/login")//customize login page url
                .defaultSuccessUrl("/login/success")
                .failureHandler(customAuthenticationFailureHandler())//Handles the error message for invalid username and locked user
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/logout")//this will call the url after logout
                .invalidateHttpSession(true)//invalidate the session
                .clearAuthentication(true)//clear all the authentication
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")//when this url is called, the logout will act
                .permitAll()
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/logout");
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


}
