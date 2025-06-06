package com.gmail.drack.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.drack.commons.constants.BatchJobConstants;
import com.gmail.drack.commons.constants.PathConstants;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_TWEETS)
public class TweetJobController {
    @Qualifier(BatchJobConstants.IMPORT_USER_JOB)
    private final Job job;
    private final JobLauncher jobLauncher;

    @SneakyThrows
    @PostMapping(PathConstants.USER_BATCH_JOB)
    public ResponseEntity<Void> runImportUserBatchJob() {
        jobLauncher.run(job, new JobParameters());
        return ResponseEntity.noContent().build();
    }
}
