package com.maindark.livestream.repository;

import com.maindark.livestream.domain.LiveStreamMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiveStreamMessageRepository extends JpaRepository<LiveStreamMessage, Integer> {
//    LiveStreamMessage findById(String id);
    Page<LiveStreamMessage> findAllByAndStatus(String status, Pageable pageable);
}
