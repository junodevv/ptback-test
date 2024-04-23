package goorm.webide.user.dto;

import goorm.webide.user.entity.User;
import goorm.webide.user.util.enums.DeleteStatus;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class UserDetailDto implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getDeleteStatus().name();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        if(user.getDeleteStatus()== DeleteStatus.DELETE){
            return false;
        }
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(user.getDeleteStatus()== DeleteStatus.DELETE){
            return false;
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if(user.getDeleteStatus()== DeleteStatus.DELETE){
            return false;
        }
        return true;
    }

    public Long getUserNo(){
        return user.getUserNo();
    }

    public String getNickname(){
        return user.getNickname();
    }

    public String getEmail(){
        return user.getEmail();
    }

}
