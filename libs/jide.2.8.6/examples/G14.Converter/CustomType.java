/*
 * @(#)CustomType.java 5/12/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

/**
 * A custom type to demo how to use converter in ListComboBox.
 */
public class CustomType {
    private int _intValue;
    private String _stringValue;

    public CustomType(int intValue, String stringValue) {
        _intValue = intValue;
        _stringValue = stringValue;
    }

    public int getIntValue() {
        return _intValue;
    }

    public void setIntValue(int intValue) {
        _intValue = intValue;
    }

    public String getStringValue() {
        return _stringValue;
    }

    public void setStringValue(String stringValue) {
        _stringValue = stringValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomType) {
            if (((CustomType) obj).getIntValue() == getIntValue() &&
                    ((CustomType) obj).getStringValue().equals(getStringValue())) {
                return true;
            }
        }
        return false;
    }
}
