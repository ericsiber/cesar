package org.mixit.cesar;

import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.Authority;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.repository.AuthorityRepository;
import org.mixit.cesar.site.model.article.Article;
import org.mixit.cesar.site.model.article.ArticleComment;
import org.mixit.cesar.site.model.event.Event;
import org.mixit.cesar.site.model.member.*;
import org.mixit.cesar.site.model.session.*;
import org.mixit.cesar.site.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Initialize data if db is empty (only in dev)
 */
@Component("cesarInitializer")
@Profile("default")
public class CesarInitializer {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private SharedLinkRepository sharedLinkRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleCommentRepository articleCommentRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MemberEventRepository memberEventRepository;

    private Long id = -1L;

    @PostConstruct
    public void init() {
        if (authorityRepository.count() == 0) {
            Event event2016 = eventRepository.findByYear(2016);
            if (event2016 == null) {
                event2016 = eventRepository.save(new Event().setId(-1L).setYear(2016).setCurrent(true));
            }
            Event event2015 = eventRepository.save(new Event().setId(-2L).setYear(2015));
            Event event2014 = eventRepository.save(new Event().setId(-3L).setYear(2014));
            Event event2013 = eventRepository.save(new Event().setId(-4L).setYear(2013));
            Event event2012 = eventRepository.save(new Event().setId(-5L).setYear(2012));
            addInterests();

            //Friend
            addMember("Elodie", "Dupond", "Agilite", new Member(), event2016, null);
            //Participant
            addMember("Laurent", "Gayet", "Java", new Member(), event2016, null);
            addMember("Alfred", "Almendra", "Agilite", new Member(), event2016, null);
            //Sponsor
            addMember("Open", "", "Java",
                    new Sponsor()
                            .setCompany("Open")
                            .setLogoUrl("logo-open.jpg")
                            .setShortDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
                    event2016, MemberEvent.Level.GOLD);
            addMember("Viseo", "", "Java",
                    new Sponsor()
                            .setCompany("Viseo")
                            .setLogoUrl("logo-od.png")
                            .setShortDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
                    event2016, MemberEvent.Level.GOLD);
            addMember("Atlassian", "", "Java",
                    new Sponsor()
                            .setCompany("Atlassian")
                            .setLogoUrl("logo-atlassian.png")
                            .setShortDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
                    event2016, MemberEvent.Level.GOLD);
            //Staff
            addMember("Agnes",
                    "Crepet",
                    "Java",
                    new Staff()
                            .setShortDescription("Ninja Squad Co-founder - JUG Leader : Lyon JUG & Duchess France - Mix-IT Co-founder")
                            .setLongDescription("Agnès est une activiste. Une Java activiste tout d'abord ! Depuis 11 ans, elle prend plaisir à bâtir des architectures Java et les implémenter.\n" +
                                    "\n" +
                                    "Parce qu'elle aime également apprendre et partager, elle est très active dans la communauté. Elle est leader de 2 Java Users Groups en France : le Lyon JUG et Duchess France, et a été élue Java Champion en 2012! Elle est également co-fondatrice de la société coopérative de développeurs passionnés Ninja Squad\n" +
                                    "\n" +
                                    "Elle aime parler Design Patterns, Objet ou Agilité dans des conférences à travers le monde. Vous pouvez lire ses articles techniques sur des frameworks de l'écosystème Java dans la presse et ses interviews sur le blog des Duchesses.\n" +
                                    "\n" +
                                    "Parce qu'elle ne regarde que très rarement la télévision, elle a encore du temps libre pour l'organisation de la conférence Mix-IT, mix de Java et d'Agilité, qu'elle a co-fondé. Vous pouvez également l'entendre au micro du podcast Cast-IT qui aborde ses sujets de prédilection.\n" +
                                    "\n" +
                                    "Et parce que les nuits sont courtes, elle garde un peu de temps pour l'association Avataria dont elle est présidente, qui organise des concerts, festivals ou Linux Party, dans des lieux du patrimoine industriel de sa ville !\n" +
                                    "\n")
                            .setEmail("agnes.crepet@gmail.com"), event2016, null);

            addMember("Gregory", "Alexandre", "Agilite",
                    new Staff().setEmail("g.alexandre@coactiv.fr"), event2016, null);

            //Speaker
            addSpeakers(event2016, event2015, event2014, event2013, event2012);
            Staff author = addMember("Philippe", "Charrière", "Web", new Staff().setEmail("ph.charriere@gmail.com"), event2016, null);
            addArticle(author, 0);
            addArticle(author, 0);
            addArticle(author, 1);
            addArticle(author, 1);
            addArticle(author, 2);

            addSecurity(addMember("Guillaume", "EHRET", "Java", new Member().setEmail("guillaume@dev-mind.fr"), event2016, null));
        }
    }

