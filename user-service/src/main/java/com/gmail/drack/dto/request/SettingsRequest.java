package com.gmail.drack.dto.request;

import com.gmail.drack.commons.enums.BackgroundColorType;
import com.gmail.drack.commons.enums.ColorSchemeType;
import lombok.Data;

@Data
public class SettingsRequest {

    private String username;
    private String email;
    private String countryCode;
    private String country;
    private String phoneCode;
    private Long phoneNumber;
    private String gender;
    private String language;
    private boolean mutedDirectMessages;
    private boolean privateProfile;
    private BackgroundColorType backgroundColor;
    private ColorSchemeType colorScheme;
}
