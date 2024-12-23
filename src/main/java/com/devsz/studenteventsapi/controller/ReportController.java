// package com.devsz.studenteventsapi.controller;

// import com.devsz.studenteventsapi.entity.ReportEntity;
// import com.devsz.studenteventsapi.service.IReportService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/reports")
// @RequiredArgsConstructor
// public class ReportController {

//     //@Autowired
//     private final IReportService service;

//     @GetMapping
//     public ResponseEntity<List<ReportEntity>> readAll() throws Exception {
//         return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
//     }

//     @PostMapping
//     public ResponseEntity<ReportEntity> create(@RequestBody ReportEntity reportEntity) throws Exception {
//         return new ResponseEntity<>(service.save(reportEntity), HttpStatus.CREATED);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<ReportEntity> update(@PathVariable(value = "id") String id, @RequestBody ReportEntity reportEntity) throws Exception {
//         return new ResponseEntity<>(service.update(reportEntity, id), HttpStatus.OK);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> delete(@PathVariable(value = "id") String id) throws Exception{
//         service.delete(id);
//         return new ResponseEntity<>(null, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<ReportEntity> readById(@PathVariable(value = "id") String id) throws Exception {
//         return new ResponseEntity<>(service.readById(id), HttpStatus.OK);
//     }

// }
