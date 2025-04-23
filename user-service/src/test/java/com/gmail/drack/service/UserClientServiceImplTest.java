package com.gmail.drack.service;

import com.gmail.drack.UserServiceTestHelper;
import com.gmail.drack.commons.dto.response.notification.NotificationUserResponse;
import com.gmail.drack.repository.projection.NotificationUserProjection;
import com.gmail.drack.commons.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserClientServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserClientService userClientService;

    @Test
    public void getUsersWhichUserSubscribed() {
        NotificationUserProjection userProjection = UserServiceTestHelper.createNotificationUserProjection();
        List<NotificationUserProjection> notificationUserProjections = List.of(userProjection);
        List<NotificationUserResponse> notificationUserResponses = List.of(new NotificationUserResponse());
        when(userRepository.getUsersWhichUserSubscribed(TestConstants.USER_ID)).thenReturn(notificationUserProjections);
        when(basicMapper.convertToResponseList(notificationUserProjections, NotificationUserResponse.class)).thenReturn(notificationUserResponses);
        assertEquals(notificationUserResponses, userClientService.getUsersWhichUserSubscribed());
        verify(userRepository, times(1)).getUsersWhichUserSubscribed(TestConstants.USER_ID);
        verify(basicMapper, times(1)).convertToResponseList(notificationUserProjections, NotificationUserResponse.class);
    }

    @Test
    public void getUserIdsWhichUserSubscribed() {
        when(userRepository.getUserIdsWhichUserSubscribed(TestConstants.USER_ID)).thenReturn(ids);
        assertEquals(ids, userClientService.getUserIdsWhichUserSubscribed());
        verify(userRepository, times(1)).getUserIdsWhichUserSubscribed(TestConstants.USER_ID);
    }
}
