package org.example.service;

import java.util.Comparator;
import java.util.List;
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
            return gitlabProjectApiDTOS.stream()
                .map(gitlabProjectMapper::toGitlabProjectDTO)
                .sorted(sortProjects(sorting, ordering))
                .filter(projectDTO -> filterByEvenOddAll(restrict, projectDTO))
                .limit(limit)
                .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving data from Gitlab" + e);
        }
    }

    private Comparator<GitlabProjectDTO> sortProjects(Sorting sorting, Ordering ordering) {

        Comparator<GitlabProjectDTO> comparator;

        switch (sorting) {
            case SORTING_ID -> comparator = Comparator.comparing(GitlabProjectDTO::getId);
            case SORTING_NAME -> comparator = Comparator.comparing(GitlabProjectDTO::getName);
            default -> throw new IllegalArgumentException("Invalid sort atribute" + sorting);
        }

        if (ordering == Ordering.ORDERING_DESC) {
            comparator = comparator.reversed();
        }
        return comparator;

    }

    private static boolean filterByEvenOddAll(Restrict restrict, GitlabProjectDTO projectDTO) {
        switch (restrict) {
            case RESTRICT_EVEN -> {
                return projectDTO.getId() % 2 == 0;
            }
            case RESTRICT_ODD -> {
                return projectDTO.getId() % 2 != 0;
            }
            case RESTRICT_ALL -> {
                return true;
            }
            default -> throw new IllegalArgumentException("Invalid sort atribute" + restrict);
        }
    }


}


