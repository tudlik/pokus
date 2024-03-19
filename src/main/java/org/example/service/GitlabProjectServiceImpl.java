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

            List<GitlabProjectDTO> fileredProjectDTOs = filterProjects(restrict, gitlabProjectDTOS);

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

    private List<GitlabProjectDTO> filterProjects(Restrict restrict, List<GitlabProjectDTO> projectDTOs) {
        List<GitlabProjectDTO> restricedProjectDTOs = new ArrayList<>();
        if (restrict.equals(Restrict.RESTRICT_EVEN)) {
            for (int i = 0; i < projectDTOs.size(); i++) {
                if ((i + 1) % 2 == 0) {
                    restricedProjectDTOs.add(projectDTOs.get(i));
                }
            }
        } else if (restrict.equals(Restrict.RESTRICT_ODD)) {
            for (int i = 0; i < projectDTOs.size(); i++) {
                if ((i + 1) % 2 == 0) {
                    restricedProjectDTOs.add(projectDTOs.get(i));
                }
            }
        } else if (restrict.equals(Restrict.RESTRICT_ALL)) {
            restricedProjectDTOs = projectDTOs;
        }
        return restricedProjectDTOs;
    }

}


