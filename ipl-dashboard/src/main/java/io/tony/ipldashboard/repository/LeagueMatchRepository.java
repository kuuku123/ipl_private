package io.tony.ipldashboard.repository;

import io.tony.ipldashboard.model.League_Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeagueMatchRepository extends CrudRepository<League_Match,Long> {


    List<League_Match> getByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2, Pageable pageable);

    @Query("select m from League_Match m where (m.team1 = :teamName or m.team2 = :teamName) and (m.date between :startDate and :endDate) order by date desc")
    List<League_Match> getMatchesByTeamBetweenDates(@Param("teamName") String teamName,
                                                    @Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate);

    default List<League_Match> findLatestMatchesByTeam(String teamName, int count) {
        return getByTeam1OrTeam2OrderByDateDesc(teamName,teamName, PageRequest.of(0,count));
    }

}
