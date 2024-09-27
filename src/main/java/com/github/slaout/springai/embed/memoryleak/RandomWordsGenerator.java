package com.github.slaout.springai.embed.memoryleak;

import java.util.Random;

public class RandomWordsGenerator {

    private static final String[] WORDS = {
        "apple", "banana", "orange", "grape", "melon", "kiwi", "mango", "peach", "berry", "cherry",
        "lemon", "lime", "plum", "pear", "fig", "pomegranate", "pineapple", "apricot", "nectarine", "tangerine",
        "watermelon", "papaya", "guava", "coconut", "passionfruit", "dragonfruit", "durian", "jackfruit", "rambutan", "lychee",
        "strawberry", "blueberry", "raspberry", "blackberry", "cranberry", "gooseberry", "currant", "elderberry", "mulberry", "boysenberry",
        "tomato", "potato", "carrot", "onion", "garlic", "ginger", "pepper", "spinach", "kale", "lettuce",
        "cabbage", "cauliflower", "broccoli", "zucchini", "cucumber", "pumpkin", "squash", "radish", "beet", "turnip",
        "celery", "parsley", "basil", "oregano", "thyme", "rosemary", "dill", "cilantro", "mint", "sage",
        "mushroom", "olive", "artichoke", "asparagus", "eggplant", "brussels", "corn", "peas", "bean", "chickpea",
        "lentil", "quinoa", "barley", "rice", "wheat", "oat", "buckwheat", "spelt", "rye", "millet",
        "avocado", "almond", "cashew", "walnut", "pecan", "pistachio", "hazelnut", "peanut", "macadamia", "brazilnut",
        "coffee", "tea", "milk", "juice", "water", "soda", "wine", "beer", "whiskey", "rum",
        "vodka", "gin", "brandy", "champagne", "cider", "smoothie", "shake", "latte", "mocha", "espresso",
        "bread", "bagel", "croissant", "donut", "muffin", "cookie", "brownie", "cake", "pie", "pudding",
        "waffle", "pancake", "toast", "sandwich", "burger", "pizza", "taco", "burrito", "quesadilla", "sushi",
        "pasta", "spaghetti", "lasagna", "ravioli", "fettuccine", "macaroni", "noodle", "dumpling", "springroll", "wonton",
        "chicken", "beef", "pork", "lamb", "fish", "shrimp", "crab", "lobster", "oyster", "mussel",
        "clams", "salmon", "tuna", "sardine", "mackerel", "trout", "herring", "anchovy", "octopus", "squid",
        "cheese", "butter", "yogurt", "cream", "icecream", "chocolate", "candy", "caramel", "honey", "jam",
        "jelly", "syrup", "ketchup", "mustard", "mayonnaise", "relish", "salsa", "guacamole", "hummus", "tahini",
        "sugar", "salt", "pepper", "cinnamon", "nutmeg", "paprika", "cumin", "coriander", "turmeric", "saffron",
        "soy", "vinegar", "oil", "buttermilk", "tofu", "seitan", "tempeh", "miso", "kimchi", "sauerkraut"
    };

    public static String generateRandomWords(int wordCount) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {
            String word = WORDS[random.nextInt(WORDS.length)];
            result.append(word);
            if (i < wordCount - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

}
