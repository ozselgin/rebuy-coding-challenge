package com.rebuy.rebuycasestudy.controller;

import com.rebuy.rebuycasestudy.form.ProductUrl;
import com.rebuy.rebuycasestudy.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class ShortUrlController {

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping("/create")
    public String createShortUrlForm(Model model) {
        model.addAttribute("producturl", new ProductUrl());
        return "urlform";
    }

    @PostMapping("/create")
    public String createShortUrlSubmit(@ModelAttribute ProductUrl productUrl, Model model) {
        var isProductUrlValid = true;
        if (!productUrl.getFullUrl().startsWith("https://www.rebuy.de/i,")) {
            isProductUrlValid = false;
        } else {
            try {
                URI uri = new URI(productUrl.getFullUrl());
                String uriPath = uri.getPath();
                var productId = Integer.parseInt(uriPath.substring(3, uriPath.indexOf('/', 2)));
                model.addAttribute("shorturl", shortUrlService.save(productId, productUrl.getFullUrl()));
            } catch (URISyntaxException | StringIndexOutOfBoundsException e) {
                isProductUrlValid = false;
            }
        }

        model.addAttribute("isvalid", isProductUrlValid);

        return "result";
    }

    @GetMapping(value ="/l/{url}")
    public RedirectView redirectShortUrl(@PathVariable("url") String url) {

        var fullUrl = shortUrlService.getFullProductUrl(url);
        RedirectView redirectView;
        if (fullUrl.isEmpty()) {
            redirectView = new RedirectView("https://www.rebuy.de");
        } else {
            redirectView = new RedirectView(fullUrl.get());
        }
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirectView;

    }
}
