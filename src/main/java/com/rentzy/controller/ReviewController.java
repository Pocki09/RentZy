package com.rentzy.controller;

import com.rentzy.model.dto.FilterParams;
import com.rentzy.model.dto.request.ReviewRequestDTO;
import com.rentzy.service.ReviewService;
import com.rentzy.utils.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/reviews")
@Validated
public class ReviewController {

}
