package com.gmail.drack.controller.rest;

import com.gmail.drack.commons.constants.PathConstants;
import com.gmail.drack.commons.dto.HeaderResponse;
import com.gmail.drack.dto.response.BlockedUserResponse;
import com.gmail.drack.mapper.BlockUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_USER)
public class BlockUserController {

    private final BlockUserMapper blockUserMapper;

    @GetMapping(PathConstants.BLOCKED)
    public ResponseEntity<List<BlockedUserResponse>> getBlockList(@PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<BlockedUserResponse> response = blockUserMapper.getBlockList(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(PathConstants.BLOCKED_USER_ID)
    public ResponseEntity<Boolean> processBlockList(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(blockUserMapper.processBlockList(userId));
    }
}
