package litheraa.view.util;

import litheraa.SettingsController;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class DoubleFilter extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offs, int length,
                        String string, AttributeSet a) throws BadLocationException {
        String text = "";
        text += string;
        if ((isChars() && text.matches("^[0-9]+$")) ||
                (!isChars() && text.matches("^[0-9]+[.,]?[0-9]{0,3}$"))) {
            super.replace(fb, offs, length, string, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean isChars() {
        return SettingsController.isChars();
    }
}