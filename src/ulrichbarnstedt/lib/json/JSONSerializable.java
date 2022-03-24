package ulrichbarnstedt.lib.json;

public interface JSONSerializable {
    default String toJSON () {
        return this.toJSON(0);
    }

    String toJSON (int indent);
}
