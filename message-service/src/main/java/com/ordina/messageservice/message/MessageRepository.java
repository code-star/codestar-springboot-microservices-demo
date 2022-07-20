package com.ordina.messageservice.message;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public interface MessageRepository extends CrudRepository<Message, Long> {

    Optional<Message> findById(Long id);

    List<Message> findAllByUserId(Long userId);

    List<Message> findAll();

    default List<MessageDto> findAllDtoByUserId(Long userId) {
        return findAllByUserId(userId).stream()
                .map(MessageDto::new)
                .toList();
    }

}
