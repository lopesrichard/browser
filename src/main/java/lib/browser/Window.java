package lib.browser;

import gui.WindowPanel;
import gui.components.interaction.Dialog;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import lib.net.URLReader;
import lib.database.repositories.BookmarkRepository;
import lib.database.repositories.HistoryRepository;
import lib.document.Document;
import lib.document.element.html.HTMLParser;
import lib.document.element.html.HTMLRenderer;
import lib.net.UrlComplete;

public class Window {
    private String title = "Sem Título";
    private WindowPanel panel = new WindowPanel();
    private HistoryList history = new HistoryList();
    private BookmarkList bookmarks = new BookmarkList();
    private Navigation navigation = new Navigation();
    private Document document;

    public Window(String title) {
        this.title = title;
        loadHistory();
        loadBookmarks();
    }

    public void loadHistory() {
        try {
            HistoryRepository.all().forEach(lc -> history.add(lc));
        } catch (SQLException e) {
            Dialog.showMessage(
                "error",
                "Atenção",
                "Houve um problema ao carregar o histórico de navegação"
            );
        }
    }

    public void loadBookmarks() {
        try {
            BookmarkRepository.all().forEach(bm -> bookmarks.add(bm));
        } catch (SQLException e) {
            Dialog.showMessage(
                "error",
                "Atenção",
                "Houve um problema ao carregar os favoritos"
            );
        }

    }

    public void open(String url) {
        try {
            setLocation(url);
            go(url);
        } catch (IOException e) {
            Dialog.showMessage(
                "error",
                "Atenção",
                "Não foi possível realizar a leitura, url inválida"
            );
        }
    }

    public void go(String url) throws IOException {
        parse(read(url));
        revalidate();
        render();
        register();
    }

    public void back() {
        navigation.backward();
        reload();
    }

    public void next() {
        navigation.forward();
        reload();
    }

    public void reload() {
        try {
            go(navigation.getLocation().getUrl());
        } catch (IOException e) {
            Dialog.showMessage(
                "error",
                "Atenção",
                "Ocorreu um erro ao avançar, por favor recarregue a página"
            );
        }
    }

    private String read(String url) throws IOException {
        URLReader urlReader = new URLReader(url);
        return urlReader.read();
    }

    private void parse(String html) {
        HTMLParser parser = new HTMLParser();
        parser.parse(html);
        document = parser.getDocument();
    }

    public void setLocation(String url) {
        navigation.current(new Location(
            null,
            UrlComplete.complete(url),
            LocalDateTime.now()
        ));
    }

    private void revalidate() {
        panel.getMenu().setUrlTextContent(navigation.getLocation().getUrl());
    }

    private void render() {
        HTMLRenderer renderer = new HTMLRenderer(panel.getPage());
        renderer.render(document);
    }

    private void register() {
        navigation.getLocation().setTitle(title);

        if (Browser.getInstance().isIncognito()) {
            return;
        }

        try {
            history.insert(
                new History(0, Browser.getInstance().getUser().getId(), navigation.getLocation())
            );
        } catch (SQLException e) {
            System.out.println("Erro ao salvar informações no histórico");
        }
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public HistoryList getHistory() {
        return history;
    }

    public void setHistory(HistoryList history) {
        this.history = history;
    }

    public BookmarkList getBookmarkList() {
        return bookmarks;
    }

    public void setBookmarkList(BookmarkList bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Navigation getPagination() {
        return navigation;
    }

    public void setPagination(Navigation navigation) {
        this.navigation = navigation;
    }

    public WindowPanel getPanel() {
        return panel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        Browser.getInstance().changeSelectedTabTitle(title);
    }

}
