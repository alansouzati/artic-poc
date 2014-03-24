package br.ufrgs.artic.model;

import java.util.List;

public class ArticBoundingBox {

    public List<ArticWord> words;
    public ArticDimension dimension;
    private int biggestLine;

    public String getText() {
        if (words != null) {
            StringBuilder text = new StringBuilder();
            for (ArticWord articWord : words) {
                text.append(articWord.getOriginalText()).append(" ");
            }
            return text.toString().trim();
        }

        return null;
    }

    public int getParagraph() {
        for (ArticWord word : words) {
            int index = (word instanceof ArticAuthorInformation) ? ((ArticAuthorInformation) word).getParagraphIndex() : word.getLineIndex();
            if (index > biggestLine) {
                biggestLine = index;
            }
        }

        return biggestLine;
    }
}
