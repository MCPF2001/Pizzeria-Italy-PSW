package com.progettopswcp.ProgettoPSW.controllers;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check")
public class CheckController {
    @CrossOrigin(
            origins = "http://localhost:4200",
            allowedHeaders = "*",
            methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
    )



    @GetMapping("/simple")
    public ResponseEntity checkSimple() {
        return new ResponseEntity("Check status ok! - User", HttpStatus.OK);
    }


    @GetMapping("/logged")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity checkLogged() {
        return new ResponseEntity("Check status ok, hi Admin" + "!", HttpStatus.OK);
    }


}