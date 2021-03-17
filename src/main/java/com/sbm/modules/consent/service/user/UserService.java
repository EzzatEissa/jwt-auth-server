package com.sbm.modules.consent.service.user;

import com.sbm.modules.consent.dto.UserDto;
import com.sbm.modules.consent.dto.UserSearchDto;
import com.sbm.modules.consent.model.User;

import java.util.List;

public interface UserService {

    public UserDto getLoggedInUser();

    public User findUserByUsername(String userName);
    public UserDto getUserByUsername(String userName);

    public List<UserDto> getUser(UserSearchDto userSearchDto);

    public User getUserByMobileNumber(String mobileNumber);

    public void saveUser(User user);
}
