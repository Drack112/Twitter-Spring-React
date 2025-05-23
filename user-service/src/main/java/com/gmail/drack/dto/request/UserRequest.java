package com.gmail.drack.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    private String fullName;
    private String about;
    private String location;
    private String website;
    private String avatar;
    private String wallpaper;
}
