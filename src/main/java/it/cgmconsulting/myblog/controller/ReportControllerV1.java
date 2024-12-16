package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.cgmconsulting.myblog.payload.request.ReportRequest;
import it.cgmconsulting.myblog.service.ReportService;
import it.cgmconsulting.myblog.utils.Msg;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportControllerV1 {

    private final ReportService reportService;

    @PostMapping("/v1/report")
    @PreAuthorize("hasAuthority('MEMBER')")
    @Operation(summary = "Create a report",
            description = "Create a report inside the database\n" +
                    "if the report already exists and his status is not close the counter will increment", tags = {"ReportReason"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report successfully created"),
            @ApiResponse(responseCode = "200", description = "Counter incremented"),
            @ApiResponse(responseCode = "404", description = "Comment or artifact not found"),
            @ApiResponse(responseCode = "400", description = "Wrong parameters"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<String> createReport(@RequestBody @Valid ReportRequest request){
        boolean result = reportService.createReport(request);
        if(result)
            return new ResponseEntity<>(Msg.REPORT_CREATED, HttpStatus.CREATED);
        return ResponseEntity.ok(Msg.REPORT_CREATED);
    }
}
