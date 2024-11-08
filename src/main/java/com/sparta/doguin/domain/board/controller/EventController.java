package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.security.AuthUser;
import com.sparta.doguin.domain.board.dto.BoardRequest.BoardCommonRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse;
import com.sparta.doguin.domain.board.dto.BoardResponse.BoardCommonResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.service.BoardService;
import com.sparta.doguin.domain.board.service.EventService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards/events")
public class EventController{

    private final BoardService boardService;

    public EventController(EventService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@AuthenticationPrincipal AuthUser authUser,
                                                    @RequestBody BoardCommonRequest boardRequest,
                                                    @RequestPart(required = false) List<MultipartFile> files){
        User user = User.fromAuthUser(authUser);
        boardService.create(user, boardRequest);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_CREATE_SUCCESS));
    }

    @PutMapping("{boardId}")
    public ResponseEntity<ApiResponse<Void>> update(@AuthenticationPrincipal AuthUser authUser,
                                                    @PathVariable Long boardId,
                                                    @RequestBody BoardCommonRequest boardRequest,
                                                    @RequestPart(required = false) List<MultipartFile> files) {
        User user = User.fromAuthUser(authUser);
        boardService.update(user, boardId, boardRequest);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_UPDATE_SUCCESS));
    }

    @GetMapping("{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse.BoardWithAnswer>> viewOne(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long boardId) {
        User user = authUser != null ? User.fromAuthUser(authUser) : null;
        BoardResponse.BoardWithAnswer response = boardService.viewOneWithUser(boardId,user);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_FIND_ONE_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardCommonResponse>>> viewAll(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<BoardCommonResponse> responses = boardService.viewAll(page, size);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_FIND_ALL_SUCCESS, responses));

    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BoardCommonResponse>>> search(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam String title) {
        Page<BoardCommonResponse> responses = boardService.search(page,size,title);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_SEARCH_SUCCESS, responses));
    }

    @DeleteMapping("{boardId}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long boardId) {
        User user = User.fromAuthUser(authUser);
        boardService.delete(user,boardId);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.EVENT_DELETE_SUCCESS));
    }
}
