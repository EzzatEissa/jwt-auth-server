package com.sbm.modules.consent.dto;

import com.sbm.common.dto.BaseDto;

public class UserDto extends BaseDto{

    private String name;

    private String userName;

    private SegmentDto segment;

    private String firstName;

    private String lastName;


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

    public SegmentDto getSegment() {
        return segment;
    }

    public void setSegment(SegmentDto segment) {
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
}
