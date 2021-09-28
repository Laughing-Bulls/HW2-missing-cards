import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HW2MissingCard {

    public static int[] shuffle(int deckSize, boolean shuffle) {
        // routine to define and shuffle cards
        // boolean shuffle is true;
        int[] cards = new int[deckSize];
        for(int i = 0; i < deckSize; i++) {
            cards[i] = i + 1;
        }
        int randomCard;
        int temp;
        if (shuffle) {
            for (int j = 0; j < deckSize; j++) {
                randomCard = (int)(Math.random() * deckSize);
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
        for(int i = 0; i < num2choose; i++) {
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

        // ...................
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);

        // ...............


        for(int i = 0; i < printLines; i++) {
            System.out.print(cards[i]);
            suitNum = cards[i] / 13;
            rankNum = cards[i] - (suitNum * 13)  + 1;
            rank = String.valueOf(rankNum);
            if (rankNum == 1) rank = "Ace";
            if (rankNum == 11) rank = "Jack";
            if (rankNum == 12) rank = "Queen";
            if (rankNum == 13) rank = "King";
            //        - if 0 - 12, suit = hearts, rank - 0
            //        - else if 13 - 25 suit = diamonds, rank - 13
            //        - else if 26 - 38 suit = spades, rank - 26
            //        - else if 39 - 51 suit = clubs, rank - 39

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

    public static void main(String[] args){

        // Develop and test a MapReduce-based approach in the Hadoop system to find all the missing Poker cards.

        // Make cards and shuffle
        int deckSize = 52;
        // int[] cards = new int[deckSize];
        int[] shuffledDeck;
        shuffledDeck = shuffle(deckSize, true);

        // Select and print a random number of cards
        int number2choose = (int)(Math.random() * deckSize);
        // System.out.println(number2choose);
        int[] selectedCards;
        selectedCards = select(shuffledDeck, number2choose);

        // .............
       try {
           // Create new file
           String path = "input.txt";
           File file = new File(path);

           // If file doesn't exists, then create it
           if (!file.exists()) {
               file.createNewFile();
           }

           printCards(selectedCards, file);
       }
       catch (IOException e) {
           e.printStackTrace();
       }
        // ...............

        // Use MapReduce to identify missing cards
        int[] missingCards = new int[deckSize - number2choose];
        // MapReduce

        // .............
        try {
            // Create new file
            String path = "output.txt";
            File Outfile = new File(path);

            // If file doesn't exists, then create it
            if (!Outfile.exists()) {
                Outfile.createNewFile();
            }

            // Print missing cards
            printCards(missingCards, Outfile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // ...............
    }

}
