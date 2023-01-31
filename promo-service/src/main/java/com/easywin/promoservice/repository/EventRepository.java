package com.easywin.promoservice.repository;

import com.easywin.promoservice.dto.EventRequest;
import com.easywin.promoservice.model.Event;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

}
