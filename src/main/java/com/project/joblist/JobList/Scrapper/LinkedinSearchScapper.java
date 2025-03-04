package com.project.joblist.JobList.Scrapper;

import com.project.joblist.JobList.Entity.JobOffer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LinkedinSearchScapper implements Scrapper {
    public static final String SPLIT_LOGIN = "https://www.linkedin.com/login/es?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin";

    public List<JobOffer> scrap(String search){
        // , Map<String,String> filters
        try {
            String url = "https://www.linkedin.com/search/results/content/?datePosted=%22past-month%22&keywords="+ search +"&origin=FACETED_SEARCH&sid=qr5";

            Document res = Jsoup.connect(url)
                    .data("action", "account",
                            "redirect", "account_home.php?",
                            "radiobutton", "old",
                            "loginemail", "jfuzatti@gmail.com",
                            "password", "130798Jf",
                            "LoginChoice", "Sign In to Secure Area")
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();


            Document doc = Jsoup.connect(url).get();
            System.out.println("Page Title: " + res);

           /* Connection.Response res2 = Jsoup.connect(SPLIT_LOGIN)
                    .data("ok", "End Prior Session")
                    .method(Connection.Method.POST)
                    .cookies(res.cookies())
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
                    .execute(); */
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
