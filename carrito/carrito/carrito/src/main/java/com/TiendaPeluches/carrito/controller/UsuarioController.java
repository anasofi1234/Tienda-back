package com.TiendaPeluches.carrito.controller;

import java.security.Principal;
import java.util.Map;

import com.TiendaPeluches.carrito.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.TiendaPeluches.carrito.dto.UsuarioDTO;
import com.TiendaPeluches.carrito.entity.Usuario;
import com.TiendaPeluches.carrito.repository.UsuarioRepository;
import com.TiendaPeluches.carrito.service.UsuarioService;
import org.springframework.security.core.AuthenticationException;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UsuarioController {

    @Autowired
    private UsuarioService servicio;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO creado = servicio.crearUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * LOGIN + CREA SESIÓN + GUARDA AUTHENTICATION
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        try {
            String correo = body.get("correo");
            String password = body.get("password");

            // 1️⃣ Autenticación (Spring Security valida contra la BD)
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correo, password)
            );

            // 2️⃣ Usuario autenticado correctamente
            Usuario usuario = usuarioRepo.findByCorreo(correo)
                    .orElseThrow();

            // 3️⃣ Generar JWT
            String token = jwtUtil.generarToken(correo);

            // 4️⃣ Respuesta: usuario + token
            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "usuario", UsuarioDTO.fromEntity(usuario)
                    )
            );

        } catch (AuthenticationException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }
    }
    
    /**
     * PERFIL AUTENTICADO
     */
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> perfil(Authentication auth) {

        String correo = auth.getName();

        Usuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow();

        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }


    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable Long id) {
        return servicio.obtenerPorId(id);
    }
}
