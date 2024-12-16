package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.cgmconsulting.myblog.payload.request.ReportReasonRequest;
import it.cgmconsulting.myblog.payload.response.ReportReasonResponse;
import it.cgmconsulting.myblog.service.ReportReasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportReasonControllerV1 {

    private final ReportReasonService reportReasonService;

    @PostMapping("/v1/reportReason")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a report reason",
            description = "Create a report reason inside the database\n" +
                    "if the reason not exists or if reason exists but is not valid (expired)", tags = {"ReportReason"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report reason successfully created"),
            @ApiResponse(responseCode = "409", description = "Report reason already present"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ReportReasonResponse> addReportReason(@RequestBody @Valid ReportReasonRequest request){
        return new ResponseEntity<>(reportReasonService.addReportReason(request), HttpStatus.CREATED);
    }

    @PutMapping("/v1/reportReason")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update a report reason",
                description = "This method update severity or set validity end date",
                tags = {"ReportReason"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report reason successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid end date"),
            @ApiResponse(responseCode = "404", description = "Report reason not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ReportReasonResponse> updateReportReason(@RequestBody @Valid ReportReasonRequest request){
        return ResponseEntity.ok(reportReasonService.updateReportReason(request));
    }


    @Operation(summary = "Valid reason list",
            description = "This method get the valid reasons",
            tags = {"ReportReason"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/v1/reportReason")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<List<ReportReasonResponse>> getValidReasons(){
        return ResponseEntity.ok(reportReasonService.getValidReasons());
    }

}
