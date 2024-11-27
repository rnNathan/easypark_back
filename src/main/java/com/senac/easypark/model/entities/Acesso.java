package com.senac.easypark.model.entities;

import com.senac.easypark.model.enums.TipoAcesso;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Acesso implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;

    @Size(min = 0, max = 999)
    private String senha;

    private String email;
    @Enumerated(EnumType.STRING)
    private TipoAcesso tipoAcesso;

    // Construtores, getters e setters
    public Acesso(String username, String senha, String email, TipoAcesso tipoAcesso) {
        this.username = username;
        this.senha = senha;
        this.tipoAcesso = tipoAcesso;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority(tipoAcesso.toString()));

        return list;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

}