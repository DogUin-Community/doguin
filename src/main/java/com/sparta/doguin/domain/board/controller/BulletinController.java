package com.sparta.doguin.domain.board.controller;

import com.sparta.doguin.domain.board.BoardType;
import com.sparta.doguin.domain.board.dto.request.BoardRequest;
import com.sparta.doguin.domain.board.dto.response.BoardResponse;
import com.sparta.doguin.domain.board.service.BoardService;
import com.sparta.doguin.domain.board.service.BulletinService;
import com.sparta.doguin.domain.board.service.EventService;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseBoardEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards/bulletins")
public class BulletinController implements BoardController{

    private final BoardService boardService;
    public BulletinController(BulletinService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponse<BoardResponse>> create(@RequestBody BoardRequest boardRequest){
        BoardResponse response = BoardResponse.from(boardService.create(boardRequest));
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.BULLETIN_CREATE_SUCCESS, response));
    }

    @Override
    @PutMapping("{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> update(@PathVariable Long boardId,@RequestBody BoardRequest boardRequest) {
        BoardResponse response = BoardResponse.from(boardService.update(boardId,boardRequest));
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.BULLETIN_UPDATE_SUCCESS, response));
    }

    @Override
    @GetMapping("{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> viewOne(@PathVariable Long boardId) {
        BoardResponse response = BoardResponse.from(boardService.viewOne(boardId));
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.BULLETIN_FIND_ONE_SUCCESS, response));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> viewAll(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<BoardResponse> responses = boardService.viewAll(page, size);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.BULLETIN_FIND_ALL_SUCCESS, responses));

    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> search(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam String title) {
        Page<BoardResponse> responses = boardService.search(page,size,title);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.BULLETIN_SEARCH_SUCCESS, responses));
    }

    @Override
    @DeleteMapping("{boardId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return ApiResponse.of(ApiResponse.of(ApiResponseBoardEnum.BULLETIN_DELETE_SUCCESS));
    }
}