package contact;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private GestionContacts gestionnaire;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private TextField idField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dateAnniversairePicker;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField entrepriseField;
    @FXML
    private ToggleGroup typeContactGroup;
    @FXML
    private RadioButton personnelRadioButton;
    @FXML
    private RadioButton professionnelRadioButton;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button supprimerButton;
    @FXML
    private Button modifierButton;
    @FXML
    private Button rechercherButton;
    @FXML
    private TableView<Contact> contactTableView;
    @FXML
    private TableColumn<Contact, String> idColumn;
    @FXML
    private TableColumn<Contact, String> nomColumn;
    @FXML
    private TableColumn<Contact, String> telephoneColumn;
    @FXML
    private TableColumn<Contact, String> emailColumn;
    @FXML
    private TableColumn<Contact, String> anniversaireColumn;
    @FXML
    private TableColumn<Contact, String> typeColumn;
    @FXML
    private TableColumn<Contact, String> adresseColumn;
    @FXML
    private TableColumn<Contact, String> entrepriseColumn;

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            gestionnaire = new GestionContacts();
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur de connexion", "Impossible de se connecter à la base de données", e.getMessage());
            return;
        }

        // Initialisation des colonnes de la TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTelephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        anniversaireColumn.setCellValueFactory(cellData -> {
            LocalDate dateAnniv = cellData.getValue().getDateAnniversaire();
            return new SimpleStringProperty(dateAnniv != null ? formatter.format(dateAnniv) : "");
        });
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().getType()));

        // Liaison de la colonne adresseColumn
        adresseColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof ContactPersonnel) {
                return new SimpleStringProperty(((ContactPersonnel) cellData.getValue()).getAdresse());
            } else {
                return new SimpleStringProperty("");
            }
        });

        // Liaison de la colonne entrepriseColumn
        entrepriseColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof ContactProfessionnel) {
                return new SimpleStringProperty(((ContactProfessionnel) cellData.getValue()).getNomEntreprise());
            } else {
                return new SimpleStringProperty("");
            }
        });

        contactTableView.setItems(contacts);

        // Initialisation des boutons
        ajouterButton.setOnAction(this::ajouterContact);
        supprimerButton.setOnAction(this::supprimerContact);
        modifierButton.setOnAction(this::modifierContact);
        rechercherButton.setOnAction(this::rechercherContact);

        // Charger les contacts depuis la base de données
        try {
            chargerContacts();
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur de chargement", "Impossible de charger les contacts", e.getMessage());
        }
    }

    private void chargerContacts() throws SQLException {
        contacts.clear();
        List<Contact> allContacts = gestionnaire.listerContacts();
        contacts.addAll(allContacts);
    }

    @FXML
    private void ajouterContact(ActionEvent event) {
        try {
            String id = idField.getText();
            String nom = nomField.getText();
            String numero = telephoneField.getText();
            String email = emailField.getText();
            LocalDate dateAnniv = dateAnniversairePicker.getValue();

            // Vérification des champs obligatoires
            if (id.isEmpty() || nom.isEmpty() || numero.isEmpty() || email.isEmpty() || dateAnniv == null) {
                afficherErreur("Erreur de saisie", "Veuillez remplir tous les champs obligatoires.", "");
                return;
            }

            Contact contact;
            if (personnelRadioButton.isSelected()) {
                String adresse = adresseField.getText();
                contact = new ContactPersonnel(id, nom, numero, email, dateAnniv, adresse);
            } else {
                String entreprise = entrepriseField.getText();
                contact = new ContactProfessionnel(id, nom, numero, email, dateAnniv, entreprise);
            }

            gestionnaire.ajouterContact(contact);
            chargerContacts();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur d'ajout", "Impossible d'ajouter le contact", e.getMessage());
        }
    }

    @FXML
    private void supprimerContact(ActionEvent event) {
        try {
            Contact contact = contactTableView.getSelectionModel().getSelectedItem();
            if (contact != null) {
                gestionnaire.supprimerContact(contact.getId());
                chargerContacts();
            } else {
                afficherErreur("Erreur de suppression", "Aucun contact sélectionné", "Veuillez sélectionner un contact à supprimer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur de suppression", "Impossible de supprimer le contact", e.getMessage());
        }
    }

    @FXML
    private void modifierContact(ActionEvent event) {
        try {
            Contact contact = contactTableView.getSelectionModel().getSelectedItem();
            if (contact != null) {
                String id = idField.getText();
                String nom = nomField.getText();
                String numero = telephoneField.getText();
                String email = emailField.getText();
                LocalDate dateAnniv = dateAnniversairePicker.getValue();

                // Vérification des champs obligatoires
                if (id.isEmpty() || nom.isEmpty() || numero.isEmpty() || email.isEmpty() || dateAnniv == null) {
                    afficherErreur("Erreur de saisie", "Veuillez remplir tous les champs obligatoires.", "");
                    return;
                }

                gestionnaire.modifierContact(id, nom, numero, email, dateAnniv);
                chargerContacts();
                clearFields();
            } else {
                afficherErreur("Erreur de modification", "Aucun contact sélectionné", "Veuillez sélectionner un contact à modifier.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur de modification", "Impossible de modifier le contact", e.getMessage());
        }
    }

    @FXML
    private void rechercherContact(ActionEvent event) {
        String id = idField.getText();
        String nom = nomField.getText();
        String numero = telephoneField.getText();
        String email = emailField.getText();

        try {
            if (!id.isEmpty()) {
                rechercherParId(id);
            } else if (!nom.isEmpty()) {
                rechercherParNom(nom);
            } else if (!numero.isEmpty()) {
                rechercherParNumero(numero);
            } else if (!email.isEmpty()) {
                // TODO: Implémenter la recherche par email
                // rechercherParEmail(email);
            } else {
                chargerContacts();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur de recherche", "Erreur lors de la recherche de contact", e.getMessage());
        }
    }

    private void rechercherParId(String id) throws SQLException {
        Contact contact = gestionnaire.rechercherContactParId(id);
        if (contact != null) {
            contacts.clear();
            contacts.add(contact);
        } else {
            afficherErreurRecherche("Aucun contact trouvé avec l'ID " + id);
        }
    }

    private void rechercherParNom(String nom) throws SQLException {
        Contact contact = gestionnaire.rechercherContactParNom(nom);
        if (contact != null) {
            contacts.clear();
            contacts.add(contact);
        } else {
            afficherErreurRecherche("Aucun contact trouvé avec le nom " + nom);
        }
    }

    private void rechercherParNumero(String numero) throws SQLException {
        Contact contact = gestionnaire.rechercherContactParNumero(numero);
        if (contact != null) {
            contacts.clear();
            contacts.add(contact);
        } else {
            afficherErreurRecherche("Aucun contact trouvé avec le numéro " + numero);
        }
    }

    private void afficherErreurRecherche(String message) {
        contacts.clear();
        afficherErreur("Erreur de recherche", "Contact non trouvé", message);
    }

    private void clearFields() {
        idField.clear();
        nomField.clear();
        telephoneField.clear();
        emailField.clear();
        dateAnniversairePicker.setValue(null);
        adresseField.clear();
        entrepriseField.clear();
        typeContactGroup.selectToggle(null);
    }

    private void afficherErreur(String titre, String entete, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        alert.showAndWait();
    }
}