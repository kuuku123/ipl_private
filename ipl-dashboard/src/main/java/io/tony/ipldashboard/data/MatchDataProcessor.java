package io.tony.ipldashboard.data;

import org.springframework.batch.item.ItemProcessor;

import io.tony.ipldashboard.model.League_Match;

import java.time.LocalDate;

public class MatchDataProcessor implements ItemProcessor<MatchInput, League_Match>{
    

    @Override
    public League_Match process(MatchInput matchInput) throws Exception {
        League_Match leagueMatch = new League_Match();
        leagueMatch.setId(Long.parseLong(matchInput.getId()));
        leagueMatch.setCity(matchInput.getCity());

        leagueMatch.setDate(LocalDate.parse(matchInput.getDate()));
        leagueMatch.setPlayerOfMatch(matchInput.getPlayer_of_match());
        leagueMatch.setVenue(matchInput.getVenue());

        //Set Team1 and Team2 depnding on the innings order
        String firstInningsTeam, secondInningsTeam;

        if ("bat".equals(matchInput.getToss_decision())) {
            firstInningsTeam = matchInput.getToss_winner();
            secondInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1())
                    ? matchInput.getTeam2() : matchInput.getTeam1();
        }
        else {
            secondInningsTeam = matchInput.getToss_winner();
            firstInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1())
                    ? matchInput.getTeam2() : matchInput.getTeam1();
        }

        leagueMatch.setTeam1(firstInningsTeam);
        leagueMatch.setTeam2(secondInningsTeam);

        leagueMatch.setTossWinner(matchInput.getToss_winner());
        leagueMatch.setTossDecision(matchInput.getToss_decision());
        leagueMatch.setMatchWinner(matchInput.getWinner());
        leagueMatch.setResult(matchInput.getResult());
        leagueMatch.setResultMargin(matchInput.getResult_margin());
        leagueMatch.setUmpire1(matchInput.getUmpire1());
        leagueMatch.setUmpire2(matchInput.getUmpire2());

        return leagueMatch;
    }

    
}