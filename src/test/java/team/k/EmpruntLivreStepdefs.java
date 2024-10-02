package team.k;

import fr.unice.polytech.biblio.Bibliotheque;
import fr.unice.polytech.biblio.Etudiant;
import fr.unice.polytech.biblio.Livre;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ph. Collet
 */
public class EmpruntLivreStepdefs {
    Bibliotheque biblio = new Bibliotheque();
    Etudiant etudiant;


    @Given("une bibliothèque avec un etudiant de nom {string} et de noEtudiant {int}")
    public void uneBibliothequeAvecUnEtudiantDeNomEtDeNoEtudiant(String nom, int ident) {
        etudiant = new Etudiant();
        etudiant.setNom(nom);
        etudiant.setNoEtudiant(ident);
        biblio.addEtudiant(etudiant);
    }


    @Given("un etudiant de nom {string} et de noEtudiant {int}")
    public void etantDonneUnEtudiant(String nomEtudiant, Integer noEtudiant)  // besoin de refactorer int en Integer car utilisation de la généricité par Cucumber Java 8
    {
        etudiant = new Etudiant();
        etudiant.setNom(nomEtudiant);
        etudiant.setNoEtudiant(noEtudiant);
        biblio.addEtudiant(etudiant);
    }
    @And("un livre de titre {string}")
    public void eUnLivre(String titreLivre) {
        Livre livre = new Livre(titreLivre);
        biblio.addLivre(livre);
    }

    @And("un livre de titre {string} en deux exemplaires")
    public void unLivreDeTitreEnDeuxExemplaires(String name) {
        eUnLivre(name);
        eUnLivre(name);
    }



    @When("{string} emprunte le livre {string}")
    public void WhenEmprunte(String nomEtudiant, String titreLivre)  {
        etudiant = biblio.getEtudiantByName(nomEtudiant);
        Optional<Livre> livreOptional = biblio.getLivreDisponibleByTitle(titreLivre);
        livreOptional.ifPresent(livre -> biblio.emprunte(etudiant, livre));
    }
    @And("Il y a le livre {string} dans un emprunt de la liste d'emprunts")
    public void etLivreDejaEmprunte(String titreLivre) {
        assertTrue(etudiant.getEmprunts().stream().
                anyMatch(emp -> emp.getLivreEmprunte().getTitre().equals(titreLivre)));
    }
    @And("Le livre {string} est indisponible")
    public void etLivreDispo(String titreLivre)  {
        assertFalse(biblio.getLivreDisponibleByTitle(titreLivre).isPresent());
    }



    @When("{string} rend le livre {string}")
    public void rendreLivre(String nomEtudiant, String titreLivre) {
        etudiant = biblio.getEtudiantByName(nomEtudiant);
        Livre livre = etudiant.getEmpruntFor(titreLivre).getLivreEmprunte();
        biblio.rend(livre);
    }

    @Then("Le livre {string} est disponible")
    public void leLivreEstDisponible(String titreLivre) {
        assertTrue( biblio.getLivreDisponibleByTitle(titreLivre).isPresent());
    }


    @Then("Il y a {int} dans son nombre d'emprunts")
    public void ilYADansSonNombreDEmprunts(int nombredEmprunts) {
        assertEquals(nombredEmprunts, etudiant.getNombreDEmprunts());
    }

    @Given("{string} a emprunté le livre {string}")
    public void aEmprunteLeLivre(String nomEtudiant, String nomLivre) {
        Etudiant e = biblio.getEtudiantByName(nomEtudiant);
        Livre l = biblio.getLivreDisponibleByTitle(nomLivre).get();
        biblio.emprunte(e,l);
    }
}
