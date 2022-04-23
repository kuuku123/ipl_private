package io.tony.ipldashboard.controller;

import io.tony.ipldashboard.model.Team;
import io.tony.ipldashboard.repository.LeagueMatchRepository;
import io.tony.ipldashboard.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final LeagueMatchRepository leagueMatchRepository;

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = teamRepository.findByTeamName(teamName)
                .orElseGet(() -> new Team("no such team", 0));
        team.setLeague_matches(leagueMatchRepository.findLatestMatchesByTeam(teamName,4));
        return team;
    }
}
