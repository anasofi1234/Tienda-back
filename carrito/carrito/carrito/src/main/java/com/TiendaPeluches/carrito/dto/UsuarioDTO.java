package com.TiendaPeluches.carrito.dto;

import java.util.Date;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String contrasena;  // 👈 AGREGADO
    private Date fechaCreacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContrasena() { return contrasena; }   // 👈 AGREGADO
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public static UsuarioDTO fromEntity(com.TiendaPeluches.carrito.entity.Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaCreacion(usuario.getFechaCreacion());

        // ⚠ No devolver la contraseña al frontend por seguridad
        dto.setContrasena(null);

        return dto;
    }
}
