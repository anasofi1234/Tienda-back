package com.TiendaPeluches.carrito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.TiendaPeluches.carrito.dto.UsuarioDTO;
import com.TiendaPeluches.carrito.entity.Usuario;
import com.TiendaPeluches.carrito.exception.LoginException;
import com.TiendaPeluches.carrito.exception.UsuarioNoEncontradoException;
import com.TiendaPeluches.carrito.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setTelefono(usuarioDTO.getTelefono());

        // Encriptar la contraseña real del registro
        String raw = usuarioDTO.getContrasena();
        usuario.setContrasena(passwordEncoder.encode(raw));

        Usuario saved = usuarioRepo.save(usuario);
        return UsuarioDTO.fromEntity(saved);
    }



    // Opcional: si alguna parte del código llama a este login, usar PasswordEncoder.matches
    @Override
    public UsuarioDTO login(String correo, String password) {
        Usuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new LoginException("Correo no existe"));

        if (!passwordEncoder.matches(password, usuario.getContrasena()))
            throw new LoginException("Contraseña incorrecta");

        return UsuarioDTO.fromEntity(usuario);
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con id " + id));
        return UsuarioDTO.fromEntity(usuario);
    }
}

