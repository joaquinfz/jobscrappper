package com.project.joblist.JobList.Scrapper;

import com.project.joblist.JobList.Entity.JobOffer;
import com.project.joblist.JobList.Entity.Skill;
import com.project.joblist.JobList.Repository.JobOfferRepository;
import com.project.joblist.JobList.Repository.SkillRepository;
import com.project.joblist.JobList.Scrapper.Builder.WeWorkBuilder;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Component
@AllArgsConstructor
public class WeWorkRemotelyScrapper implements Scrapper {

    @Autowired
    JobOfferRepository jobOfferRepository;
    SkillRepository skillRepository;

    public static final String SPLIT_LOGIN = "https://www.linkedin.com/login/es?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin";

    public List<JobOffer> scrap(String search){
        ArrayList<JobOffer> jobOffers = new ArrayList<>();
        // , Map<String,String> filters
        try {
            String webUrl = "https://weworkremotely.com/";
            String url = webUrl + "/categories/remote-back-end-programming-jobs#job-listings";

            Document doc = Jsoup.connect(url)
                   /* .data("action", "account",
                            "redirect", "account_home.php?",
                            "radiobutton", "old",
                            "loginemail", "jfuzatti@gmail.com",
                            "password", "130798Jf",
                            "LoginChoice", "Sign In to Secure Area") */
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();

            String offerStart = "<li class=\" new-listing-";
            String offerEnd = "<script>\n";

            String rolStart = "<h4 class=\"new-listing__header__title\">";
            String rolEnd = "</h4>";

            String companyStart = "<p class=\"new-listing__company-name\">";
            String companyEnd = "<img alt";

            String headquartersStart = "<p class=\"new-listing__company-headquarters\">";
            String headquartersEnd = "<i class=";

            String contractTypeStart = "<p class=\"new-listing__categories__category\">";
            String contractTypeEnd = "</p>";
            //IF EMPIEZA CON $ ES EL SALARIO, EL PROXIMO INDEX EN EL ARRAY ES LA LOCATION
            String offerLocationStart = "<p class=\"new-listing__categories__category\">";
            String offerLocationEnd = "</p>";

            String offerUrlStart = "</div><a href=\"";
            String offerUrlEnd = "\">";

            String[] preOfferArray = doc.toString().split(offerStart);
            //PARA ELIMINAR EL PRIMER ELEMENTO (ANTES DE LA OFERTA)
            String[] offerArray = Arrays.copyOfRange(preOfferArray, 1, preOfferArray.length);


            HashMap<Consumer<String>, Pair> parameters = new HashMap<>();




            String rol= null;
            String company;
            String headquarters;
            String contractType;
            String offerLocation = null;
            String offerUrl = null;
            String salary;
            String skills;
            String offerFullUrl;
            Document offerDoc;

            WeWorkBuilder builder = new WeWorkBuilder();

            parameters.put(builder::setRol, new Pair<>(rolStart, rolEnd));
            parameters.put(builder::setCompany, new Pair<>(companyStart, companyEnd));
            parameters.put(builder::setHeadquarters, new Pair<>(headquartersStart, headquartersEnd));
            parameters.put(builder::setContractType, new Pair<>(contractTypeStart, contractTypeEnd));
            parameters.put(builder::setOfferLocation, new Pair<>(offerLocationStart, offerLocationEnd));
            parameters.put(builder::setOfferFullUrl, new Pair<>(offerUrlStart, offerUrlEnd));

            int count = 0;
            int START_INDEX = 1;
            int START_INDEX_REPEATED_TAG = 2;
            int END_INDEX = 0;

            String parameter;


            for(String offer : offerArray) {

                for(Map.Entry<Consumer<String>, Pair> entry : parameters.entrySet()) {
                    //offer = offer.split(offerEnd)[END_INDEX];
                    //a = start, b = end

                    String[] parameterSplit = offer.split((String) entry.getValue().a);
                    if (parameterSplit.length > 1) {
                        //Agarro el segundo elemento del split(luego del regex) y le saco el final, donde termina la etiqueta html
                        parameter = parameterSplit[START_INDEX].split((String) entry.getValue().b)[END_INDEX];
                        entry.getKey().accept(parameter);

                        String methodName = getMethodName(entry.getKey());
                        System.out.println("EMPIEZA " + methodName + ": " + parameter + " TERMINA");
                    }


                }

                offerFullUrl = webUrl + offerUrl;


                JobOffer jobOffer = JobOffer.builder()
                        .rol(builder.getRol())
                        .company(builder.getCompany())
                        .headquarters(builder.getHeadquarters())
                        .contractType(builder.getContractType())
                        .offerLocation(builder.getOfferLocation())
                        .scrappedFrom("WeWorkRemotely")
                        .offerUrl(builder.getOfferFullUrl())
                        .build();

                if (isNewJobOffer(jobOffer)) {
                    jobOffers.add(jobOffer);
                }
                    //System.out.println(documentPending.contains("div"));
            }


            // Document doc = Jsoup.connect(url).get();
           // System.out.println(count + offerDiv);

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
        CompletableFuture<List<String>> scrapDetails = scrapDetails(jobOffers);

        return jobOffers;
    }

    @Async("taskExecutor")
    public CompletableFuture<List<String>> scrapDetails(List<JobOffer> jobOffers) {
       System.out.println("entraa");
       String skillStart = "<a target=\"_blank\" href=\"/remote-jobs-";
       String skillEnd = "\"";
       ArrayList<Skill> offerSkills = new ArrayList<>();
       Object lock = new Object();

        return CompletableFuture.supplyAsync(() -> {
            String[] skills ;
            List<String> scrapedData = new ArrayList<>();
            try {
                for(JobOffer offer : jobOffers) {
                    offerSkills.clear();
                    System.out.println(offer.getOfferUrl());
                    // Scrape data from a website (e.g., example.com)
                    Document document = Jsoup.connect(offer.getOfferUrl()).get();
                    skills =document.toString().split(skillStart); // Update the array


                    for(String skillString : skills){
                        skillString = skillString.split(skillEnd)[0].toUpperCase();
                        System.out.println(skillString);

                        Optional<Skill> existingSkill = searchSameSkill(skillString);

                        int FIRST_OCURRENCE = 1;
                        Skill newSkill = Skill.builder()
                                .name(skillString)
                                .ocurrences(FIRST_OCURRENCE)
                                .build();

                        if (existingSkill.isEmpty()) {
                            newSkill = saveSkill(newSkill);
                        } else {
                            skillRepository.incrementOccurrences(existingSkill.get());
                            newSkill.setId(existingSkill.get().getId());
                        }
                        offerSkills.add(newSkill);
                        jobOfferRepository.insertJobSkill(offer.getId(), newSkill.getId());
                    }

                    offer.setSkills(offerSkills);



                }
            } catch (IOException e) {
                System.err.println("Error scraping website: " + e.getMessage());
            }
            return scrapedData;
        });
    }

    public Boolean isNewJobOffer(JobOffer jobOffer){
         return jobOfferRepository.findSameJobOffer(jobOffer.getRol(),
                                            jobOffer.getCompany(),
                                            jobOffer.getOfferLocation(),
                                            jobOffer.getScrappedFrom()).isEmpty();
    }

    public Optional<Skill> searchSameSkill(String skill){
        return skillRepository.findSameSkill(skill);
    }

    @Transactional
    public Skill saveSkill(Skill skill){

        Long FIRST_INSERTED_SKILL_ID = 1L;



        Long generatedId = FIRST_INSERTED_SKILL_ID;

        Optional<Skill> lastInsertedSkill = skillRepository.getLastInsertedSkill();
         if(lastInsertedSkill.isPresent()){
             generatedId = lastInsertedSkill.get().getId() + 1;
         }
        skill.setId(generatedId);

        skillRepository.insertSkill(skill.getName(),
                skill.getInfo(), skill.getOcurrences());
        return skill;
    }


    private static String getMethodName(Consumer<String> setter) {
        try {
            // Get the SerializedLambda from the method reference
            Method writeReplace = setter.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) writeReplace.invoke(setter);

            return lambda.getImplMethodName(); // Returns something like "setRol"
        } catch (Exception e) {
            e.printStackTrace();
            return "UnknownMethod";
        }
    }

}