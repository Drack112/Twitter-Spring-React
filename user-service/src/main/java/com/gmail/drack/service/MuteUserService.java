package com.gmail.drack.service;

import com.gmail.drack.repository.projection.MutedUserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MuteUserService {

    Page<MutedUserProjection> getMutedList(Pageable pageable);

    Boolean processMutedList(Long userId);
}
