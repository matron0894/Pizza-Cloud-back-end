package com.springproject.shavermacloud.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class User implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue()
    private Long id;
    private String username;
    private String password;
    private String fullname;
    private String street;

    public User(String username, String password, String fullname, String street, String city, String state, String zip, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
    }

    private String city;
    private String state;
    private String zip;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Provider provider;


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @OneToMany(mappedBy = "user")
    private List<Order> items;
}