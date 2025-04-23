package com.gmail.drack.mapper;

import com.gmail.drack.commons.dto.HeaderResponse;
import com.gmail.drack.commons.mapper.BasicMapper;
import com.gmail.drack.dto.response.BlockedUserResponse;
import com.gmail.drack.repository.projection.BlockedUserProjection;
import com.gmail.drack.service.BlockUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockUserMapper {

    private final BasicMapper basicMapper;
    private final BlockUserService blockUserService;

    public HeaderResponse<BlockedUserResponse> getBlockList(Pageable pageable) {
        Page<BlockedUserProjection> blockList = blockUserService.getBlockList(pageable);
        return basicMapper.getHeaderResponse(blockList, BlockedUserResponse.class);
    }

    public Boolean processBlockList(Long userId) {
        return blockUserService.processBlockList(userId);
    }
}
