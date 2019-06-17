package gui.components.menu.popup.popupitem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import gui.components.modal.BookmarkModal;
import lib.browser.Bookmark;
import lib.browser.BookmarkList;
import lib.browser.Browser;

public class BookmarkPopupItem extends JMenuItem {
    public BookmarkPopupItem() {
        setText("Favoritos");
        setEvents();
    }

    public void setEvents() {
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                BookmarkModal bookmarkModal = new BookmarkModal();
                BookmarkList bookmarkList = Browser.getInstance().getWindow().getBookmarkList();
                for (Object object : bookmarkList) {
                    Bookmark b = (Bookmark) object;
                    bookmarkModal.addRow(b.getId(), b.getName(), b.getLocation().getUrl());
                }
                bookmarkModal.setVisible(true);
            }
        });
        
    }
}