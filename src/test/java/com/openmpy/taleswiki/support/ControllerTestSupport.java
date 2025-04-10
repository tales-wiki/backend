package com.openmpy.taleswiki.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmpy.taleswiki.admin.application.AdminCommandService;
import com.openmpy.taleswiki.admin.application.AdminQueryService;
import com.openmpy.taleswiki.admin.presentation.AdminCommandController;
import com.openmpy.taleswiki.admin.presentation.AdminQueryController;
import com.openmpy.taleswiki.article.application.ArticleCommandService;
import com.openmpy.taleswiki.article.application.ArticleQueryService;
import com.openmpy.taleswiki.article.presentation.ArticleCommandController;
import com.openmpy.taleswiki.article.presentation.ArticleQueryController;
import com.openmpy.taleswiki.common.infrastructure.RequestServletFilter;
import com.openmpy.taleswiki.common.properties.CookieProperties;
import com.openmpy.taleswiki.discord.application.DiscordService;
import com.openmpy.taleswiki.member.application.GoogleService;
import com.openmpy.taleswiki.member.application.KakaoService;
import com.openmpy.taleswiki.member.application.MemberService;
import com.openmpy.taleswiki.member.presentation.MemberController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfigureRestDocs
@WebMvcTest(
        controllers = {
                MemberController.class,
                ArticleCommandController.class,
                ArticleQueryController.class,
                AdminCommandController.class,
                AdminQueryController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        WebMvcConfigurer.class,
                        RequestServletFilter.class
                })
        }
)
@Import(TestWebMvcConfig.class)
public class ControllerTestSupport {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected CookieProperties cookieProperties;

    @MockitoBean
    protected MemberService memberService;

    @MockitoBean
    protected KakaoService kakaoService;

    @MockitoBean
    protected GoogleService googleService;

    @MockitoBean
    protected ArticleCommandService articleCommandService;

    @MockitoBean
    protected ArticleQueryService articleQueryService;

    @MockitoBean
    protected AdminCommandService adminCommandService;

    @MockitoBean
    protected AdminQueryService adminQueryService;

    @MockitoBean
    protected DiscordService discordService;
}
