package com.sbm.modules.consent.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sbm.common.model.BaseEntity;

@Entity
public class User extends BaseEntity implements UserDetails  {

	private static final long serialVersionUID = 6817800551624199403L;

	private String name;

	private String firstName;

	private String lastName;

    private String userName;

    private String password;
    
    @ManyToOne
    private Segment segment;

    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (Permission perm : segment.getPermissions()) {
            setAuths.add(new SimpleGrantedAuthority("ROLE_"+perm.getCode().toUpperCase()));
        }
        return new ArrayList<GrantedAuthority>(setAuths);
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
