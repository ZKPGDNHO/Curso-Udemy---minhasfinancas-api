package com.zk.minhasfinancas.api.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.minhasfinancas.api.DTO.AtualizaStatusDTO;
import com.zk.minhasfinancas.api.DTO.LancamentosDTO;
import com.zk.minhasfinancas.exception.RegraNegocioException;
import com.zk.minhasfinancas.model.entity.Lancamentos;
import com.zk.minhasfinancas.model.entity.StatusLancamento;
import com.zk.minhasfinancas.model.entity.TipoLancamento;
import com.zk.minhasfinancas.model.entity.Usuario;
import com.zk.minhasfinancas.service.LancamentosService;
import com.zk.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentosController {

    private LancamentosService service;
    private UsuarioService usuarioService;

    public LancamentosController(LancamentosService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario) {

        Lancamentos lancamentosFiltro = new Lancamentos();
        lancamentosFiltro.setDescricao(descricao);
        lancamentosFiltro.setMes(mes);
        lancamentosFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.BuscarById(idUsuario);

        if (!usuario.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario nao encontrado para id informada");

        } else {
            lancamentosFiltro.setUsuario(usuario.get());

        }

        List<Lancamentos> lancamentos = service.buscar(lancamentosFiltro);

        return ResponseEntity.ok(lancamentos);

    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentosDTO dto) {

        try {
            Lancamentos entidade = converter(dto);
            entidade = service.salvar(entidade);

            return new ResponseEntity(entidade, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentosDTO dto) {

        return service.BuscarById(id).map(entity -> {
            try {
                Lancamentos l = converter(dto);
                l.setId(entity.getId());

                service.atualizar(l);

                return ResponseEntity.ok(l);

            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {

        return service.BuscarById(id).map(entity -> {

            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if (statusSelecionado == null) {
                return ResponseEntity.badRequest().body("Nenhum status selecionado, informe um status valido");
            }

            try {

                entity.setStatus(statusSelecionado);
                service.atualizar(entity);

                return ResponseEntity.ok(entity);

            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") long id) {

        return service.BuscarById(id).map(entity -> {

            service.delete(entity);

            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    private Lancamentos converter(LancamentosDTO dto) {
        Lancamentos l = new Lancamentos();

        l.setId(dto.getId());
        l.setDescricao(dto.getDescricao());
        l.setAno(dto.getAno());
        l.setMes(dto.getMes());
        l.setValor(dto.getValor());

        Usuario usuario = usuarioService.BuscarById(dto.getUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuario nao encontrado"));

        l.setUsuario(usuario);

        if (dto.getTipo() != null) {
            l.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if (dto.getStatus() != null) {
            l.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return l;
    }

}
