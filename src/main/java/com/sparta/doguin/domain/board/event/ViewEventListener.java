package com.sparta.doguin.domain.board.event;

import com.sparta.doguin.domain.board.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewEventListener {

    private final PopularService popularService;

    @EventListener
    public void handleViewEvent(ViewEvent event) {
        if (event.getUserId() != null) {
            popularService.trackUserView(event.getBoardId(), event.getUserId());
        }
    }
}
