package com.project.joblist.JobList.Scrapper;

import com.project.joblist.JobList.Entity.JobOffer;
import com.project.joblist.JobList.Entity.Skill;
import com.project.joblist.JobList.Repository.JobOfferRepository;
import com.project.joblist.JobList.Repository.SkillRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

            String rol;
            String company;
            String headquarters;
            String contractType;
            String offerLocation;
            String offerUrl;
            String salary;
            String skills;
            String offerFullUrl;
            Document offerDoc;

            int count = 0;
            int START_INDEX = 1;
            int START_INDEX_REPEATED_TAG = 2;
            int END_INDEX = 0;




            for(String offer : offerArray) {
                //offer = offer.split(offerEnd)[END_INDEX];
                System.out.println("EMPIEZA OFERTA");
                //System.out.println(doc);
                //System.out.println(offer);
                //System.out.println(offer.contains(contractTypeStart));
                System.out.println("TERMINA OFERTA");
                /* String [] roles =  offer.split(rolStart);
                for(String rolPrueba : roles){
                    System.out.println("PRUEBA " + rolPrueba + " PRUEBA ");
                } */
                rol = offer.split(rolStart)[START_INDEX];
                rol = rol.split(rolEnd)[END_INDEX];
                //System.out.println("ROL " + rol + " TERMINA ROL");

                company = offer.split(companyStart)[START_INDEX];
                company = company.split(companyEnd)[END_INDEX];
                //System.out.println("COMPANY " + company + " TERMINA COMPANY");

                headquarters = offer.split(headquartersStart)[START_INDEX];
                headquarters = headquarters.split(headquartersEnd)[END_INDEX];
                //System.out.println("HEADQUARTERS " + headquarters + " TERMINA HEADQUARTERS");

                contractType = offer.split(contractTypeStart)[START_INDEX];
                contractType = contractType.split(contractTypeEnd)[END_INDEX];
                System.out.println("CONTRACT " + contractType + " TERMINA CONTRACT");

                offerUrl = offer.split(offerUrlStart)[START_INDEX];
                offerUrl = offerUrl.split(offerUrlEnd)[END_INDEX];
                System.out.println("EMPIEZA URL " + offerUrl + " TERMINA URL");


                //System.out.println("OFFER" + offer);
                //System.out.println("PRECONTRACT" + preContractType + "CIERRA");
                //2 PORQUE SE SEPARAN POR EL MISMO REGEX, OFFERLOCATION ES EL SEGUNDO ELEMENTO DEL ARRAY
                offerLocation = offer.split(offerLocationStart)[START_INDEX_REPEATED_TAG];
                offerLocation = offerLocation.split(offerLocationEnd)[END_INDEX];
                System.out.println("OFFERLOCATION " + offerLocation + " TERMINA OFFERLOCATION");


                offerFullUrl = webUrl + offerUrl;
               /* offerDoc = Jsoup.connect(offerFullUrl)
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();

                System.out.println(offerDoc);

                String rol;
            String company;
            String headquarters;
            String contractType;
            String offerLocation;
            String offerUrl;
                */


                JobOffer jobOffer = JobOffer.builder()
                        .rol(rol)
                        .company(company)
                        .headquarters(headquarters)
                        .contractType(contractType)
                        .offerLocation(offerLocation)
                        .scrappedFrom("WeWorkRemotely")
                        .offerUrl(offerFullUrl)
                        .build();

                if(isNewJobOffer(jobOffer)){
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


}