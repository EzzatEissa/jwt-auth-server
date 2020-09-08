package com.sbm.modules.consent.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.sbm.common.model.BaseEntity;

/**
 * @author ezzateissa
 *
 */
@Entity
public class Segment extends BaseEntity{

	private String name;
	private String code;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name = "segment_permission", joinColumns = {
            @JoinColumn(name = "segment_id", nullable = false, updatable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
    private Set<Permission> permissions;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public void addPermission(Permission perm) {
        this.permissions.add(perm);
    }

    public void removePermission(Permission perm) {
        this.permissions.remove(perm);
    }
}
