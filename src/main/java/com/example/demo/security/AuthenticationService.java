package com.example.demo.security;


import com.example.demo.entities.TokenBlackList;
import com.example.demo.entities.Utente;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.UserNotConfirmedException;
import com.example.demo.repositories.ComuneRepository;
import com.example.demo.repositories.UtenteRepository;
import com.example.demo.request.AuthenticationRequest;
import com.example.demo.request.RegistrationRequest;
import com.example.demo.response.AuthenticationResponse;
import com.example.demo.services.EmailService;
import com.example.demo.services.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenBlackListService tokenBlackListService;
    @Autowired
    private ComuneRepository comuneRepository;
    @Autowired
    private EmailService emailService;

    @Transactional
    public AuthenticationResponse register(RegistrationRequest registrationRequest) {

        var user = Utente.builder()
                .nome(registrationRequest.getNome())
                .cognome(registrationRequest.getCognome())
                .email(registrationRequest.getEmail())
                .role(Role.GUEST)
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .saldo(registrationRequest.getSaldo())
                .build();
        var jwtToken = jwtService.generateToken(user);
        user.setRegistrationToken(jwtToken);
        utenteRepository.saveAndFlush(user);

        sendConfirmationEmail(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void sendConfirmationEmail(Utente utente) {
        String url = "http://localhost:8080/auth/confirm?id=" + utente.getId() + "&token=" + utente.getRegistrationToken();
        String text = "Clicca per confermare la registrazione: " + url;
        emailService.sendEmail(utente.getEmail(), "Conferma", text);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws UserNotConfirmedException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));
        var user = utenteRepository.findByEmail(authenticationRequest.getEmail());
        if (user.getRole().equals(Role.GUEST)) {
            throw new UserNotConfirmedException();
        }
        var jwtToken = jwtService.generateToken(user);
        if (tokenBlackListService.tokenNotValidFromUtenteById(user.getId()).contains(jwtToken)) {
            String email = jwtService.extractUsername(jwtToken);
            // Carica l'utente dal database
            UserDetails userDetails = utenteRepository.findByEmail(email);

            // Genera un nuovo token con le informazioni aggiornate
            String newToken = jwtService.generateToken(userDetails);
            return AuthenticationResponse.builder().token(newToken).build();
        }
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void logout(HttpServletRequest httpRequest, Long id) {
        String token = extractTokenFromRequest(httpRequest);
        TokenBlackList tokenBlackList = TokenBlackList.builder()
                .utente(utenteRepository.getReferenceById(id))
                .token(token)
                .build();
        tokenBlackListService.createTokenBlackList(tokenBlackList);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public boolean confirmRegistration (Long id, String token) {
        Utente utente = utenteRepository.getReferenceById(id);
        if (utente.getRegistrationToken().equals(token)) {
            utente.setRole(Role.UTENTE);
            utenteRepository.saveAndFlush(utente);
            return true;
        }
        return false;
    }
}
