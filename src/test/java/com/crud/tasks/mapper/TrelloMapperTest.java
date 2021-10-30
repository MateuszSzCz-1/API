package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TrelloMapperTest {

    @Autowired
    private TrelloMapper mapper;

    @Test
    void mapToBoards() {
        // given
        List<TrelloListDto> listDtos = new ArrayList<>();
        List<TrelloBoardDto> boardDtos = new ArrayList<>();
        TrelloBoardDto boardDto = new TrelloBoardDto("1001", "the_name", listDtos);
        boardDtos.add(boardDto);
        // when
        List<TrelloBoard> boards = mapper.mapToBoards(boardDtos);
        // then
        assertEquals("1001", boards.get(0).getId());
        assertEquals("the_name", boards.get(0).getName());
    }

    @Test
    void mapToBoardsDto() {
        // given
        List<TrelloList> lists = new ArrayList<>();
        List<TrelloBoard> boards = new ArrayList<>();
        TrelloBoard board = new TrelloBoard("1002", "the_name", lists);
        boards.add(board);
        // when
        List<TrelloBoardDto> boardDtos = mapper.mapToBoardsDto(boards);
        // then
        assertEquals("1002", boardDtos.get(0).getId());
        assertEquals("the_name", boardDtos.get(0).getName());
    }

    @Test
    void mapToList() {
        // given
        List<TrelloListDto> listDtos = new ArrayList<>();
        TrelloListDto listDto = new TrelloListDto("1003", "the_name", true);
        listDtos.add(listDto);
        // when
        List<TrelloList> list = mapper.mapToList(listDtos);
        // then
        assertEquals("1003", list.get(0).getId());
        assertEquals("the_name", list.get(0).getName());
    }

    @Test
    void mapToListDto() {
        // given
        List<TrelloList> lists = new ArrayList<>();
        TrelloList list = new TrelloList("1004", "the_name", true);
        lists.add(list);
        // when
        List<TrelloListDto> listDtos = mapper.mapToListDto(lists);
        // then
        assertEquals("1004", listDtos.get(0).getId());
        assertEquals("the_name", listDtos.get(0).getName());
    }

    @Test
    void mapToCardDto() {
        // given
        TrelloCard card = new TrelloCard("card1", "some description", "pos", "1005");
        // when
        TrelloCardDto cardDto = mapper.mapToCardDto(card);
        // then
        assertEquals("card1", cardDto.getName());
        assertEquals("some description", cardDto.getDescription());
    }

    @Test
    void mapToCard() {
        // given
        TrelloCardDto cardDto = new TrelloCardDto("card2", "some description 2", "pos", "1006");
        // when
        TrelloCard card = mapper.mapToCard(cardDto);
        // then
        assertEquals("card2", card.getName());
        assertEquals("some description 2", card.getDescription());
    }
}