package com.ordina.messageservice.model;

import com.ordina.messageservice.controller.dto.MessageDto;
import com.ordina.messageservice.controller.dto.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class MessageDtoRepository {
    final MessageRepository repository;

    protected MessageDtoRepository(MessageRepository repository) {
        this.repository = repository;
    }

    public Optional<MessageDto> findById(UUID id) {
        return repository.findById(id).map(MessageDto::new);
    }

    public List<MessageDto> findAllByUserId(UUID userId) {
        return repository.findAllByUserId(userId).stream()
                .map(MessageDto::new)
                .toList();
    }

    public List<MessageDto> findAll() {
        return repository.findAll().stream()
                .map(MessageDto::new)
                .toList();
    }

    public MessageDto save(MessageDto messageDto) {
        Message msg = repository.save(messageDto.toMessageEntity());  // TODO: try catch
        return new MessageDto(msg);
    }

    public MessageDto save(MessageRequest messageRequest, UUID userId) {
        return save(new MessageDto(messageRequest, userId));
    }

    public long count() {
        return repository.count();
    }

}
