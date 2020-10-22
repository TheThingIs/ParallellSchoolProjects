package main.java.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.java.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    @FXML
    private ListView<String> listNames;
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
            listNames.getItems().clear();
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
            boolean[] arr = new boolean[listOfCertificates.getItems().size()];
            for (int index = listOfCertificates.getItems().size() - 1; index >= 0; index--) {
                arr[index] = listOfCertificates.getItems().get(index).checked.isSelected();
            }
            for (int index = listOfCertificates.getItems().size() - 1; index >= 0; index--) {
                if (arr[index]) {
                    Admin.getInstance().deleteCertificate(listOfCertificates.getItems().get(index).certificate);

                }
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
        listNames.getItems().clear();
        listNames.getItems().addAll(getEmployeesWithCertificate(selected.certificate));
    }

    private List<String> getEmployeesWithCertificate(Certificate certificate) {
        List<String> allNames = new ArrayList<>();

        Iterator<Employee> names = CertificateHandler.getInstance().getEmployeeWithCertificate(certificate);
        while (names.hasNext()) {
            Employee employee = names.next();
            allNames.add(employee.getName());
        }
        if (allNames.size() == 0) {
            allNames.add("Det finns inga anställda");
            allNames.add("med detta certifikatet");
        }
        return allNames;
    }

    @Override
    public void update() {
        loadCertificates();
    }
}