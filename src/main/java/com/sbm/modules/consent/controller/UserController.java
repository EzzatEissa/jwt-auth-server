package com.sbm.modules.consent.controller;

import java.security.Principal;
import java.util.List;

import com.sbm.modules.consent.dto.UserDto;
import com.sbm.modules.consent.dto.UserSearchDto;
import com.sbm.modules.consent.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }
    
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {

        UserDto userDto = userService.getLoggedInUser();

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @GetMapping("/{userName}")
    public ResponseEntity<UserDto> searchForUser(@PathVariable("userName") String userName) {

        UserDto userDto = userService.getUserByUsername(userName);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> listRequestsToBeAddedToStatement(@RequestParam(value = "userName", required = false) String userName,
                                                                   @RequestParam(value = "accountNumber", required = false) String accountNumber,
                                                                   @RequestParam(value = "firstName", required = false) String firstName,
                                                                   @RequestParam(value = "lastName", required = false) String lastName,
                                                                   @RequestParam(value = "segment", required = false) String segment){

        UserSearchDto userSearchDto = new UserSearchDto();

        userSearchDto.setUserName(userName);
        userSearchDto.setFirstName(firstName);
        userSearchDto.setLastName(lastName);
        userSearchDto.setSegment(segment);
        userSearchDto.setAccountNumber(accountNumber);

        List<UserDto> results = userService.getUser(userSearchDto);
        return new ResponseEntity<List<UserDto>>(results, HttpStatus.OK);
    }

}
