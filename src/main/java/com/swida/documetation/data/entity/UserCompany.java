package com.swida.documetation.data.entity;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.Roles;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class UserCompany implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nameOfCompany;
    private String location;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;
    private boolean active=true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired= true;
    private boolean enabled = true;

    @OneToOne(fetch = FetchType.LAZY)
    private ContrAgent contrAgent;

    @OneToMany(mappedBy = "userCompany",fetch = FetchType.LAZY)
    private List<TreeStorage> treeStorageList;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }


    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public String toString() {
        return "UserCompany{" +
                "id=" + id +
                ", nameOfCompany='" + nameOfCompany + '\'' +
                ", location='" + location + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", active=" + active +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", contrAgent=" + contrAgent +
                '}';
    }
}
