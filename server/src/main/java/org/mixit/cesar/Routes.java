package org.mixit.cesar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Maps all AngularJS routes to index so that they work with direct linking.
 */
@Controller
public class Routes {

    @RequestMapping({
            "/about",
            "/account",
            "/admaccounts",
            "/admcfp",
            "/admcfptalk/{id:\\w+}",
            "/admarticle/{id:\\w+}",
            "/admarticles",
            "/article/{id:\\w+}",
            "/article/{id:\\w+}/{name:[A-Za-z0-9_\\-.]+}",
            "/articles",
            "/authent",
            "/cache",
            "/cerror",
            "/cerror/{id:\\w+}",
            "/cfp",
            "/cfptalk/{type:\\w+}",
            "/conduite",
            "/compte",
            "/createaccount/*",
            "/createaccountsocial",
            "/interest/{name:\\w+}",
            "/faq",
            "/favoris",
            "/home",
            "/lightning/{type:\\w+}/{name:[A-Za-z0-9_\\-.]+}",
            "/lightnings",
            "/logout",
            "/member/{type:\\w+}/{id:\\w+}",
            "/member/{type:\\w+}",
            "/mixit15",
            "/mixit14",
            "/mixit13",
            "/mixit12",
            "/monitor",
            "/multimedia",
            "/news/{id:\\w+}",
            "/news/{id:\\w+}/{title:[A-Za-z0-9_\\-.]+}",
            "/passwordlost",
            "/passwordreinit",
            "/planning",
            "/profile/{login:\\w+}",
            "/session/{id:\\w+}",
            "/session/{id:\\w+}/{title:[A-Za-z0-9_\\-.]+}",
            "/sessions",
            "/speakers",
            "/sponsor/{id:\\w+}",
            "/sponsors",
            "/staff",
            "/talks",
            "/valid",
            "/venir"
    })
    public String index() {
        return "forward:/";
    }


}