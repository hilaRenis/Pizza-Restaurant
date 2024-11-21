package pizza.restaurant.presantation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pizza.restaurant.domain.service.StatisticsService;
import pizza.restaurant.presantation.response.StatisticsResponse;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping()
    public ResponseEntity<StatisticsResponse> getAllStatistics(@RequestParam(required = true) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
                                                               @RequestParam(required = true, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
        Date startDateAsDate = java.sql.Date.valueOf(startDate);
        Date endDateAsDate = endDate != null ? java.sql.Date.valueOf(endDate.plusDays(1)) : null;

        return ResponseEntity.ok(statisticsService.generateStats(startDateAsDate, endDateAsDate));

    }
}