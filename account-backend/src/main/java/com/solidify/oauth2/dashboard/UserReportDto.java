package com.solidify.oauth2.dashboard;


public class UserReportDto {

    private final long userCount;

    public UserReportDto(long userCount) {
        this.userCount = userCount;
    }

    public long getUserCount() {
        return userCount;
    }
}
