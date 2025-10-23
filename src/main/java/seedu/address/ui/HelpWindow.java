package seedu.address.ui;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    // Classpath resources (pack these under src/main/resources/docs/)
    private static final String CLASSPATH_MD = "/docs/UserGuide.md";
    private static final String CLASSPATH_DOCS_DIR = "/docs/"; // for <base href>

    // Optional dev fallback if you keep docs/ at repo root
    private static final Path FS_MD = Paths.get("docs", "UserGuide.md");

    @FXML
    private WebView webView;

    /**
     * Controller for the Help window that displays the User Guide in a scrollable WebView.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        loadUserGuide();
    }

    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Displays the Help window and centers it on the screen.
     */
    public void show() {
        getRoot().show();
        getRoot().centerOnScreen();
    }

    public boolean isShowing() {
        return getRoot().isShowing();
    }

    public void hide() {
        getRoot().hide();
    }

    public void focus() {
        getRoot().requestFocus();
    }

    private void loadUserGuide() {
        try {
            String md = readUserGuideMarkdown();
            if (md == null) {
                webView.getEngine().loadContent("<h2>UserGuide not found</h2>");
                return;
            }

            // 1) Strip Jekyll front-matter and convert kramdown {:toc} to Flexmark [TOC]
            md = stripJekyllFrontMatter(md);
            md = md.replace("\n{:toc}", "").replace("{:toc}", "");
            // Insert a real [TOC] placeholder where the original table was
            md = md.replaceFirst("(?s)\\*\\s*Table of Contents\\s*", "[TOC]\n\n");

            // replace GitHub/Jekyll emoji shortcodes with Unicode
            md = md.replace(":bulb:", "💡")
                    .replace(":information_source:", "ℹ️")
                    .replace(":exclamation:", "⚠️");


            // 2) Flexmark config with TOC + heading anchors + tables
            MutableDataSet opts = new MutableDataSet()
                    .set(Parser.EXTENSIONS, java.util.List.of(
                            TablesExtension.create(),
                            TocExtension.create()
                    ))
                    // generate id="" attributes on headings so the TOC can link to them
                    .set(HtmlRenderer.GENERATE_HEADER_ID, true)
                    .set(HtmlRenderer.RENDER_HEADER_ID, true)
                    // optional: GitHub-like id generation
                    .set(HtmlRenderer.HEADER_ID_GENERATOR_RESOLVE_DUPES, true)
                    .set(TocExtension.LIST_CLASS, "toc")
                    .set(TocExtension.LEVELS, 255);

            Parser parser = Parser.builder(opts).build();
            HtmlRenderer renderer = HtmlRenderer.builder(opts).build();

            Node document = parser.parse(md);
            String bodyHtml = renderer.render(document);

            // 3) Base href so relative images resolve inside the JAR
            String baseHref = computeDocsBaseHref();

            String css = """
                        body{font-family:-apple-system,Segoe UI,Roboto,Arial,sans-serif;padding:16px;line-height:1.6}
                        h1,h2,h3{border-bottom:1px solid #ddd;padding-bottom:.25em}
                        img{max-width:100%;height:auto}
                        code, pre{font-family:ui-monospace,SFMono-Regular,Menlo,Consolas,monospace}
                        .toc{margin:0 0 1rem 0;padding-left:1.25rem}
                        /* Minimal Bootstrap-like alerts */
                        .alert{border-radius:6px;padding:.75rem 1rem;margin:1rem 0;border:1px solid #d0d7de;
                        background:#f6f8fa}
                        .alert-info{border-color:#54aeff;background:#ddf4ff}
                        .alert-primary{border-color:#9b8eff;background:#f4f1ff}
                        .alert-warning{border-color:#c69026;background:#fff8c5}
                    """;

            String html = "<!doctype html><html><head><meta charset='UTF-8'>"
                    + (baseHref != null ? "<base href='" + baseHref + "'>" : "")
                    + "<style>" + css + "</style></head><body>"
                    + bodyHtml
                    + "</body></html>";

            webView.getEngine().loadContent(html);
        } catch (Exception e) {
            webView.getEngine().loadContent(
                    "<h2>Failed to load UserGuide</h2><pre>" + e.getMessage() + "</pre>");
        }
    }


    private String readUserGuideMarkdown() throws Exception {
        try (InputStream is = getClass().getResourceAsStream(CLASSPATH_MD)) {
            if (is != null) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
        if (Files.exists(FS_MD)) {
            return Files.readString(FS_MD, StandardCharsets.UTF_8);
        }
        return null;
    }

    // Remove YAML front matter between leading --- and the next ---
    private static String stripJekyllFrontMatter(String md) {
        String s = md.stripLeading();
        if (s.startsWith("---")) {
            int next = s.indexOf("\n---", 3);
            if (next != -1) {
                int end = next + 4; // include newline + ---
                int after = (end < s.length() && s.charAt(end) == '\n') ? end + 1 : end;
                return s.substring(after);
            }
        }
        return md;
    }

    // Resolve /docs/ inside resources to a URL string usable as <base href="...">
    private String computeDocsBaseHref() {
        URL url = getClass().getResource(CLASSPATH_DOCS_DIR);
        return (url != null) ? url.toExternalForm() : null;
    }
}
