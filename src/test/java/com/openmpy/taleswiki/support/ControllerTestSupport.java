package com.openmpy.taleswiki.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.taleswiki.article.application.ArticleService;
import com.openmpy.taleswiki.article.presentation.ArticleController;
import com.openmpy.taleswiki.common.properties.CookieProperties;
import com.openmpy.taleswiki.member.application.GoogleService;
import com.openmpy.taleswiki.member.application.KakaoService;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.presentation.MemberController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfigureRestDocs
@WebMvcTest(
        controllers = {MemberController.class, ArticleController.class},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfigurer.class})}
)
public abstract class ControllerTestSupport {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected MemberService memberService;

    @MockitoBean
    protected KakaoService kakaoService;

    @MockitoBean
    protected GoogleService googleService;

    @MockitoBean
    protected ArticleService articleService;

    @MockitoBean
    protected CookieProperties cookieProperties;
}
