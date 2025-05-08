package com.cnb.training.structure;

import com.epam.indigo.Bingo;
import com.epam.indigo.BingoObject;
import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;

public class BingoTest {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Indigo indigo = new Indigo();
        IndigoObject mola = indigo.loadMolecule("ONc1cccc1");
        // IndigoObject mol2 = indigo.loadMoleculeFromFile("structure.mol");
        IndigoObject qmol1 = indigo.loadQueryMolecule("C1-C-C-C-1");
        //  IndigoObject qmol2 = indigo.loadQueryMoleculeFromFile("query.mol");
        IndigoObject qmol3 = indigo.loadSmarts("[N,n,O;!H0]");
        //IndigoObject qmol4 = indigo.loadSmartsFromFile("query.sma");
        //https://lifescience.opensource.epam.com/bingo/user-manual-nosql.html
        Bingo bingo_db=   Bingo.createDatabaseFile(indigo, "C:\\CNBindexing\\epam", "molecule", "");

        IndigoObject   mol1 = indigo.loadMolecule("C1CCCCC1");
        int id1 = bingo_db.insert(mol1);
        IndigoObject    mol2 = indigo.loadMolecule("C1CCNCC1");
        int id2 = bingo_db.insert(mol2);
        IndigoObject mol3 = indigo.loadMolecule("ONc1cccc1");
        int id3 = bingo_db.insert(mol3);
        IndigoObject    mol4 = indigo.loadMolecule("NC(=O)c1ccccc1");
        int id4 = bingo_db.insert(mol4);

        bingo_db.close();

        bingo_db = Bingo.loadDatabaseFile(indigo, "C:\\CNBindexing\\epam", "");


        //extact matcher

        IndigoObject  queryExact = indigo.loadMolecule("C1CCNCC1");

        BingoObject exact_matcher = bingo_db.searchExact(queryExact,"");

        while (exact_matcher.next()) {

            System.out.println("exact match id : "+exact_matcher.getCurrentId());
        }
        exact_matcher.close();


        //substructure

        IndigoObject query = indigo.loadQueryMolecule("CC");

        BingoObject sub_matcher = bingo_db.searchSub(query, "");



        while (sub_matcher.next()) {

            System.out.println("substring match id : "+sub_matcher.getCurrentId());
        }
        sub_matcher.close();

        bingo_db.close();
    }

}
