package app.dao.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DefaultServiceDao implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails[] SERVICE_LIST = new UserDetails[]{
                new User("authentication_service","$2a$10$5KJc1VOK1c3NIaA1WNAPo.T2RHvfBEkbBCpBTrGNEW8MtGtTwMeyW",List.of(new SimpleGrantedAuthority("ROLE_AUTH_SERVICE"))),
                new User("conversation_service","$2a$10$9CtrYTrqAZFNj2q2WNkQMupLhrZcaF7gfbZNKobr.PnuTfTjUAKhe",List.of(new SimpleGrantedAuthority("ROLE_AUTH_SERVICE")))
        };

        return Arrays.stream(SERVICE_LIST)
                .filter((p) -> p.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> {throw new UsernameNotFoundException("Username not found");});
    }
}
