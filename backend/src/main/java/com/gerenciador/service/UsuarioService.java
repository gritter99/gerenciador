package com.gerenciador.service;

import java.util.UUID;

import com.gerenciador.dto.RegisterRequest;
import com.gerenciador.entity.Usuario;
import com.gerenciador.exception.CustomException;
import com.gerenciador.repository.UsuarioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioService {
    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PasswordService passwordService;

    public Usuario getUsuarioById(UUID idUsuarioFromToken) {
        return usuarioRepository.findById(idUsuarioFromToken);
    }

    @Transactional
    public void saveUsuario(RegisterRequest usuarioRequest) {

        var isInvalidEmail = usuarioRepository.find("email", usuarioRequest.getEmail()).count() > 0;
        if (isInvalidEmail) {
            throw new CustomException("E-mail já cadastrado");
        }
        var usuario = new Usuario();
        usuario.setNome(usuarioRequest.getNome());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenhaHash(passwordService.hash(usuarioRequest.getSenha()));
        usuarioRepository.persist(usuario);
    }
}
