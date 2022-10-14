package com.rebuy.rebuycasestudy.service;

import com.rebuy.rebuycasestudy.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Service
public class ShortUrlService {

    //we use 62 characters to create the hash. Avoiding punctuations will avoid copy-paste related problems.
    String elements = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired
    ShortUrlRepository shortUrlRepository;

    public String save(int rebuyId, String fullProductUrl) {
        var urlHash = encodeHash(rebuyId);
        shortUrlRepository.save(urlHash, fullProductUrl);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .replacePath(null)
                .build()
                .toUriString();

        return baseUrl + "/l/" + urlHash;
    }

    public String encodeHash(int rebuyId) {
        StringBuilder hashStringBuilder = new StringBuilder();
        while (rebuyId != 0) {
            hashStringBuilder.insert(0, elements.charAt(rebuyId % 62));
            rebuyId /= 62;
        }

        //zerofill if length is smaller than 4
        while (hashStringBuilder.length() < 4) {
            hashStringBuilder.insert(0, '0');
        }

        return hashStringBuilder.toString();
    }

    public Optional<String> getFullProductUrl(String productHash) {
        return shortUrlRepository.findUrlByHash(productHash);
    }


    //TODO Fallback method. Currently not used. Can be used to generate all hashes for all possible products.
    private int decodeHashToRebuyId(String hash) {
        int rebuyId = 0;
        for (int i = 0; i < hash.length(); i++) {
            rebuyId = rebuyId * 62 + decodeCharacter(hash.charAt(i));
        }
        return rebuyId;

    }

    private int decodeCharacter(char character) {
        if (character >= '0' && character <= '9') {
            //48 is 0 character in ASCII table
            return character - 48;
        }
        else if (character >= 'a' && character <= 'z') {
            //97 is a in ASCII table
            return character - 97 + 10;
        }
        else if (character >= 'A' && character <= 'Z') {
            //65 is A in ASCII table
            return character - 65 + 36;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
