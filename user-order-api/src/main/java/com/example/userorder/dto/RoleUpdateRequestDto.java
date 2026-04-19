package com.example.userorder.dto;

import com.example.userorder.entity.Role;

public class RoleUpdateRequestDto {
    private Role role;

    public Role getRole(){ return role; }
    public void setRole(Role role){ this.role = role; }
}
