package com.rentzy.controller;

import com.rentzy.enums.user.UserRole;
import com.rentzy.exception.ResourceNotFoundException;
import com.rentzy.exception.UserAlreadyExistsException;
import com.rentzy.model.dto.FilterParams;
import com.rentzy.model.dto.request.UserRequestDTO;
import com.rentzy.model.dto.response.UserResponseDTO;
import com.rentzy.service.UserService;
import com.rentzy.utils.PageableUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

}
