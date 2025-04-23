package com.gmail.drack.client;

import com.gmail.drack.commons.configuration.FeignConfiguration;
import com.gmail.drack.commons.constants.FeignConstants;
import com.gmail.drack.commons.constants.PathConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(value = FeignConstants.TAG_SERVICE, path = PathConstants.API_V1_TAGS, configuration = FeignConfiguration.class)
public interface TagClient {

    @CircuitBreaker(name = FeignConstants.TAG_SERVICE, fallbackMethod = "defaultEmptyArray")
    @GetMapping(PathConstants.SEARCH_TEXT)
    List<String> getTagsByText(@PathVariable("text") String text);

    default ArrayList<String> defaultEmptyArray(Throwable throwable) {
        return new ArrayList<>();
    }
}
