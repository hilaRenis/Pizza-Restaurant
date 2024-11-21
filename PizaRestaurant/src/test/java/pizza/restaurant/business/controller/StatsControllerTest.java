package pizza.restaurant.business.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pizza.restaurant.domain.service.StatisticsService;
import pizza.restaurant.presantation.controller.StatisticsController;
import pizza.restaurant.presantation.response.StatisticsResponse;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsControllerTest {
    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;


    @Test
    void testGetAllStatisticsWithValidDates() {
        // Setup
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        StatisticsResponse mockResponse = new StatisticsResponse(); // Assuming StatisticsResponse is a proper class
        when(statisticsService.generateStats(any(Date.class), any(Date.class))).thenReturn(mockResponse);

        // Execute
        ResponseEntity<StatisticsResponse> response = statisticsController.getAllStatistics(startDate, endDate);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllStatisticsWithNullEndDate() {
        // Setup
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        StatisticsResponse mockResponse = new StatisticsResponse(); // Assuming StatisticsResponse is a proper class
        when(statisticsService.generateStats(any(Date.class), isNull())).thenReturn(mockResponse);

        // Execute
        ResponseEntity<StatisticsResponse> response = statisticsController.getAllStatistics(startDate, null);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


}
