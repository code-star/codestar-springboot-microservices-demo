package com.ordina.messageservice.message;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface MessageRepository extends CrudRepository<Message, Long> {

    Optional<Message> findById(Long id);

    List<Message> findAllByUserId(Long userId);

    List<Message> findAll();

}
