package com.crud.tasks.client;

import com.crud.tasks.config.TrelloConfig;
import com.crud.tasks.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrelloClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloClient.class);

    @InjectMocks
    private TrelloClient trelloClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private TrelloConfig trelloConfig;

    @Test
    public void shouldFetchTrelloBoards() throws URISyntaxException {
        // Given
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com");
        when(trelloConfig.getTrelloAppKey()).thenReturn("test");
        when(trelloConfig.getTrelloToken()).thenReturn("test");
        when(trelloConfig.getTrelloUser()).thenReturn("test");
        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[1];
        trelloBoards[0] = new TrelloBoardDto("test_id", "test_board", new ArrayList<>());
        URI uri = new URI("http://test.com/members/test/boards?key=test&token=test&fields=name,id&lists=all");
        when(restTemplate.getForObject(uri, TrelloBoardDto[].class)).thenReturn(trelloBoards);
        // When
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getBoards();
        // Then
        assertEquals(1, fetchedTrelloBoards.size());
        assertEquals("test_id", fetchedTrelloBoards.get(0).getId());
        assertEquals("test_board", fetchedTrelloBoards.get(0).getName());
        assertEquals(new ArrayList<>(), fetchedTrelloBoards.get(0).getLists());
    }

    @Test
    public void shouldCreateCard() throws URISyntaxException {
        // Given
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com");
        when(trelloConfig.getTrelloAppKey()).thenReturn("test");
        when(trelloConfig.getTrelloToken()).thenReturn("test");
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test task", "Test Description", "top", "test_id");
        URI uri = new URI(
                "http://test.com/cards?key=test&token=test&name=Test%20task&desc=Test%20Description&pos=top&idList=test_id");
        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto("1", "Test task", "http://test.com");
        when(restTemplate.postForObject(uri, null, CreatedTrelloCardDto.class)).thenReturn(createdTrelloCardDto);
        // When
        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);
        // Then
        assertEquals("1", newCard.getId());
        assertEquals("Test task", newCard.getName());
        assertEquals("http://test.com", newCard.getShortUrl());
    }

    @Test
    public void shouldReturnEmptyList() throws URISyntaxException {
        // given
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com");
        when(trelloConfig.getTrelloAppKey()).thenReturn("test");
        when(trelloConfig.getTrelloToken()).thenReturn("test");
        when(trelloConfig.getTrelloUser()).thenReturn("test");
        URI url = new URI("http://test.com/members/test/boards?key=test&token=test&fields=name,id&lists=all");
        // when
        try {
            TrelloBoardDto[] boardsResponse = restTemplate.getForObject(url, TrelloBoardDto[].class);
            Arrays.asList(ofNullable(boardsResponse).orElse(new TrelloBoardDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        when(restTemplate.getForObject(url, TrelloBoardDto[].class)).thenReturn(null);
        List<TrelloBoardDto> emptyList = trelloClient.getBoards();
        // then
        assertEquals(0, emptyList.size());
    }

    // Kodilla exercise 29.2 further development of tests (in order to increase coverage)
    @Test
    public void testCreateNewCard() throws URISyntaxException {
        // Given
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.io");
        when(trelloConfig.getTrelloAppKey()).thenReturn("test");
        when(trelloConfig.getTrelloToken()).thenReturn("test");
        TrelloCardDto trelloCardDto = new TrelloCardDto("theName", "Descr", "top", "101");
        URI uri = new URI("http://test.io/cards?key=test&token=test&name=theName&desc=Descr&pos=top&idList=101");
        Trello trello = new Trello(1, 2);
        AttachmentsByType attachments = new AttachmentsByType(trello);
        Badges badges = new Badges(2, attachments);
        CreatedTrelloCardDto card = new CreatedTrelloCardDto("101", "theName", "http://test.io", badges);
        when(restTemplate.postForObject(uri, null, CreatedTrelloCardDto.class)).thenReturn(card);
        // When
        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);
        // Then
        assertEquals("101", newCard.getId());
        assertEquals("theName", newCard.getName());
        assertEquals("http://test.io", newCard.getShortUrl());
        assertEquals(2, newCard.getBadges().getVotes());
        assertEquals(1, newCard.getBadges().getAttachmentsByType().getTrello().getBoard());
        assertEquals(2, newCard.getBadges().getAttachmentsByType().getTrello().getCard());
    }

}