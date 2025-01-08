package litheraa.view.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class IntegerFilter extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offs, int length,
                        String string, AttributeSet a) throws BadLocationException {
        String text = fb.getDocument().getText(0,
                fb.getDocument().getLength());
        text += string;
        if (text.matches("^[0-9]{1,9}$")) {
            super.replace(fb, offs, length, string, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
