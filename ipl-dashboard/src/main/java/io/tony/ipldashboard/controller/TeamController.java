package io.tony.ipldashboard.controller;

import io.tony.ipldashboard.model.League_Match;
import io.tony.ipldashboard.model.Team;
import io.tony.ipldashboard.repository.LeagueMatchRepository;
import io.tony.ipldashboard.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class TeamController {

    private final TeamRepository teamRepository;
    private final LeagueMatchRepository leagueMatchRepository;

    @GetMapping("/team")
    public List<Team> getAllTeam() {
        Iterable<Team> all = teamRepository.findAll();
        List<Team> teams = Streamable.of(all)
                .stream().toList();
        return teams;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = teamRepository.findByTeamName(teamName)
                .orElseGet(() -> new Team("no such team", 0));
        team.setLeague_matches(leagueMatchRepository.findLatestMatchesByTeam(teamName,4));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<League_Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year+1,1,1);
        List<League_Match> matchesByTeamBetweenDates = leagueMatchRepository.getMatchesByTeamBetweenDates(teamName, startDate, endDate);
        return matchesByTeamBetweenDates;
    }
}
