package gui.components.menu.popup.popupitem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import gui.components.modal.HistoryModal;
import lib.browser.History;
import lib.browser.HistoryList;
import lib.browser.Location;
import lib.browser.Browser;

public class HistoryPopupItem extends JMenuItem {
    public HistoryPopupItem() {
        setText("Histórico");
        setEvents();
    }

    public void setEvents() {
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                HistoryModal historyModal = new HistoryModal();
                HistoryList history = Browser.getInstance().getWindow().getHistory();
                for (Object object : history) {
                    History h = (History) object;
                    Location l = h.getLocation();
                    historyModal.addRow(h.getId(), l.getTitle(), l.getUrl(), l.getDateToShow());
                }
                historyModal.setVisible(true);
            }
        });
        
    }
}