package com.gmail.drack.service;

import com.gmail.drack.broker.producer.*;
import com.gmail.drack.client.LocalizationClient;
import com.gmail.drack.commons.constants.HeaderConstants;
import com.gmail.drack.client.TagClient;
import com.gmail.drack.client.TweetClient;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.repository.*;
import com.gmail.drack.commons.security.JwtProvider;
import com.gmail.drack.commons.util.TestConstants;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class AbstractServiceTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlockUserRepository blockUserRepository;

    @MockBean
    FollowerUserRepository followerUserRepository;

    @MockBean
    MuteUserRepository muteUserRepository;

    @MockBean
    UserSettingsRepository userSettingsRepository;

    @MockBean
    FollowUserProducer followUserProducer;

    @MockBean
    FollowRequestUserProducer followRequestUserProducer;

    @MockBean
    FollowUserNotificationProducer followUserNotificationProducer;

    @MockBean
    MuteUserProducer muteUserProducer;

    @MockBean
    BlockUserProducer blockUserProducer;

    @MockBean
    UpdateUserProducer updateUserProducer;

    @MockBean
    PinTweetProducer pinTweetProducer;

    @MockBean
    JwtProvider jwtProvider;

    @MockBean
    SendEmailProducer sendEmailProducer;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    BasicMapper basicMapper;

    @MockBean
    TweetClient tweetClient;

    @MockBean
    TagClient tagClient;

    @MockBean
    LocalizationClient localizationClient;

    static final PageRequest pageable = PageRequest.of(0, 20);
    static final List<Long> ids = List.of(1L, 2L, 3L);

    @Before
    public void setUp() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
    }
}
