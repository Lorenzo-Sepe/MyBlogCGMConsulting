package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.cgmconsulting.myblog.payload.request.ReportRequest;
import it.cgmconsulting.myblog.payload.response.ReportResponse;
import it.cgmconsulting.myblog.service.ReportService;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportControllerV1 {

    private final ReportService reportService;

    @PostMapping("/v1/reports")
    @PreAuthorize("hasAuthority('MEMBER')")
    @Operation(summary = "Create a report",
            description = "Create a report inside the database\n" +
                    "if the report already exists and his status is not close the counter will increment",
            tags = {"Report"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report successfully created"),
            @ApiResponse(responseCode = "200", description = "Counter incremented"),
            @ApiResponse(responseCode = "404", description = "Comment or artifact not found"),
            @ApiResponse(responseCode = "400", description = "Wrong parameters"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<String> createReport(@RequestBody @Valid ReportRequest request, @AuthenticationPrincipal UserDetails userDetails){
        boolean result = reportService.createReport(request, userDetails);
        if(result)
            return new ResponseEntity<>(Msg.REPORT_CREATED, HttpStatus.CREATED);
        return ResponseEntity.ok(Msg.REPORT_CREATED);
    }

    @GetMapping("/v1/reports")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @Operation(summary = "Get reports",
            description = "Get paginated reports" ,
            tags = {"Report"})
    public ResponseEntity<List<ReportResponse>> getReports(
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire; 0 è la prima pagina
            @RequestParam(defaultValue = "5") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "status") String sortBy, // la colonna presa in considerazione per l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // ASC o DESC, ordinamento ascendente o discendente
    ){
        return ResponseEntity.ok(reportService.getReports(pageNumber, pageSize, sortBy, direction));
    }

    @PatchMapping("/v1/reports/{reportId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    @Operation(summary = "Change report status",
            description = "Change report status. Rules:\n" +
                    "1) The change of status is unidirectional: OPEN -> IN_PROGRESS -> CLOSED_\n" +
                    "2) A closed report cannot change status, is unmodifiable\n" +
                    "3) The CLOSED_WITH_BAN status implies user ban and comment /made by you censorship" ,
            tags = {"Report"})
    public ResponseEntity<ReportResponse> updateReport(
            @PathVariable int reportId,
            @RequestParam String status,
            @RequestParam(required = false) @Size(max=30) String reason
    ){
        return ResponseEntity.ok(reportService.updateRecord(reportId, status, reason));
    }

}
