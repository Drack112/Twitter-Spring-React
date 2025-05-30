package com.gmail.drack.commons.util;

import com.gmail.drack.commons.constants.ErrorMessage;
import com.gmail.drack.commons.constants.HeaderConstants;
import com.gmail.drack.commons.exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {

    public static Long getAuthenticatedUserId() {
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) attribs).getRequest();
        String userId = request.getHeader(HeaderConstants.AUTH_USER_ID_HEADER);

        if (userId == null) {
            throw new ApiRequestException(ErrorMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else {
            return Long.parseLong(userId);
        }
    }
}
