package org.example.board.service;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.Board;
import org.example.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 목록 조회 (페이징 처리)
    public Page<Board> findAllPosts(Pageable pageable) {
        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber()
                , pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return boardRepository.findAll(sortedByDescId);
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 게시글 등록
    @Transactional
    public void createBoard(Board board) {
        board.setCreated_at(LocalDateTime.now());
        boardRepository.save(board);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoardById(Long id){
        boardRepository.deleteById(id);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Board board){
        board.setUpdated_at(LocalDateTime.now());
        boardRepository.save(board);
    }
}
