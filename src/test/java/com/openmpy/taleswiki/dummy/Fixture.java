package com.openmpy.taleswiki.dummy;

import com.openmpy.taleswiki.article.domain.Article;
import com.openmpy.taleswiki.article.domain.ArticleVersion;
import java.util.ArrayList;

public class Fixture {

    public static final Article ARTICLE01 = new Article("제목입니다.", "닉네임입니다.", new ArrayList<>(), null);

    public static final ArticleVersion VERSION01 = new ArticleVersion("버전1", 1, null);
    public static final ArticleVersion VERSION02 = new ArticleVersion("버전2", 2, null);
}
