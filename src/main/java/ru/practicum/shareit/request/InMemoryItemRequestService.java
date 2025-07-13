package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryItemRequestService implements ItemRequestService {
    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto dto) {
        User user = new User();
        user.setId(userId);
        ItemRequest request = ItemRequestMapper.toItemRequest(dto, user);
        request.setId(idCounter.getAndIncrement());
        request.setCreated(LocalDateTime.now());
        requests.put(request.getId(), request);
        return ItemRequestMapper.toItemRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByUser(Long userId) {
        return requests.values().stream()
                .filter(r -> r.getRequester().getId().equals(userId))
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, int from, int size) {

        List<ItemRequestDto> filtered = requests.values().stream()
                .filter(r -> !r.getRequester().getId().equals(userId)) // например, исключаем запросы пользователя
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .skip(from)
                .limit(size)
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();

        return filtered;
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        ItemRequest request = requests.get(requestId);
        if (request == null) {
            throw new NotFoundException("Request not found with id=" + requestId);
        }
        return ItemRequestMapper.toItemRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return requests.values().stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }
}
