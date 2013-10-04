package me.lachlanap.balloonbox.core.lctext;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import me.lachlanap.lct.gui.ConstantEditor;
import me.lachlanap.lct.gui.GBC;

/**
 *
 * @author lachlan
 */
public class BooleanEditor extends ConstantEditor {

    private final BooleanConstantField constant;
    private final JCheckBox checkBox;

    public BooleanEditor(BooleanConstantField constant) {
        super(constant);
        this.constant = constant;

        boolean value = constant.get();

        setLayout(new GridBagLayout());

        add(new JLabel(constant.name), new GBC(0, 0, 5));

        checkBox = new JCheckBox(String.valueOf(value), value);
        checkBox.addChangeListener(new CheckBoxChangeListener());
        add(checkBox, new GBC(1, 0, 5).weight(100, 0).fillX());

        setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                     getLayout().preferredLayoutSize(this).height));
    }

    private class CheckBoxChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            boolean val = checkBox.isSelected();
            checkBox.setText(String.valueOf(val));
            constant.set(val);
        }
    }
}
