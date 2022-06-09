package combase.pubsubpublisher.security;

//import combase.pubsubpublisher.service.GooglePubSubSubscriberServiceKasse1;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${dispatcher.username}")
    private String username;

    @Value("${dispatcher.password}")
    private String password;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() //
                .withUser(username).password(passwordEncoder().encode(password)) //
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //
                .authorizeRequests() //
                .anyRequest().authenticated()//
                .and().httpBasic() //
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    String header = request.getHeader("Authorization");
                    String json = "UNAUTHORIZED REQUEST AT: " + Calendar.getInstance().getTime() + ". FROM IP: " + request.getRemoteAddr();
                    if (header != null && header.length() > 10) {
                        byte[] decodedBytes = Base64.decodeBase64(header.substring(6).getBytes());
                        String pair = new String(decodedBytes);
                        String[] userDetails = pair.split(":", 2);
                        json += ". USERNAME: " + userDetails[0] + ". PASSWORD: " + userDetails[1];
                    }
                    LOG.error(json);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(json);
                });
        //http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);
    }


}
