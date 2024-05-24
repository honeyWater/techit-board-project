package org.example.board.controller;

import lombok.RequiredArgsConstructor;
import org.example.board.domain.Board;
import org.example.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 1. 글 목록 보기 (페이징 처리)
    @GetMapping("/list")
    public String showBoardList(Model model,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardService.findAllPosts(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        return "list";
    }

    // 2. 게시글 상세 조회
    @GetMapping("/view/{id}")
    public String showBoardDetail(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        System.out.println(board);
        model.addAttribute("board", board);
        return "detail";
    }

    // 3.1 게시글 등록 폼
    @GetMapping("/writeform")
    public String createBoardForm(Model model) {
        model.addAttribute("board", new Board());
        return "create-form";
    }

    // 3.2 게시글 등록 후 리스트가 아닌 작성한 게시글 상세 페이지로 리다이렉트
    @PostMapping("/write")
    public String createBoard(@ModelAttribute Board board) {
        boardService.createBoard(board);
        return "redirect:/view/" + board.getId();
    }

    // 4.1 게시글 삭제 폼
    @GetMapping("/deleteform/{id}")
    public String deleteBoardForm(@PathVariable Long id, Model model) {
        Board board = new Board();
        board.setId(id);
        model.addAttribute("board", board);
        return "delete-form";
    }

    // 4.2 게시글 삭제 수행 후 리스트로 리다이렉트
    @PostMapping("/delete")
    public String deleteBoard(@ModelAttribute Board board,
                              RedirectAttributes redirectAttributes) {

        Board existBoard = boardService.findBoardById(board.getId());
        if (existBoard == null || !existBoard.getPassword().equals(board.getPassword())) {
            // 리다이렉트시 delete-form에서 error 메시지 출력
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/deleteform/" + board.getId();
        }

        boardService.deleteBoardById(board.getId());
        return "redirect:/list";
    }

    // 5.1 게시글 수정 폼
    @GetMapping("/updateform/{id}")
    public String updateBoardForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "update-form";
    }

    // 5.2 게시글 수정 폼 제출 시 수정
    @PostMapping("/update")
    public String updateBoard(@ModelAttribute Board board,
                              RedirectAttributes redirectAttributes) {

        Board existBoard = boardService.findBoardById(board.getId());
        if (existBoard == null || !existBoard.getPassword().equals(board.getPassword())) {
            // 리다이렉트시 update-form에서 error 메시지 출력
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/updateform/" + board.getId();
        }

        boardService.updateBoard(board);
        return "redirect:/view/" + board.getId();
    }
}
