package org.example.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.example.dto.GitlabProjectApiDTO;
import org.example.dto.GitlabProjectDTO;
import org.example.enums.Ordering;
import org.example.enums.Restrict;
import org.example.enums.Sorting;
import org.example.mapper.GitlabProjectMapper;
import org.example.utils.GitlabConnector;
import org.springframework.stereotype.Service;

@Service
public class GitlabProjectServiceImpl implements GitLabProjectService {

    private final GitlabConnector gitlabConnector;
    private final GitlabProjectMapper gitlabProjectMapper;

    public GitlabProjectServiceImpl(GitlabConnector gitlabConnector, GitlabProjectMapper gitlabProjectMapper) {
        this.gitlabConnector = gitlabConnector;
        this.gitlabProjectMapper = gitlabProjectMapper;
    }

    @Override
    public List<GitlabProjectDTO> getDataFromGitlab(Restrict restrict, int limit, Sorting sorting, Ordering ordering) {
        try {
            List<GitlabProjectApiDTO> gitlabProjectApiDTOS = gitlabConnector.getProjectsInfoFromGitlab();
            List<GitlabProjectDTO> gitlabProjectDTOS = gitlabProjectApiDTOS.stream()
                .map(gitlabProjectMapper::toGitlabProjectDTO)
                .sorted(sortProjects(sorting, ordering))
                .collect(Collectors.toList());

            List<GitlabProjectDTO> fileredProjectDTOs = filterByEvenOddAll(restrict, gitlabProjectDTOS);

            return fileredProjectDTOs.stream()
                .limit(limit)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving data from Gitlab" + e);
        }
    }

    private Comparator<GitlabProjectDTO> sortProjects(Sorting sorting, Ordering ordering) {

        Comparator<GitlabProjectDTO> comparator;

        switch (sorting) {
            case SORTING_ID -> {
                comparator = Comparator.comparing(GitlabProjectDTO::getId);
                break;
            }
            case SORTING_NAME -> {
                comparator = Comparator.comparing(GitlabProjectDTO::getName);
                break;
            }
            default -> throw new IllegalArgumentException("Invalid sort atribute" + sorting);
        }

        if (ordering == Ordering.ORDERING_DESC) {
            comparator = comparator.reversed();
        }
        return comparator;

    }

    private List<GitlabProjectDTO> filterByEvenOddAll(Restrict restrict, List<GitlabProjectDTO> projectDTOs) {
        List<GitlabProjectDTO> filteredProjectDTOs = new ArrayList<>();
        switch (restrict) {
            case RESTRICT_EVEN -> {
                filteredProjectDTOs = projectDTOs.stream()
                    .filter(projectDTO -> projectDTO.getId() % 2 == 0)
                    .collect(Collectors.toList());
                break;
            }
            case RESTRICT_ODD -> {
                filteredProjectDTOs = projectDTOs.stream()
                    .filter(projectDTO -> projectDTO.getId() % 2 != 0)
                    .collect(Collectors.toList());
                break;
            }
            case RESTRICT_ALL -> {
                filteredProjectDTOs = projectDTOs;
                break;
            }
            default -> throw new IllegalArgumentException("Invalid sort atribute" + restrict);
        }
        return filteredProjectDTOs;
    }
}


