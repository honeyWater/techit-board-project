package org.example.board.repository;

import org.example.board.domain.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BoardRepository
        extends CrudRepository<Board, Long>, PagingAndSortingRepository<Board, Long> {
}