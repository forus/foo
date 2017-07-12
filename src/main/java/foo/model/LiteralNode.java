package foo.model;

import foo.utils.LiteralParser;

public class LiteralNode extends Node {
    private final String text;
    private final Object value;


    public LiteralNode(String text) {
        this(text, LiteralParser.parse(text));
    }

    private LiteralNode(String text, Object value) {
        this.text = text;
        this.value = LiteralParser.parse(text);
    }


    public String getText() {
        return text;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitLiteral(this);
    }

    public static LiteralNode ofValue(Object value) {
        return new LiteralNode(LiteralParser.toString(value), value);
    }
}
