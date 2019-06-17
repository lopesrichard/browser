package gui.components.menu.popup.popupmenu;

import gui.components.menu.popup.popupitem.BookmarkPopupItem;
import gui.components.menu.popup.popupitem.HistoryPopupItem;

public class SettingsPopupMenu extends PopupMenu {
    BookmarkPopupItem bookmarkPopupItem = new BookmarkPopupItem();
    HistoryPopupItem  historyPopupItem  = new HistoryPopupItem();

    public SettingsPopupMenu() {
        add(bookmarkPopupItem);
        add(historyPopupItem);
    }
}