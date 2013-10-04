package me.lachlanap.balloonbox.core.lctext;

import java.util.Properties;
import me.lachlanap.lct.ConstantSettingException;
import me.lachlanap.lct.data.ConstantField;

/**
 *
 * @author lachlan
 */
public class BooleanConstantField extends ConstantField {

    public BooleanConstantField(
            Class<?> container, String field, String name) {
        super(container, field, name);
    }

    public boolean get() {
        try {
            return container.getField(field).getBoolean(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new ConstantSettingException(this, e);
        }
    }

    public void set(boolean value) {
        try {
            container.getField(field).setBoolean(null, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new ConstantSettingException(this, e);
        }
    }

    /**
     * Loads this constant's settings from the Properties. If they don't exist this will do nothing.
     */
    @Override
    public void loadFromProperties(Properties props) {
        String strValue = props.getProperty(container.getSimpleName() + "." + name);
        if (strValue == null)
            return;

        boolean intValue = Boolean.parseBoolean(strValue);
        set(intValue);
    }

    @Override
    public void saveToProperties(Properties props) {
        String strValue = String.valueOf(get());
        props.setProperty(container.getSimpleName() + "." + name, strValue);
    }
}
