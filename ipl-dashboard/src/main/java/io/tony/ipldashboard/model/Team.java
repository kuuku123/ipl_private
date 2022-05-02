package io.tony.ipldashboard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor
public class Team {

    @Id @GeneratedValue
    private long id;

    private String teamName;
    private long totalMatches;
    private long totalWins;

    @Transient
    List<League_Match> league_matches;

    public Team(String teamName, long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
}
