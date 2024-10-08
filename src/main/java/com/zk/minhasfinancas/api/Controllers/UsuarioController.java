package com.zk.minhasfinancas.api.Controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zk.minhasfinancas.api.DTO.UsuarioDTO;
import com.zk.minhasfinancas.exception.ErroAutenticacao;
import com.zk.minhasfinancas.exception.RegraNegocioException;
import com.zk.minhasfinancas.model.entity.Usuario;
import com.zk.minhasfinancas.service.LancamentosService;
import com.zk.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private UsuarioService service;
    private LancamentosService lancamentosService;

    public UsuarioController(UsuarioService service, LancamentosService lancamentosService) {
        this.service = service;
        this.lancamentosService = lancamentosService;
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);

        } catch (ErroAutenticacao e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity save(@RequestBody UsuarioDTO dto) {

        Usuario user = new Usuario(null, dto.getName(), dto.getEmail(), dto.getSenha());

        try {
            Usuario userSalvo = service.salvarUsuario(user);
            return new ResponseEntity(userSalvo, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id) {

        Optional<Usuario> usuario = service.BuscarById(id);

        if (!usuario.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        BigDecimal saldo = lancamentosService.obterSaldoByUsuario(id);

        return ResponseEntity.ok(saldo);

    }
}
