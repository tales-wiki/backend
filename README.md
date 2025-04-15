## 테스트 커비리지
<img width="755" alt="Image" src="https://github.com/user-attachments/assets/26d57ba7-0bf5-4b69-a090-1ff7279e1771" />

## 프로젝트 구조

```
.
├── docs
│   └── asciidoc
│       ├── admin.adoc
│       ├── article.adoc
│       ├── index.adoc
│       └── member.adoc
├── main
│   ├── java
│   │   └── com
│   │       └── openmpy
│   │           └── taleswiki
│   │               ├── TalesWikiApplication.java
│   │               ├── admin
│   │               │   ├── application
│   │               │   │   ├── AdminCommandService.java
│   │               │   │   └── AdminQueryService.java
│   │               │   ├── domain
│   │               │   │   ├── BlockedIp.java
│   │               │   │   └── repository
│   │               │   │       └── BlockedIpRepository.java
│   │               │   └── presentation
│   │               │       ├── AdminCommandController.java
│   │               │       ├── AdminQueryController.java
│   │               │       ├── request
│   │               │       │   └── AdminBlockedIpRequest.java
│   │               │       └── response
│   │               │           ├── AdminReadAllArticleVersionReportResponse.java
│   │               │           ├── AdminReadAllArticleVersionResponse.java
│   │               │           ├── AdminReadAllBlockedIpResponse.java
│   │               │           ├── AdminReadAllBlockedIpResponses.java
│   │               │           └── AdminReadAllMemberResponse.java
│   │               ├── article
│   │               │   ├── application
│   │               │   │   ├── ArticleCommandService.java
│   │               │   │   └── ArticleQueryService.java
│   │               │   ├── domain
│   │               │   │   ├── Article.java
│   │               │   │   ├── ArticleCategory.java
│   │               │   │   ├── ArticleContent.java
│   │               │   │   ├── ArticleNickname.java
│   │               │   │   ├── ArticleReportReason.java
│   │               │   │   ├── ArticleSize.java
│   │               │   │   ├── ArticleTitle.java
│   │               │   │   ├── ArticleVersion.java
│   │               │   │   ├── ArticleVersionNumber.java
│   │               │   │   ├── ArticleVersionReport.java
│   │               │   │   └── repository
│   │               │   │       ├── ArticleRepository.java
│   │               │   │       ├── ArticleVersionReportRepository.java
│   │               │   │       └── ArticleVersionRepository.java
│   │               │   └── presentation
│   │               │       ├── ArticleCommandController.java
│   │               │       ├── ArticleQueryController.java
│   │               │       ├── request
│   │               │       │   ├── ArticleCreateRequest.java
│   │               │       │   ├── ArticleUpdateRequest.java
│   │               │       │   └── ArticleVersionReportRequest.java
│   │               │       └── response
│   │               │           ├── ArticleReadCategoryGroupResponse.java
│   │               │           ├── ArticleReadCategoryResponse.java
│   │               │           ├── ArticleReadCategoryResponses.java
│   │               │           ├── ArticleReadLatestUpdateResponse.java
│   │               │           ├── ArticleReadLatestUpdateResponses.java
│   │               │           ├── ArticleReadResponse.java
│   │               │           ├── ArticleResponse.java
│   │               │           ├── ArticleSearchResponse.java
│   │               │           ├── ArticleSearchResponses.java
│   │               │           ├── ArticleVersionReadArticleResponse.java
│   │               │           └── ArticleVersionReadArticleResponses.java
│   │               ├── auth
│   │               │   ├── annotation
│   │               │   │   └── Login.java
│   │               │   ├── infrastructure
│   │               │   │   ├── AuthenticationExtractor.java
│   │               │   │   ├── AuthenticationInterceptor.java
│   │               │   │   └── AuthenticationPrincipalArgumentResolver.java
│   │               │   └── jwt
│   │               │       ├── JwtProperties.java
│   │               │       └── JwtTokenProvider.java
│   │               ├── common
│   │               │   ├── application
│   │               │   │   ├── ImageLocalService.java
│   │               │   │   ├── ImageS3Service.java
│   │               │   │   ├── ImageService.java
│   │               │   │   ├── ImageStorageStrategyContext.java
│   │               │   │   └── RedisService.java
│   │               │   ├── config
│   │               │   │   ├── JpaAuditingConfig.java
│   │               │   │   ├── RedisConfig.java
│   │               │   │   ├── RestClientConfig.java
│   │               │   │   ├── S3Config.java
│   │               │   │   └── WebMvcConfig.java
│   │               │   ├── domain
│   │               │   │   ├── BaseEntity.java
│   │               │   │   └── ClientIp.java
│   │               │   ├── dummy
│   │               │   │   └── DummyData.java
│   │               │   ├── exception
│   │               │   │   ├── AuthenticationException.java
│   │               │   │   ├── CustomErrorCode.java
│   │               │   │   ├── CustomException.java
│   │               │   │   ├── ErrorResponse.java
│   │               │   │   └── GlobalExceptionHandler.java
│   │               │   ├── infrastructure
│   │               │   │   └── RequestServletFilter.java
│   │               │   ├── presentation
│   │               │   │   ├── ImageController.java
│   │               │   │   └── response
│   │               │   │       ├── ImageUploadResponse.java
│   │               │   │       └── PaginatedResponse.java
│   │               │   ├── properties
│   │               │   │   ├── CookieProperties.java
│   │               │   │   ├── CorsProperties.java
│   │               │   │   ├── DiscordProperties.java
│   │               │   │   ├── GoogleProperties.java
│   │               │   │   └── KakaoProperties.java
│   │               │   └── util
│   │               │       ├── CharacterUtil.java
│   │               │       ├── DateFormatterUtil.java
│   │               │       ├── FileLoaderUtil.java
│   │               │       └── IpAddressUtil.java
│   │               ├── discord
│   │               │   └── application
│   │               │       ├── DiscordService.java
│   │               │       └── request
│   │               │           ├── DiscordEmbeds.java
│   │               │           └── DiscordErrorMessageRequest.java
│   │               └── member
│   │                   ├── application
│   │                   │   ├── GoogleService.java
│   │                   │   ├── KakaoService.java
│   │                   │   ├── MemberService.java
│   │                   │   └── response
│   │                   │       ├── GoogleLoginResponse.java
│   │                   │       └── KakaoLoginResponse.java
│   │                   ├── domain
│   │                   │   ├── Member.java
│   │                   │   ├── MemberAuthority.java
│   │                   │   ├── MemberEmail.java
│   │                   │   ├── MemberSocial.java
│   │                   │   └── repository
│   │                   │       └── MemberRepository.java
│   │                   └── presentation
│   │                       ├── MemberController.java
│   │                       └── response
│   │                           ├── MemberLoginResponse.java
│   │                           └── MemberResponse.java
│   └── resources
│       ├── application-dev.yml
│       ├── application-local.yml
│       ├── application.yml
│       ├── logback-spring.xml
│       ├── markdown
│       │   ├── 1.md
│       │   ├── 2.md
│       │   ├── 3.md
│       │   └── sample.md
│       ├── static
│       │   └── docs
│       │       ├── admin.html
│       │       ├── article.html
│       │       ├── index.html
│       │       └── member.html
│       └── templates
└── test
    ├── java
    │   └── com
    │       └── openmpy
    │           └── taleswiki
    │               ├── admin
    │               │   ├── application
    │               │   │   ├── AdminCommandServiceTest.java
    │               │   │   └── AdminQueryServiceTest.java
    │               │   ├── domain
    │               │   │   └── BlockedIpTest.java
    │               │   └── presentation
    │               │       ├── AdminCommandControllerTest.java
    │               │       └── AdminQueryControllerTest.java
    │               ├── article
    │               │   ├── application
    │               │   │   ├── ArticleCommandServiceTest.java
    │               │   │   └── ArticleQueryServiceTest.java
    │               │   ├── domain
    │               │   │   ├── ArticleCategoryTest.java
    │               │   │   ├── ArticleContentTest.java
    │               │   │   ├── ArticleNicknameTest.java
    │               │   │   ├── ArticleSizeTest.java
    │               │   │   ├── ArticleTest.java
    │               │   │   ├── ArticleTitleTest.java
    │               │   │   ├── ArticleVersionNumberTest.java
    │               │   │   ├── ArticleVersionReportReasonTest.java
    │               │   │   ├── ArticleVersionReportTest.java
    │               │   │   └── ArticleVersionTest.java
    │               │   └── presentation
    │               │       ├── ArticleCommandControllerTest.java
    │               │       └── ArticleQueryControllerTest.java
    │               ├── auth
    │               │   └── infrastructure
    │               │       └── AuthenticationExtractorTest.java
    │               ├── common
    │               │   ├── application
    │               │   │   ├── ImageLocalServiceTest.java
    │               │   │   ├── ImageS3ServiceTest.java
    │               │   │   └── RedisServiceTest.java
    │               │   ├── domain
    │               │   │   └── ClientIpTest.java
    │               │   ├── infrastructure
    │               │   │   └── RequestServletFilterTest.java
    │               │   └── util
    │               │       ├── CharacterUtilTest.java
    │               │       ├── DateFormatterUtilTest.java
    │               │       ├── FileLoaderUtilTest.java
    │               │       └── IpAddressUtilTest.java
    │               ├── discord
    │               │   └── application
    │               │       └── DiscordServiceTest.java
    │               ├── member
    │               │   ├── application
    │               │   │   └── MemberServiceTest.java
    │               │   ├── domain
    │               │   │   ├── MemberEmailTest.java
    │               │   │   └── MemberTest.java
    │               │   └── presentation
    │               │       └── MemberControllerTest.java
    │               └── support
    │                   ├── ControllerTestSupport.java
    │                   ├── CustomServiceTest.java
    │                   ├── EmbeddedRedisConfig.java
    │                   ├── Fixture.java
    │                   ├── MockAuthenticationPrincipalArgumentResolver.java
    │                   ├── ServiceTestSupport.java
    │                   └── TestWebMvcConfig.java
    └── resources
        └── application.yml

```