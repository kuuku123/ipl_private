package io.tony.ipldashboard.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter @Getter
public class Team {

    @Id @GeneratedValue
    private long ld;

    private String teamName;
    private long totalMatches;
    private long totalWins;

    public Team(String teamName, long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
}
