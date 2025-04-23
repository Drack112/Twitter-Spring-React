package com.gmail.drack.mapper;

import com.gmail.drack.commons.dto.HeaderResponse;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.dto.response.MutedUserResponse;
import com.gmail.drack.repository.projection.MutedUserProjection;
import com.gmail.drack.service.MuteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MuteUserMapper {

    private final BasicMapper basicMapper;
    private final MuteUserService muteUserService;

    public HeaderResponse<MutedUserResponse> getMutedList(Pageable pageable) {
        Page<MutedUserProjection> mutedList = muteUserService.getMutedList(pageable);
        return basicMapper.getHeaderResponse(mutedList, MutedUserResponse.class);
    }

    public Boolean processMutedList(Long userId) {
        return muteUserService.processMutedList(userId);
    }
}
