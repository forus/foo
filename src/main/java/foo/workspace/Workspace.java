package foo.workspace;

import foo.changes.*;
import foo.model.*;

import java.util.*;

public class Workspace {
    public static final int MAX_PROBLEMS = 10;

    private final Map<String, Node> nodes = new HashMap<>();
    private final List<Change> appliedChanges = new ArrayList<>();
    private final List<ProjectNode> projects = new ArrayList<>();

    private final ChangeVisitor<Problem> changeVisitor = new ChangeVisitor<Problem>() {
        @Override
        public Problem visitCreateProject(CreateProject createProject) {
            ProjectNode projectNode = new ProjectNode();
            projectNode.setId(createProject.getId());
            projectNode.setName(createProject.getName());
            addNode(projectNode);
            return null;
        }

        @Override
        public Problem visitCreatePackage(CreatePackage createPackage) {
            Node parent = node(createPackage.getParentId());
            PackageNode packageNode = new PackageNode();
            packageNode.setName(createPackage.getName());
            packageNode.setId(createPackage.getId());
            addNode(packageNode);
            if (parent instanceof ProjectNode) {
                ((ProjectNode) parent).getPackages().add(packageNode);
            } else if(parent instanceof PackageNode) {
                ((PackageNode) parent).getItems().add(packageNode);
            }
            return null;
        }

        @Override
        public Problem visitCreateFunction(CreateFunction createFunction) {
            PackageNode parent = node(createFunction.getParentId());
            FunctionNode functionNode = new FunctionNode();
            functionNode.setName(createFunction.getName());
            functionNode.setId(createFunction.getId());
            addNode(functionNode);
            parent.getItems().add(functionNode);
            return null;
        }

        @Override
        public Problem visitCreateParameter(CreateParameter createParameter) {
            FunctionNode parent = node(createParameter.getParentId());
            ParameterNode parameterNode = new ParameterNode();
            parameterNode.setName(createParameter.getName());
            parameterNode.setId(createParameter.getId());
            addNode(parameterNode);
            parent.getParameters().add(parameterNode);
            return null;

        }
    };

    public List<ProjectNode> getProjects() {
        return projects;
    }

    public List<Problem> apply(List<Change> changes) {
        List<Problem> problems = new ArrayList<>();

        for (Change change: changes) {
            Problem problem = change.accept(changeVisitor);
            if (problem != null) {
                problems.add(problem);
                if (problems.size() == MAX_PROBLEMS) {
                    break;
                }
            }
        }

        if (problems.isEmpty()) { // TODO: Make a distinction between warnings and errors
            appliedChanges.addAll(changes);
        } else {
           restore();
        }
        return problems;
    }

    private void restore() {
        nodes.clear();
        projects.clear();

        List<Change> changes =new ArrayList<>(appliedChanges);
        appliedChanges.clear();
        apply(changes);
    }

    public <T extends Node> T node(String id) {
        return (T) nodes.get(id);
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        if (node instanceof ProjectNode) {
            projects.add((ProjectNode) node);
        }
    }
}
