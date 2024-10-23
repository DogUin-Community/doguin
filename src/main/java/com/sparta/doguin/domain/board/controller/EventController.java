package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.service.BoardService;
import com.sparta.doguin.domain.board.service.EventService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards/events")
public class EventController implements BoardController{

    private final BoardService boardService;

    public EventController(EventService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponse<BoardResponse>> create(@AuthenticationPrincipal AuthUser authUser, @RequestBody BoardRequest boardRequest){
        User user = User.fromAuthUser(authUser);
        BoardResponse response = BoardResponse.from(boardService.create(user,boardRequest));
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_CREATE_SUCCESS, response));
    }

    @Override
    @PutMapping("{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> update(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long boardId,@RequestBody BoardRequest boardRequest) {
        User user = User.fromAuthUser(authUser);
        BoardResponse response = BoardResponse.from(boardService.update(user,boardId,boardRequest));
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_UPDATE_SUCCESS, response));
    }

    @Override
    @GetMapping("{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> viewOne(@PathVariable Long boardId) {
        BoardResponse response = BoardResponse.from(boardService.viewOne(boardId));
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_FIND_ONE_SUCCESS, response));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> viewAll(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<BoardResponse> responses = boardService.viewAll(page, size);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_FIND_ALL_SUCCESS, responses));

    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> search(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam String title) {
        Page<BoardResponse> responses = boardService.search(page,size,title);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_SEARCH_SUCCESS, responses));
    }

    @Override
    @DeleteMapping("{boardId}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long boardId) {
        User user = User.fromAuthUser(authUser);
        boardService.delete(user,boardId);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_DELETE_SUCCESS));
    }
}
