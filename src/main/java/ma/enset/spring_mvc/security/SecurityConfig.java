package ma.enset.spring_mvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;// @Configuration : indique que cette classe contient des configurations Spring
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// Permet d'activer la sécurité au niveau des méthodes
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// Active la sécurité Web de Spring Security
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// BCrypt = algorithme pour chiffrer les mots de passe
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// Stocke les utilisateurs en mémoire
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    // Déclare un bean géré par Spring
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // On utilise BCrypt pour encoder les mots de passe
    }

    @Bean  // Un bean qui stocke les utilisateurs en mémoire
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        PasswordEncoder encoder = passwordEncoder();
        // On récupère l’encodeur de mot de passe pour chiffrer nos mots de passe
        System.out.println("==========");
        String encodedPWD = encoder.encode("123456");
        System.out.println(encodedPWD);
        System.out.println("==========");
        return new InMemoryUserDetailsManager(
                // Création de 3 utilisateurs en mémoire
                User.withUsername("user1").password(passwordEncoder().encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder().encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder().encode("1234")).roles("USER","ADMIN").build()
        );
    }
    @Bean
    // Configure toute la sécurité HTTP
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(fl->fl
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .permitAll())
                // Active le formulaire d’authentification personnalisé
                // .loginPage("/login") = la page de login se trouve à /login
                // .permitAll() : tout le monde peut y accéder sans être connecté
                .csrf(Customizer.withDefaults())// Active la protection CSRF (sécurité standard)
                //.authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
                //.authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar->ar.requestMatchers("/public/**","/webjars/**").permitAll())
                // Tout ce qui est dans /public et les ressources webjars sont accessibles à tous
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .exceptionHandling(eh->eh.accessDeniedPage("/notAuthorized"))
                .build(); // Retourne l'objet SecurityFilterChain final

    }
}

