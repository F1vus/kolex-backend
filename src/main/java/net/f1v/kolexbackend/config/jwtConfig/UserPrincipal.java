package net.f1v.kolexbackend.config.jwtConfig;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserPrincipal extends User {
    private final Long id;

    public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.id = id;
    }
}