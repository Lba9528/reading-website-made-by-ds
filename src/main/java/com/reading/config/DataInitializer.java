package com.reading.config;

import com.reading.model.*;
import com.reading.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
                          BookRepository bookRepository, ChapterRepository chapterRepository,
                          PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.chapterRepository = chapterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create roles
        Role userRole = roleRepository.save(new Role("USER"));
        Role adminRole = roleRepository.save(new Role("ADMIN"));

        // Create default admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@reading.com")
                    .displayName("管理员")
                    .roles(new java.util.HashSet<>(List.of(userRole, adminRole)))
                    .build();
            userRepository.save(admin);
        }

        // Create default user
        if (!userRepository.existsByUsername("reader")) {
            User reader = User.builder()
                    .username("reader")
                    .password(passwordEncoder.encode("reader123"))
                    .email("reader@reading.com")
                    .displayName("读者小明")
                    .roles(new java.util.HashSet<>(List.of(userRole)))
                    .build();
            userRepository.save(reader);
        }

        // Create sample books and chapters
        createBookWithChapters("三体", "刘慈欣",
                "《三体》是刘慈欣创作的系列长篇科幻小说，由《三体》《三体Ⅱ·黑暗森林》《三体Ⅲ·死神永生》组成。作品讲述了地球人类文明和三体文明的信息交流、生死搏杀及两个文明在宇宙中的兴衰历程。",
                "科幻",
                List.of(
                        "第一章 科学边界",
                        "第二章 朝闻道",
                        "第三章 射手和农场主",
                        "第四章 三体、牛顿、冯·诺依曼"
                ));

        createBookWithChapters("活着", "余华",
                "《活着》是余华的代表作之一，讲述了在大时代背景下，随着内战、三反五反、大跃进、文化大革命等社会变革，徐福贵的人生和家庭不断经受着苦难。",
                "小说",
                List.of(
                        "第一章 少爷",
                        "第二章 家珍",
                        "第三章 有庆",
                        "第四章 凤霞"
                ));

        createBookWithChapters("红楼梦", "曹雪芹",
                "《红楼梦》以贾宝玉、林黛玉、薛宝钗的爱情婚姻悲剧为主线，描绘了一个封建大家族的兴衰历程，是中国古典文学的巅峰之作。",
                "古典文学",
                List.of(
                        "第一回 甄士隐梦幻识通灵 贾雨村风尘怀闺秀",
                        "第二回 贾夫人仙逝扬州城 冷子兴演说荣国府",
                        "第三回 金陵城起复贾雨村 荣国府收养林黛玉",
                        "第四回 薄命女偏逢薄命郎 葫芦僧乱判葫芦案"
                ));

        createBookWithChapters("围城", "钱钟书",
                "《围城》是钱钟书所著的长篇小说，是中国现代文学史上一部风格独特的讽刺小说，被誉为「新儒林外史」。故事主要写抗战初期知识分子的群相。",
                "小说",
                List.of(
                        "第一章",
                        "第二章",
                        "第三章",
                        "第四章"
                ));
    }

    private void createBookWithChapters(String title, String author, String description,
                                        String category, List<String> chapterTitles) {
        if (bookRepository.findByTitleContainingIgnoreCaseAndPublishedTrue(title).isEmpty()) {
            Book book = Book.builder()
                    .title(title)
                    .author(author)
                    .description(description)
                    .category(category)
                    .totalChapters(chapterTitles.size())
                    .coverImageUrl("/images/cover-placeholder.svg")
                    .published(true)
                    .build();
            book = bookRepository.save(book);

            for (int i = 0; i < chapterTitles.size(); i++) {
                Chapter chapter = Chapter.builder()
                        .book(book)
                        .chapterNumber(i + 1)
                        .title(chapterTitles.get(i))
                        .content(generateSampleContent(title, chapterTitles.get(i)))
                        .published(true)
                        .build();
                chapterRepository.save(chapter);
            }
        }
    }

    private String generateSampleContent(String bookTitle, String chapterTitle) {
        return "<h2>" + chapterTitle + "</h2>\n\n" +
                "<p>这是《" + bookTitle + "》中「" + chapterTitle + "」的试读内容。</p>\n\n" +
                "<p>在很久很久以前，有一个古老而神秘的地方，那里的人们过着平静而祥和的生活。然而，命运的齿轮已经开始转动，谁也没有预料到即将到来的巨变。</p>\n\n" +
                "<p>春天来了，万物复苏。大地披上了绿色的新装，花儿在微风中摇曳。人们开始了一天的劳作，田野里传来欢快的歌声。</p>\n\n" +
                "<p>「人生如梦，」老人感叹道，「我们都在寻找属于自己的那片天空。」</p>\n\n" +
                "<p>年轻人不解地看着他，眼中充满了好奇与渴望。他们不知道前方的路有多长，但心中充满了希望。</p>\n\n" +
                "<p>夜幕降临，星辰满天。远处的山峦在月光下若隐若现，仿佛一幅水墨画卷。故事就这样开始了……</p>\n\n" +
                "<p class=\"text-muted\">（以上为试读内容，完整章节请继续阅读）</p>";
    }
}