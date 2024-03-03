package com.jwt.jwt.authentication.controller;

public record AuthResponseDTO(String token, AuthStatus authStatus) {
}
