package com.sbm.modules.consent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbm.modules.consent.model.Permission;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {

	public Permission getPermissionByCode(String code);

	public List<Permission> getPermissionByCodeIn(List<String> codes);
}
