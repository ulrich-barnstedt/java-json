package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.JSONSerializable;

public class Custom implements JSONSerializable {
    @Override
    public String toJSON (int indent) {
        return "test content";
    }
}
