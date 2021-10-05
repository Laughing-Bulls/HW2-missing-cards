import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HW2MissingCard {

    public static int[] shuffle(int deckSize, boolean shuffle) {
        // routine to define and shuffle cards
        // boolean shuffle is true;
        int[] cards = new int[deckSize];
        for (int i = 0; i < deckSize; i++) {
            cards[i] = i + 1;
        }
        int randomCard;
        int temp;
        if (shuffle) {
            for (int j = 0; j < deckSize; j++) {
                randomCard = (int) (Math.random() * deckSize);
                temp = cards[randomCard];
                cards[randomCard] = cards[j];
                cards[j] = temp;
            }
        }
        return cards;
    }

    public static int[] select(int[] cards, int num2choose) {
        // routine to randomly select x cards
        int[] selectedCards = new int[num2choose];
        for (int i = 0; i < num2choose; i++) {
            selectedCards[i] = cards[i];
        }
        return selectedCards;
    }

    public static void printCards(int[] cards, File file) {

        int printLines = cards.length;
        System.out.println(printLines);
        String rank;
        String suit;
        int suitNum;
        int rankNum;


        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);


        for (int i = 0; i < printLines; i++) {
            // System.out.print(cards[i]);
            suitNum = (cards[i] - 1) / 13;
            rankNum = cards[i] - (suitNum * 13);
            rank = String.valueOf(rankNum);
            if (rankNum == 1) rank = "Ace";
            if (rankNum == 11) rank = "Jack";
            if (rankNum == 12) rank = "Queen";
            if (rankNum == 13) rank = "King";
            //        - if 1 - 13, suit = hearts, rank - 0
            //        - else if 14 - 26 suit = diamonds, rank - 13
            //        - else if 27 - 39 suit = spades, rank - 26
            //        - else if 40 - 52 suit = clubs, rank - 39

            if (suitNum == 0) suit = (" of hearts");
            else if (suitNum == 1) suit = (" of diamonds");
            else if (suitNum == 2) suit = (" of spades");
            else suit = (" of clubs");
            System.out.println(" " + rank + suit);

            try {
                bw.write(" " + rank + suit);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println();
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] mapReduceCards(int missingSize, File sourceFile) {
        // Use MapReduce to identify missing cards

        // input file format is "Ace of Hearts"
        BufferedReader reader;
        int[] missingCards = new int[missingSize];
        try {
            reader = new BufferedReader(new FileReader(sourceFile));
            String line;
            int counter = 0;
            // String[] suit = {"hearts", "diamonds", "spades", "clubs"};
            String[] rank = {" Ace", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", " 10", " Jack", " Queen", " King"};
            String[] hearts = new String[52];
            String[] diamonds = new String[52];
            String[] spades = new String[52];
            String[] clubs = new String[52];

            while ((line = reader.readLine()) != null) {

                String card = line.toString();
                String[] halves = card.split(" of ");
                if (halves[1].equals("hearts")) hearts[counter] = halves[0];
                if (halves[1].equals("diamonds")) diamonds[counter] = halves[0];
                if (halves[1].equals("spades")) spades[counter] = halves[0];
                if (halves[1].equals("clubs")) clubs[counter] = halves[0];

                // System.out.println("*" + halves[0] + "*of*" + halves[1] + "*");
                if(counter > 52 - missingSize) System.out.println("ERROR: Too many missing cards found");

                counter++;
            }

            int count = 0;
            int rankNum = 1;
            int calc = 0;
            java.util.List<String> heartsList = new ArrayList<>(Arrays.asList(hearts));
            java.util.List<String> diamondsList = new ArrayList<>(Arrays.asList(diamonds));
            java.util.List<String> spadesList = new ArrayList<>(Arrays.asList(spades));
            java.util.List<String> clubsList = new ArrayList<>(Arrays.asList(clubs));

            // System.out.println(heartsList);
            // System.out.println(diamondsList);
            // System.out.println(spadesList);
            // System.out.println(clubsList);

            // Test for missing cards in the string list
            for (String i: rank) {

                if(!heartsList.contains(i)) {
                    // System.out.println(i + " of hearts ++");
                    calc = rankNum + 0;
                    missingCards[count] = calc;
                    count++;
                }
                if(!diamondsList.contains(i)) {
                    // System.out.println(i + " of diamonds ++");
                    calc = rankNum + 13;
                    missingCards[count] = calc;
                    count++;
                }
                if(!spadesList.contains(i)) {
                    // System.out.println(i + " of spades ++");
                    calc = rankNum + 26;
                    missingCards[count] = calc;
                    count++;
                }
                if(!clubsList.contains(i)) {
                    // System.out.println(i + " of clubs ++");
                    calc = rankNum + 39;
                    missingCards[count] = calc;
                    count++;
                }
                rankNum++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        // Pass back the array of missing cards
        return missingCards;
    }


    public static void main(String[] args) {

        // Develop and test a MapReduce-based approach in the Hadoop system to find all the missing Poker cards.

        // Make cards and shuffle
        int deckSize = 52;
        // int[] cards = new int[deckSize];
        int[] shuffledDeck;
        shuffledDeck = shuffle(deckSize, true);

        // Select and print a random number of cards
        int number2choose = (int) (Math.random() * deckSize);
        // System.out.println(number2choose);
        int[] selectedCards;
        selectedCards = select(shuffledDeck, number2choose);

       try {
            // Create new file
            String path = "input.txt";
            File file = new File(path);

            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            System.out.println("INPUT");
            printCards(selectedCards, file);

            int[] missingCards = new int[deckSize - number2choose];
            // MapReduce
            missingCards = mapReduceCards(deckSize - number2choose, file);

            // Create new file
            String pathOut = "output.txt";
            File Outfile = new File(pathOut);

            // If file doesn't exists, then create it
            if (!Outfile.exists()) {
                Outfile.createNewFile();
            }

            // Print missing cards
            System.out.println("OUTPUT");
            printCards(missingCards, Outfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
