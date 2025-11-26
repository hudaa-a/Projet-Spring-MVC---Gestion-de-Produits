package ma.enset.spring_mvc.web;

import jakarta.servlet.http.HttpServletRequest; // Permet de manipuler la session utilisateur
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;// Pour valider les données d’un formulaire
import ma.enset.spring_mvc.entities.Product;// L’entité Product
import ma.enset.spring_mvc.repository.ProductRepository;// Le Repository qui permet d'accéder à la base de données
import org.springframework.beans.factory.annotation.Autowired;// Injection automatique des dépendances
import org.springframework.security.access.prepost.PreAuthorize;// Permet de sécuriser les méthodes avec @PreAuthorize
import org.springframework.stereotype.Controller;// Indique que cette classe est un contrôleur MVC
import org.springframework.ui.Model;// "Model" sert à envoyer des données vers la vue
import org.springframework.validation.BindingResult;// Récupère les erreurs de validation après @Valid
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// Pour récupérer les paramètres envoyés dans les requêtes

import java.util.List;

@Controller // Cette annotation indique que cette classe est un contrôleur Spring MVC
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired// Injection automatique du repository - Spring va créer une instance de ProductRepository
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // Page d'accueil - route racine
    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }

    /**
     * Login page
     */
    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    // Page d'erreur d'autorisation
    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        // Retourne la vue templates/notAuthorized.html
        return "NotAuthorized";
    }

    /**
     * Logout - invalidates session and redirects to login
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // ==================== USER ROUTES ====================

    /**
     * Display all products (accessible to users with USER role)
     */
    @GetMapping("/user/index")
    @PreAuthorize("hasRole('USER')")
    public String displayProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("productsList", products);
        return "Products";
    }

    // ==================== ADMIN ROUTES ====================

    // Affiche le formulaire de création de produit
    @GetMapping("/admin/newProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "NewProduct";
    }

    // Traite l'envoi du formulaire de création
    @PostMapping("/admin/saveProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveProduct(@Valid Product product,// @Valid active la validation sur l'objet Product
                              BindingResult bindingResult,// Contient les erreurs de validation
                              Model model) {

         // Vérifie s'il y a des erreurs de validation
        if (bindingResult.hasErrors()) {
            return "NewProduct";
        }

        // Save product to database
        productRepository.save(product);

        // Redirect to products list with success message (you can add flash attributes later)
        return "redirect:/user/index";
    }

    // Cette méthode répond aux requêtes POST sur /admin/delete
    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@RequestParam("id") Long productId) {
        // @RequestParam récupère le paramètre "id" envoyé dans le formulaire
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        }

        return "redirect:/user/index";
    }

}