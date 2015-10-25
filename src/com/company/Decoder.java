package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Decoder {

    int cypherLength;
    String fileName;
    List<  List<Integer>  > key;
    List<  List<Integer>  > cyphers;
    List<  List<Integer>  > XORedCyphers;



    Decoder(String fileName)
    {
        this.fileName = fileName;
        cyphers = new ArrayList<>();
        XORedCyphers  = new ArrayList<>();
        key = new ArrayList<>();
    }


    private void readCyphers() throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line;


        List<Integer> cypher = new ArrayList<>();

        line = reader.readLine();
        for(String block : line.split(" "))
        {
            cypher.add(Integer.parseInt(block, 2));
        }
        cypherLength = cypher.size();
        cyphers.add(cypher);

        while ((line = reader.readLine()) != null)
        {
            cypher = new ArrayList<>();
            for(String block : line.split(" "))
            {

                cypher.add(Integer.parseInt(block, 2));

            }

            cypher = cypher.subList(0, cypherLength);
            cyphers.add(cypher);

        }

        reader.close();

    }

    public List<Integer> and(List<Integer> a, List<Integer> b)
    {
        ArrayList<Integer> aANDb = new ArrayList<>();
        for(int i = 0 ; i < cypherLength; i++)
        {

            if((a.get(i) == 'Y' && b.get(i) == 'Y'))
                aANDb.add((int)'Y');
            else
                aANDb.add((int)'_');

        }

        return aANDb;
    }

    public List<Integer> xor(List<Integer> a, List<Integer> b)
    {
        ArrayList<Integer> aXORb = new ArrayList<>();

        for(int i = 0 ; i < cypherLength; i++)
        {
            char c = (char)(a.get(i) ^ b.get(i));

            if(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))
                c = 'Y';
            else
                c = '_';

            aXORb.add((int)c);
        }
        return aXORb;
    }


    private void xorToFindSpaces()
    {
        int numberOfCyphers = cyphers.size();
        List<Integer> firstAND;
        List<Integer> secondAND;

        /* Two ANDs increase precision*/

        for (int i = 0; i < cyphers.size(); i++)
        {

            firstAND = and(xor(cyphers.get(i), cyphers.get((i + 1) % numberOfCyphers)),
                           xor(cyphers.get(i), cyphers.get((i + 2) % numberOfCyphers)));

            secondAND = and(xor(cyphers.get(i), cyphers.get((i + 3) % numberOfCyphers)),
                            xor(cyphers.get(i), cyphers.get((i + 4) % numberOfCyphers)));


            XORedCyphers.add(and(firstAND, secondAND));

            /*for(Integer symbol : XORedCyphers.get(XORedCyphers.size() - 1))
                System.out.print((char) symbol.intValue());
            System.out.println();*/

        }


    }

    private void findKey()
    {


        for (int i = 0; i < cypherLength; i++)
        {
            key.add(new ArrayList<Integer>());
            for(int j = 0 ; j < XORedCyphers.size() ; j++)
            {
                if(XORedCyphers.get(j).get(i) == 'Y')
                {
                    if(!key.get(i).contains(cyphers.get(j).get(i) ^ ' '))
                        key.get(i).add(cyphers.get(j).get(i) ^ ' ');

                    //break;
                }


            }

        }

    }

    private void decodeCypher()
    {
        for (int j = 0; j < cyphers.size(); j++)
        {
            for (int i = 0; i < key.size(); i++) {
                if (key.get(i).isEmpty())
                    System.out.print("?");
                else
                    for (int k : key.get(i)) {
                        System.out.print((char) (cyphers.get(j).get(i) ^ k));
                    }


                System.out.print(" ");
            }
            System.out.println("");
        }

        System.out.println("\n___________________________________________\n");
        for (int i = 0; i < cypherLength; i++) {
            System.out.print(i%10 + " ");
        }
        System.out.println("");

    }

    private void preciseKey(int posInKey, int cypherNum, char c)
    {
        key.set(posInKey, new ArrayList());
        key.get(posInKey).add(c ^ cyphers.get(cypherNum).get(posInKey));


    }


    private void precise()
    {
        preciseKey(0, 1 ,'W');
        preciseKey(2, 1 ,'d');
        preciseKey(7, 1 ,'n');
        preciseKey(11, 2 ,'t');
        preciseKey(13, 1 ,'h');
        preciseKey(16, 1, 'n');
        preciseKey(24, 1, 'i');
        preciseKey(28, 2, 'w');
        preciseKey(29, 2, 'e');
        preciseKey(30, 1, 's');
        preciseKey(31, 1, 'a');
        preciseKey(32, 1, 'n');
        preciseKey(34, 1, 'e');
        preciseKey(38, 3, 'i');

        decodeCypher();

    }

    public static void main(String[] args) {


        Decoder decoder = new Decoder(args[0]);

        try {
            decoder.readCyphers();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        decoder.xorToFindSpaces();
        decoder.findKey();
        /*first iteration of decoding*/
        decoder.decodeCypher();

        /*second iteration of decoding*/
        /*individually for different cyphers*/
        decoder.precise();



    }
}
