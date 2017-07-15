package foo.changes;

import lombok.Value;

@Value
public class CreatePackage extends Change {
    String id;
    String name;
    String parent;

    @Override
    public <T> T accept(ChangeVisitor<T> visitor) {
        return visitor.visitCreatePackage(this);
    }
}
