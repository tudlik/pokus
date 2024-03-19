package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.apache.commons.lang3.EnumUtils;
import org.example.dto.GitlabProjectDTO;
import org.example.enums.Ordering;
import org.example.enums.Restrict;
import org.example.enums.Sorting;
import org.example.service.GitLabProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Fetching data about projects from Gitlab - Rest api ")
@RestController
public class GitlabController {


    private final GitLabProjectService gitLabProjectService;

    public GitlabController(GitLabProjectService gitLabProjectService) {
        this.gitLabProjectService = gitLabProjectService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK :-)"),
        @ApiResponse(responseCode = "400", description = "BadRequest"),
        @ApiResponse(responseCode = "500", description = "Internal server error :-(")})
    @Operation(summary = "get data from Gitlab projects", description = "get data from Gilab project like a list of objects")
    @GetMapping("/projects")
    public List<GitlabProjectDTO> getDataFromGitlabProjects(
        @RequestParam(defaultValue = "RESTRICT_ALL") @Pattern(regexp = "RESTRICT_ALL|RESTRICT_EVEN|RESTRICT_ODD", message = "Invalid sorting value - choose RESTRICT_ALL or RESTRICT_EVEN or RESTRICT_ODD") String restrict,
        @RequestParam(defaultValue = "10") @Min(value = 1, message = "Limit must be greater than or equal to 1") int limit,
        @RequestParam(defaultValue = "SORTING_ID") @Pattern(regexp = "SORTING_ID|SORTING_NAME", message = "Invalid sorting value - choose SORTING_ID or SORTING_NAME") String sorting,
        @RequestParam(defaultValue = "ORDERING_ASC") @Pattern(regexp = "ORDERING_ASC|ORDERING_DESC", message = "Invalid ordering value - choose ORDERING_ASC or ORDERING_DESC") String ordering) {
        try {
            logger.debug("pozdrav z kontroleru");
            validateParameters(restrict,limit,sorting,ordering);

            Restrict restrictEnum = EnumUtils.getEnumIgnoreCase(Restrict.class, restrict);
            Sorting sortingEnum = EnumUtils.getEnumIgnoreCase(Sorting.class, sorting);
            Ordering orderingEnum = EnumUtils.getEnumIgnoreCase(Ordering.class, ordering);

            return gitLabProjectService.getDataFromGitlab(restrictEnum, limit, sortingEnum, orderingEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid request parameters:" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Exception in controller layer" + e.getMessage());
        }
    }

    private void validateParameters(String restrict, int limit, String sorting, String ordering) {
        if (!restrict.matches("RESTRICT_ALL|RESTRICT_EVEN|RESTRICT_ODD")) {
            throw new IllegalArgumentException("Invalid restrict value - choose RESTRICT_ALL, RESTRICT_EVEN, or RESTRICT_ODD");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be greater than or equal to 1");
        }
        if (!sorting.matches("SORTING_ID|SORTING_NAME")) {
            throw new IllegalArgumentException("Invalid sorting value - choose SORTING_ID or SORTING_NAME");
        }
        if (!ordering.matches("ORDERING_ASC|ORDERING_DESC")) {
            throw new IllegalArgumentException("Invalid ordering value - choose ORDERING_ASC or ORDERING_DESC");
        }
    }
}

