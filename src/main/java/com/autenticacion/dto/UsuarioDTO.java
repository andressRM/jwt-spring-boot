package com.autenticacion.dto;

import com.autenticacion.modelos.NombreRol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsuarioDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String nombreUsuario;

    @NotNull
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
    @JsonIgnore 
    private String clave;

    @NotNull
    private NombreRol rol;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) 
    private String token;


    public UsuarioDTO() {
    }

    // 🔹 Constructor con parámetros
    public UsuarioDTO(Long id, String nombreUsuario, String clave, NombreRol rol, String token) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.setClave(clave); 
        this.token = token;
    }

    // 🔹 Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }


    public void setClave(String clave) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.clave = passwordEncoder.encode(clave);
    }

    public NombreRol getRol() {
        return rol;
    }

    public void setRol(NombreRol rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