    private void addInterests() {
        interestRepository.save(new Interest().setName("Agilite"));
        interestRepository.save(new Interest().setName("Java"));
        interestRepository.save(new Interest().setName("Scala"));
        interestRepository.save(new Interest().setName("Web"));
    }

    private <T extends Member> T addMember(String firstname, String lastname, String interest, T member, Event event, MemberEvent.Level level) {
        T persisted = (T) memberRepository.save(
                member
                        .setId(id--)
                        .setLogin(UUID.randomUUID().toString())
                        .setLastname(lastname)
                        .setFirstname(firstname)
                        .addInterest(interestRepository.findByName(interest))
        );
        memberEventRepository.save(new MemberEvent().setEvent(event).setMember(persisted).setLevel(level));

        return persisted;
    }

    private <T extends Session> T addSession(T session, Event event, String title, LocalDateTime createdAt, boolean accepted, Member... speaker) {
        for (Member s : speaker) {
            session.addSpeaker(s);
        }
        return (T) sessionRepository.save(
                session
                        .setId(id--)
                        .setTitle(title)
                        .setEvent(event)
                        .setDescription("description of " + title)
                        .setSessionAccepted(accepted)
                        .addVote(new Vote().setId(id--).setSession(session).setValue(false))
                        .addVote(new Vote().setId(id--).setSession(session).setValue(true))
                        .setSummary("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                        .setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "")
                        .setIdeaForNow("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                "")
                        .setValid(true)
                        .setAddedAt(createdAt)
                        .setLevel(Level.Beginner)
                        .setLang(SessionLanguage.fr)
                        .setGuest(true)
        );
    }

    private void addSpeakers(Event event2016, Event event2015, Event event2014, Event event2013, Event event2012) {
        Member speaker, speaker1;

        speaker = addMember("Martin", "Odersky", "Scala",
                new Member()
                        .setShortDescription("Sébastien Blanc is software engineer with 10 years of experience. He works at Red Hat and focus on Open Source libraries for Mobile.")
                        .setEmail("martin.odersky@pipo.com"), event2016, null);
        sharedLinkRepository.save(new SharedLink()
                        .setName("Twitter")
                        .setMember(speaker)
                        .setOrdernum(0)
                        .setURL("http://twitter.com/martin.ordersky")
        );
        sharedLinkRepository.save(new SharedLink()
                        .setName("Site")
                        .setMember(speaker)
                        .setOrdernum(1)
                        .setURL("http://martin.ordersky.com")
        );
        addSession(new Keynote(), event2016, "Le scala c'est super bien", LocalDateTime.now(), true, speaker);
        addSession(new Keynote(), event2015, "Le scala c'est super bien", LocalDateTime.now().minus(Duration.ofDays(365)), true, speaker);
        addSession(new Keynote(), event2014, "Le scala c'est super bien", LocalDateTime.now().minus(Duration.ofDays(365 * 2)), true, speaker);
        addSession(new Keynote(), event2013, "Le scala c'est super bien", LocalDateTime.now().minus(Duration.ofDays(365 * 3)), true, speaker);
        addSession(new Keynote(), event2012, "Le scala c'est super bien", LocalDateTime.now().minus(Duration.ofDays(365 * 4)), true, speaker);
        addSession(new Keynote(), event2016, "Java bashing", LocalDateTime.now(), false);
        addSession(new LightningTalk(), event2016, "Le scala en 5 minutes", LocalDateTime.now(), true, speaker);

        speaker1 = addMember("James", "Gosling", "Java",
                new Member()
                        .setShortDescription("Open Web Developer Advocate at Google • Tools, Performance, Animation, UX • HFR enthusiast • Creator of jQuery UI")
                        .setEmail("james.gosling@pipo.com"), event2016, null);
        addSession(new Workshop(), event2016, "Le Java c'est super bien", LocalDateTime.now(), true, speaker1);

        addSession(new Talk(), event2016, "Java vs Scala", LocalDateTime.now(), true, speaker, speaker1);

        speaker = addMember("Jean François", "Zobrist", "Agilite",
                new Member()
                        .setShortDescription("Transdisciplinary engineer. Building software, studying humans, designing interactions, thinking society.")
                        .setEmail("jf.zobrist@pipo.com"), event2016, null);
        ;
        addSession(new Talk(), event2016, "Favi l'entreprise libérée", LocalDateTime.now(), true, speaker);
    }


    private void addArticle(Staff author, int year) {

        Article article = new Article()
                .setAuthor(author)
                .setTitle("Ceci est un exemple d'article")
                .setHeadline("Vous serez tout sur l'IOT ou pas et cet artice a est lié à l'année 2015 - " + year + " an")
                .setContent("#### Titre\n" +
                        "Les *fonctions internes* des \"Basestars\" (vaisseaux mères cylon) sont contrôlées par un système d'ordinateur central mi biologique, mi machine appelé l'Hybride. L'ensemble des 7 humanoïdes Cylons décidèrent lors du Plan, de mettre l'ensemble des basestars en cluster pour donner la possibilité à tous les Hybrides de calculer les positions de milliers de Raiders (chasseurs cylons) afin d'éviter qu'ils n'entrent en collision lors des attaques.\n" +
                        "\n" +
                        "Les technologies utilisées pour implémenter le Plan furent :\n" +
                        "\n" +
                        "\n* Hazelcast et les ExecutorServices pour calculer les coordonnées des Raiders\n" +
                        "\n* Jetty pour fournir une interface de monitoring\n" +
                        "\n* un broker de message (MQTT)\n" +
                        "\n* Golo pour injecter du code dynamiquement\n" +
                        "\n*Golo* sera aussi utilisé comme glue syntaxique pour simplifier la programmation d'Hazelcast, de Jetty et des autres composants.\n" +
                        "\n" +
                        "Ce sera l'occasion de découvrir des structures caractéristiques du langage et de voir de quelle façon il s'apparie facilement avec Java sur diverses problématiques allant du classique web avec Jetty au calcule parallèle avec Hazelcast.")
                .setNbConsults(1)
                .setPostedAt(LocalDateTime.now().minus(Duration.ofDays(365 * year)))
                .setValid(true);

        ArticleComment comment = new ArticleComment()
                .setArticle(articleRepository.save(article))
                .setContent("Cet article est super bien")
                .setMember(author);

        articleCommentRepository.save(comment);
    }

    private void addSecurity(Member member) {
        Authority authority = authorityRepository.save(new Authority().setName(Role.ADMIN));
        authorityRepository.save(new Authority().setName(Role.MEMBER));
        authorityRepository.save(new Authority().setName(Role.SPEAKER));
        Account account = accountRepository.save(new Account()
                .setFirstname("Guillaume")
                .setLastname("Guillaume")
                .setLogin("user")
                .setOauthId("aaaa")
                .setPassword("password")
                .setProvider(OAuthProvider.CESAR)
                .setMember(member));
        account.addAuthority(authority);
        accountRepository.save(account);

    }
}
