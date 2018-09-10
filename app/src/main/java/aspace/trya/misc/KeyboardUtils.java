package aspace.trya.misc;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(View currentFocus, Object systemService) {
        if (currentFocus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) systemService;
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view, Object systemService) {
        InputMethodManager inputMethodManager = (InputMethodManager) systemService;
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
