package com.rentzy.controller;

import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.model.dto.FilterParams;
import com.rentzy.model.dto.request.NotificationRequestDTO;
import com.rentzy.service.NotificationService;
import com.rentzy.utils.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

}
