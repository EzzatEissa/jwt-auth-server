package com.sbm.modules.consent.service.user;

import com.sbm.common.security.UserPrincipalService;
import com.sbm.common.utils.MapperHelper;
import com.sbm.modules.consent.dto.UserDto;
import com.sbm.modules.consent.dto.UserSearchDto;
import com.sbm.modules.consent.model.User;
import com.sbm.modules.consent.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MapperHelper mapperHelper;

    @Override
    public UserDto getLoggedInUser() {
        User user = userRepo.findByUserName(userPrincipalService.getCurrentAuthenticatedUser().getUserName());
        return mapperHelper.transform(user, UserDto.class);
    }

    @Override
    public User findUserByUsername(String userName) {
        return userRepo.findByUserName(userName);
    }

    @Override
    public UserDto getUserByUsername(String userName) {
        User user = userRepo.findByUserName(userName);
        return mapperHelper.transform(user, UserDto.class);
    }

    @Override
    public List<UserDto> getUser(UserSearchDto userSearchDto) {
        List<User> result = userRepo.getUser(userSearchDto);
        return mapperHelper.transform(result, UserDto.class);
    }

    @Override
    public User getUserByMobileNumber(String mobileNumber) {
        return userRepo.getUserByMobile(mobileNumber);
    }
}
