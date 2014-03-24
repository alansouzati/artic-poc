package br.ufrgs.artic;

import br.ufrgs.artic.model.ArticMetadata;
import br.ufrgs.artic.utils.FileUtils;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class ArticRunnerTest {

    public void testFirstLevelParsing() throws IOException, JSONException {

        ArticRunner runner = new ArticRunner("C:\\omnipage");

        ArticMetadata articMetadata = runner.getMetadata(getClass().getResource("/core/acm1.pdf").getFile());

        String actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/acm1.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/acm2.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/acm2.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/acm3.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/acm3.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/acm4.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/acm4.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/acm5.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/acm5.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/acm6.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/acm6.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/acm7.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/acm7.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/ieee1.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/ieee1.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/ieee3.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/ieee3.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/ieee4.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/ieee4.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/elsevier1.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/elsevier1.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/elsevier2.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/elsevier2.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/elsevier3.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/elsevier3.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/elsevier4.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/elsevier4.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/elsevier5.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/elsevier5.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);

        articMetadata = runner.getMetadata(getClass().getResource("/core/elsevier6.pdf").getFile());

        actual = articMetadata.toJson();
        System.out.println(actual);
        assertEquals(FileUtils.readFile(getClass().getResource("/core/elsevier6.artic.metadata").getFile(), Charset.forName("UTF-8")), actual, true);
    }
}
