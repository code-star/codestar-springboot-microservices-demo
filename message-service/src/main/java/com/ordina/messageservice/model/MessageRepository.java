package com.ordina.messageservice.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface MessageRepository extends CrudRepository<Message, UUID> {

    Optional<Message> findById(UUID id);

    List<Message> findAllByUserId(UUID userId);

    List<Message> findAll();

    List<Message> findAllByOrderByCreatedAtDesc();

}
