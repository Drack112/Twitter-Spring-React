package com.gmail.drack.commons.event;

public interface UserEvent {

    Long getId();

    String getFullName();

    String getUsername();

    String getAbout();

    String getAvatar();

    boolean isPrivateProfile();

    boolean isMutedDirectMessages();

    boolean isActive();
}
