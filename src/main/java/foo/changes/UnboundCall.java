package foo.changes;

import lombok.*;

@Value
public class UnboundCall extends Change {
    String id;
    Destination destination;

    @Override
    public <T> T accept(ChangeVisitor<T> visitor) {
        return visitor.visitUnboundCall(this);
    }
}
