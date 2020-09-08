package com.sbm.modules.consent.repository;

import com.sbm.modules.consent.dto.UserSearchDto;
import com.sbm.modules.consent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	public User findByUserName(String userName);


	@Query("select usr from User usr left join Account acc on acc.id = usr.id where " +
			"( coalesce(:#{#userFilter.userName}, null) is null or  usr.userName = :#{#userFilter.userName})" +
			" and ( coalesce(:#{#userFilter.firstName}, null) is null or  usr.firstName like %:#{#userFilter.firstName}%)" +
			" and ( coalesce(:#{#userFilter.lastName}, null) is null or  usr.lastName like %:#{#userFilter.lastName}%)" +
			" and ( coalesce(:#{#userFilter.segment}, null) is null or  usr.segment.code = :#{#userFilter.segment})" +
			" and ( coalesce(:#{#userFilter.accountNumber}, null) is null or  acc.accountNumber = :#{#userFilter.accountNumber})")
	public List<User> getUser(@Param("userFilter") UserSearchDto userFilter);


	public User getUserByMobile(String mobileNumber);

}
