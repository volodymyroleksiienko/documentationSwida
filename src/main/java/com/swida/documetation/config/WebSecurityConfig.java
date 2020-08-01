package com.swida.documetation.config;

import com.swida.documetation.data.service.UserCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;
    @Autowired
    UserCompanyService userCompanyService;



       @Override
       protected void configure(HttpSecurity http) throws Exception {

           http.authorizeRequests()
//                    .antMatchers("/login", "/registration").permitAll()
//                    .antMatchers("/*").permitAll()
                    .antMatchers("/*").authenticated()
                    .and()
                        .formLogin()
                        .failureForwardUrl("/login?error=true")
                        .successForwardUrl("/enterRequest")
                        .passwordParameter("password")
                        .usernameParameter("username")
                        .permitAll()
                    .and()
                        .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .and()
                        .csrf().disable();
        }



    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password("user")
                .roles("USER")
                .and()
                .withUser("1")
                .password("1")
                .roles("ADMIN")
                .and()
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
        // check query !!!!!!
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from user_company where username=?")
                .authoritiesByUsernameQuery("select username, role from user_company where username=?")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());//inner join user_role ur on u.id=ur.user_id where u.username=?
    }



}























