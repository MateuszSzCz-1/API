package com.crud.tasks.client;

import com.crud.tasks.domain.TrelloBoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TrelloClient {

    private final RestTemplate restTemplate;

    @Value("${trello.api.endpoint.prod}")
    private String trelloApiEndpoint;
    @Value("${trello.app.key}")
    private String trelloAppKey;
    @Value("${trello.app.token}")
    private String trelloToken;
    @Value("${trello.app.username}")
    private String username;

    public List<TrelloBoardDto> getTrelloBoards() throws DtoNotFoundException{
        TrelloBoardDto[] boardsResponse = restTemplate.getForObject(address(), TrelloBoardDto[].class);
        return Optional.ofNullable(boardsResponse)
                .map(Arrays::asList)
                .orElseThrow(DtoNotFoundException::new);
    }

    private URI address() {
        return UriComponentsBuilder.fromHttpUrl(trelloApiEndpoint + "/members/" + username + "/boards")
                .queryParam("key", trelloAppKey)
                .queryParam("token", trelloToken)
                .queryParam("fields", "name,id")
                .build()
                .encode()
                .toUri();
    }

    public List<TrelloBoardDto> filterBoards() throws DtoNotFoundException {
        List<TrelloBoardDto> inputList = getTrelloBoards();
        List<TrelloBoardDto> outputList = new ArrayList<>();
        for (TrelloBoardDto oneDto : inputList) {
            if (oneDto.getId() != null && oneDto.getName().startsWith("Kodilla")) {
                outputList.add(oneDto);
            }
        }
        return outputList;
    }
}