package foo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UnboundCallNode extends Node implements ValueHolder<Node>, ListLike {
    private Node function;
    private final List<Node> items = new ArrayList<>();

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitUnboundCall(this);
    }

    @Override
    public Node getValue() {
        return function;
    }

    @Override
    public void setValue(Node value) {
        function = value;
    }
}
