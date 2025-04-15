package com.evenly.evenide.global.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomNameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "못생긴", "수줍은", "귀여운", "상냥한", "느긋한",
            "조용한", "명랑한", "상큼한", "어설픈", "따뜻한",
            "포근한", "순한", "활발한", "엉뚱한", "차분한",
            "웃긴", "부끄러운", "쑥스러운", "기분좋은", "재밌는");
    private static final List<String> ANIMALS = List.of(
            "고슴도치", "너구리", "고양이", "수달", "팬더",
            "햄스터", "토끼", "호랑이", "여우", "사슴",
            "곰", "강아지", "문어", "펭귄", "돌고래",
            "고라니", "앵무새", "물개", "치타", "나무늘보"
    );

    private static final List<String> FOODS = List.of(
            "초밥", "김밥", "떡볶이", "치킨", "바나나",
            "붕어빵", "라면", "딸기", "케이크", "피자"
    );

    public static String generateNickname() {
        Random random = new Random();
        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));

        List<String> nouns = new ArrayList<>();
        nouns.addAll(ANIMALS);
        nouns.addAll(FOODS);

        String noun = nouns.get(random.nextInt(nouns.size()));
        return adjective + " " + noun;
    }

    public static String generateSenderId() {
        return "anon-" + UUID.randomUUID().toString().substring(0, 8);
    }

}
