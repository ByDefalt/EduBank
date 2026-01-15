package com.example.clientAPI.entity;

public class AccountEntity {

    private Integer id;        // identifiant auto-increment
    private String username;   // nom d'utilisateur
    private String password;   // mot de passe (hashé)
    private String nom;        // nom de famille
    private String prenom;     // prénom
    private String email;      // email (unique)

    // ----------------- Getters & Setters -----------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ----------------- Optionnel : toString -----------------
    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
        // volontairement sans le password
    }
}
