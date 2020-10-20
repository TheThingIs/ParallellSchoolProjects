package View;

import Model.EmployeeCertificate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * @author Oliver Andersson
 * Visual representation of an EmployeeCertificate. Is viewed in a ListView
 * @since 2020-10-07
 */
public class EmployeeCertificateObject extends AnchorPane {
    @FXML
    private Label name, expiryDate;
    @FXML
    protected CheckBox checked;
    EmployeeCertificate certificate;

    public EmployeeCertificateObject(EmployeeCertificate certificate) {
        this.certificate = certificate;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EmployeeCertificateObject.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fixLabels();
    }

    private void fixLabels() {
        this.name.setText(certificate.getCertificateName());
        this.expiryDate.setText(certificate.getExpiryDateAsString());
    }
}