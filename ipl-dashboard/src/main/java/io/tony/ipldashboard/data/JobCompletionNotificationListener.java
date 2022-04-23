package io.tony.ipldashboard.data;

import io.tony.ipldashboard.model.Team;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamData = new HashMap<>();

            em.createQuery("select m.team1 , count(*) from League_Match m group by m.team1",Object[].class)
                    .getResultList() // getting list of (team, count)
                    .stream()
                    .map(e -> new Team((String)e[0],(long)e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(),team));

            em.createQuery("select m.team2 , count(*) from League_Match m group by m.team2",Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        team.setTotalMatches(team.getTotalMatches() + (long)e[1]);
                    });

            em.createQuery("select m.matchWinner, count(*) from League_Match m group by m.matchWinner",Object[].class)
                    .getResultList()
                    .stream()
                    .filter(e -> (!((String)e[0]).equals("NA")))
                    .forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        team.setTotalWins((long) e[1]);
                    });

            teamData.values()
                    .forEach(team -> {
                        em.persist(team);
                    });



//            jdbcTemplate.query("SELECT team1, team2, date FROM league_match",
//                    (rs,row) -> "Team 1 " + rs.getString(1) + " Team 2 " + rs.getString(2) + " Date "+ rs.getString(3))
//                    .forEach(str -> System.out.println(str));
        }
    }
}
