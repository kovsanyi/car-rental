package hu.unideb.inf.carrental.statistics.resource;

import hu.unideb.inf.carrental.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/company/statistics")
public class StatisticsResource {
    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsResource(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/incomeByBrand")
    public ResponseEntity<?> getIncomeByBrand(@RequestParam("from") String from,
                                              @RequestParam("until") String until) {
        return new ResponseEntity<>(statisticsService.getIncomeByBrand(LocalDate.parse(from), LocalDate.parse(until)),
                HttpStatus.OK);
    }
}
