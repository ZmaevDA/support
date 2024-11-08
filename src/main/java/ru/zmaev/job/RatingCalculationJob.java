package ru.zmaev.job;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.service.BuildService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingCalculationJob implements Job {
    private final BuildService buildService;

    @Override
    public void execute(JobExecutionContext context) {
        List<Build> builds = buildService.findAll();
        for (Build build : builds) {
            buildService.recalculateRating(build);
        }
    }
}
