package foo.ide;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import foo.changes.Change;
import foo.model.*;
import foo.repository.ChangeRepository;
import foo.workspace.Workspace;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class App extends Application {
    private static final YAMLFactory yamlFactory = new YAMLFactory();
    private static final ObjectMapper mapper = new ObjectMapper(yamlFactory);
    private static final File SETTINGS_FILE = new File("./application.yaml");

    private Settings settings;
    private Workspace workspace = new Workspace();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadSettings();  // TODO: async
        loadWorkspace(); // TODO: async

        SplitPane splitPane = new SplitPane();

        PackageTreeView packageTree = new PackageTreeView(workspace);
        packageTree.setShowRoot(false);
        ListView<NamedNode> functionList = new ListView<>();

        packageTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillFunctionList(functionList, newValue);
        });

        splitPane.getItems().add(packageTree);
        splitPane.getItems().add(functionList);

        primaryStage.setScene(new Scene(splitPane, 640, 480));
        primaryStage.show();
    }

    private void fillFunctionList(ListView<NamedNode> functionList, TreeItem<NamedNode> newValue) {
        NamedNode node = newValue.getValue();

        if (node instanceof PackageNode) {
            PackageNode packageNode = (PackageNode) node;
            functionList.getItems().clear();

            for (NamedNode item: packageNode.getItems()) {
                if (item instanceof FunctionNode) {
                    functionList.getItems().add(item);
                }
            }
        }




    }

    @Override
    public void stop() throws Exception {
        saveSettings(); // TODO: async
        super.stop();
    }

    private void loadSettings() {
        try {
            settings = mapper.readValue(SETTINGS_FILE, Settings.class);
        } catch (IOException e) {
            settings = new Settings();
        }
    }

    private void saveSettings() {
        SETTINGS_FILE.delete();
        try {
            mapper.writeValue(SETTINGS_FILE, settings);
        } catch (IOException e) {
        }
    }

    private void loadWorkspace() {
        if (settings.getProject() != null) {
            try {
                List<Change> changes = ChangeRepository.fetch(settings.getProject());
                workspace.apply(changes);
            } catch (IOException e) {
                // TODO:
            }
        }
    }
}
