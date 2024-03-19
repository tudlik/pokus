package org.example.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.dto.GitlabProjectApiDTO;
import org.example.dto.GitlabProjectDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GitlabConnector {

    @Value("${gitlab.apiUrl}")
    private String apiUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<GitlabProjectApiDTO> getProjectsInfoFromGitlab() {
        try {
            GitlabProjectApiDTO[] projectApiDTOS = restTemplate.getForObject(apiUrl, GitlabProjectApiDTO[].class);
            return Arrays.asList(projectApiDTOS);

        } catch (RestClientException e) {
            throw new RuntimeException("Error communicating with the GitLab server" + e);
        }
    }


}
