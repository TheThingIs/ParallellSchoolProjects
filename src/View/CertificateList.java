package View;

import Model.Admin;
import Model.Certificate;
import Model.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.Iterator;

/**
 * @author Oliver Andersson
 * Root node for certificate-tab. Creation and deletion of certificates in done through here
 * @since 2020-10-07
 */

public class CertificateList extends AnchorPane implements Observer {
    @FXML
    private ListView<CertificateObject> listOfCertificates;
    @FXML
    private Button create, delete, save;
    @FXML
    private TextField name;
    @FXML
    private Label iD;
    private CertificateObject selected;

    /**
     * Constructs a Certificates pane and loads in certificates from Admin
     */
    public CertificateList() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CertificateList.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadCertificates();
        setButtons();
        setText();
        Admin.getInstance().addObserver(this);

    }

    private void setText() {
        name.setEditable(false);
    }

    private void setButtons() {
        create.setOnAction(actionEvent -> {
            name.setEditable(true);
            name.setText("");
            save.setDisable(false);
        });
        save.setOnAction(actionEvent -> {
            Admin.getInstance().createCertificate(name.getText());
            name.setText("");
            save.setDisable(true);
        });
        save.setDisable(true);
        delete.setDisable(true);
        delete.setOnAction(actionEvent -> {
            for (CertificateObject certificateObject : listOfCertificates.getItems()) {
                if (certificateObject.checked.isSelected())
                    Admin.getInstance().deleteCertificate(certificateObject.certificate);
            }
            delete.setDisable(true);
        });
    }

    private void loadCertificates() {
        listOfCertificates.getItems().clear();
        Iterator<Certificate> certificateIterator = Admin.getInstance().getCertificatehandler().getAllCertificates();
        while (certificateIterator.hasNext()) {
            CertificateObject tmp = new CertificateObject(certificateIterator.next());
            tmp.checked.setOnMouseClicked(mouseEvent -> delete.setDisable(!tmp.checked.isSelected()));
            tmp.setOnMouseClicked(mouseEvent -> clicked(tmp));
            listOfCertificates.getItems().add(tmp);
        }
    }

    private void clicked(CertificateObject certificateObject) {
        this.selected = certificateObject;
        name.setText(selected.certificate.getName());
        iD.setText(Long.toString(selected.certificate.ID));
    }

    @Override
    public void update() {
        loadCertificates();
    }
}