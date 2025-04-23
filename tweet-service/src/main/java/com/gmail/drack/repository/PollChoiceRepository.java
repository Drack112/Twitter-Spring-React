package com.gmail.drack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gmail.drack.models.PollChoice;

@Repository
public interface PollChoiceRepository extends JpaRepository<PollChoice, Long> {

}
