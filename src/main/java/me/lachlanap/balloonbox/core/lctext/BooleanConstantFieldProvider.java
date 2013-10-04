package me.lachlanap.balloonbox.core.lctext;

import me.lachlanap.lct.Constant;
import me.lachlanap.lct.data.ConstantField;
import me.lachlanap.lct.spi.ConstantFieldProvider;

/**
 *
 * @author lachlan
 */
public class BooleanConstantFieldProvider implements ConstantFieldProvider {

    @Override
    public boolean canProvide(Class<?> type) {
        return type == boolean.class;
    }

    @Override
    public ConstantField getField(
            Class<?> type,
            Class<?> container, String field, Constant annot) {
        if (type == boolean.class)
            return new BooleanConstantField(container, field, annot.name());
        throw new UnsupportedOperationException("Cannot create constant of type " + type.getSimpleName());
    }
}
