package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.config.AuthUser;
import com.sparta.doguin.domain.board.dto.BoardRequest.BoardCommonRequest;
import com.sparta.doguin.domain.board.dto.BoardResponse.BoardCommonResponse;
import com.sparta.doguin.domain.board.entity.Board;
import com.sparta.doguin.domain.board.service.BoardService;
import com.sparta.doguin.domain.board.service.InquiryService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import com.sparta.doguin.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/v1/boards/inquiries")
public class InquiryController{

    private final BoardService boardService;

    public InquiryController(InquiryService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BoardCommonResponse>> create(@AuthenticationPrincipal AuthUser authUser,@RequestBody BoardCommonRequest boardRequest){
        User user = User.fromAuthUser(authUser);
        Board board = boardService.create(user,boardRequest);
        BoardCommonResponse response = new BoardCommonResponse(board.getId(),board.getTitle(),board.getContent());
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.INQUIRY_CREATE_SUCCESS, response));
    }

    @PutMapping("{boardId}")
    public ResponseEntity<ApiResponse<BoardCommonResponse>> update(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long boardId,@RequestBody BoardCommonRequest boardRequest) {
        User user = User.fromAuthUser(authUser);
        Board board = boardService.update(user, boardId, boardRequest);
        BoardCommonResponse response = new BoardCommonResponse(board.getId(),board.getTitle(),board.getContent());
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.INQUIRY_UPDATE_SUCCESS, response));
    }

    @GetMapping("{boardId}")
    public ResponseEntity<ApiResponse<BoardCommonResponse>> viewOne(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long boardId) {
        User user = User.fromAuthUser(authUser);
        Board board = boardService.viewOneWithUser(boardId, user);
        BoardCommonResponse response = new BoardCommonResponse(board.getId(), board.getTitle(), board.getContent());
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.INQUIRY_FIND_ONE_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardCommonResponse>>> viewAll(@AuthenticationPrincipal AuthUser authUser,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        User user = User.fromAuthUser(authUser);
        Page<BoardCommonResponse> responses = boardService.viewAllWithUser(page, size,user);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.INQUIRY_FIND_ALL_SUCCESS, responses));

    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BoardCommonResponse>>> search(@AuthenticationPrincipal AuthUser authUser,
                                                                   @RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam String title) {
        User user = User.fromAuthUser(authUser);
        Page<BoardCommonResponse> responses = boardService.searchWithUser(page,size,title,user);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.INQUIRY_SEARCH_SUCCESS, responses));
    }

    @DeleteMapping("{boardId}")
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long boardId) {
        User user = User.fromAuthUser(authUser);
        boardService.delete(user,boardId);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.INQUIRY_DELETE_SUCCESS));
    }
}
