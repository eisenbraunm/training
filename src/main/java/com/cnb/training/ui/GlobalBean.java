package com.cnb.training.ui;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SessionScoped
@Named("GlobalBean")
public class GlobalBean implements Serializable {

    private Map<String, String> elements = new HashMap<>();

    @PostConstruct
    public void init() {

        fillElements();

    }
    public String getRandomQueryString() {
        return "?r=" + Math.random() * 101;
    }

    public Map<String, String> getElements() {
        return elements;
    }

    public void setElements(Map<String, String> elements) {
        this.elements = elements;
    }


    public void fillElements() {

        this.elements = new LinkedHashMap<>();
        this.elements.put("C", "C");
        this.elements.put("H", "H");
        this.elements.put("N", "N");
        this.elements.put("O", "O");
        this.elements.put("P", "P");
        this.elements.put("S", "S");
        this.elements.put("Cl", "Cl");
        this.elements.put("F", "F");
        this.elements.put("Br", "Br");
        this.elements.put("I", "I");
        this.elements.put("Ac", "Ac");
        this.elements.put("Ag", "Ag");
        this.elements.put("Al", "Al");
        this.elements.put("Am", "Am");
        this.elements.put("Ar", "Ar");
        this.elements.put("As", "As");
        this.elements.put("At", "At");
        this.elements.put("Au", "Au");
        this.elements.put("B", "B");
        this.elements.put("Ba", "Ba");
        this.elements.put("Be", "Be");
        this.elements.put("Bh", "Bh");
        this.elements.put("Bi", "Bi");
        this.elements.put("Bk", "Bk");
        this.elements.put("Br", "Br");
        this.elements.put("C", "C");
        this.elements.put("Ca", "Ca");
        this.elements.put("Cd", "Cd");
        this.elements.put("Ce", "Ce");
        this.elements.put("Cf", "Cf");
        this.elements.put("Cl", "Cl");
        this.elements.put("Cm", "Cm");
        this.elements.put("Cn", "Cn");
        this.elements.put("Co", "Co");
        this.elements.put("Cr", "Cr");
        this.elements.put("Cs", "Cs");
        this.elements.put("Cu", "Cu");
        this.elements.put("D", "D");
        this.elements.put("Db", "Db");
        this.elements.put("Ds", "Ds");
        this.elements.put("Dy", "Dy");
        this.elements.put("Er", "Er");
        this.elements.put("Es", "Es");
        this.elements.put("Eu", "Eu");
        this.elements.put("F", "F");
        this.elements.put("Fe", "Fe");
        this.elements.put("Fl", "Fl");
        this.elements.put("Fm", "Fm");
        this.elements.put("Fr", "Fr");
        this.elements.put("Ga", "Ga");
        this.elements.put("Gd", "Gd");
        this.elements.put("Ge", "Ge");
        this.elements.put("H", "H");
        this.elements.put("He", "He");
        this.elements.put("Hf", "Hf");
        this.elements.put("Hg", "Hg");
        this.elements.put("Ho", "Ho");
        this.elements.put("Hs", "Hs");
        this.elements.put("I", "I");
        this.elements.put("In", "In");
        this.elements.put("Ir", "Ir");
        this.elements.put("K", "K");
        this.elements.put("Kr", "Kr");
        this.elements.put("La", "La");
        this.elements.put("Li", "Li");
        this.elements.put("Lr", "Lr");
        this.elements.put("Lu", "Lu");
        this.elements.put("Lv", "Lv");
        this.elements.put("Mc", "Mc");
        this.elements.put("Md", "Md");
        this.elements.put("Mg", "Mg");
        this.elements.put("Mn", "Mn");
        this.elements.put("Mo", "Mo");
        this.elements.put("Mt", "Mt");
        this.elements.put("N", "N");
        this.elements.put("Na", "Na");
        this.elements.put("Nb", "Nb");
        this.elements.put("Nd", "Nd");
        this.elements.put("Ne", "Ne");
        this.elements.put("Nh", "Nh");
        this.elements.put("Ni", "Ni");
        this.elements.put("No", "No");
        this.elements.put("Np", "Np");
        this.elements.put("O", "O");
        this.elements.put("Os", "Os");
        this.elements.put("P", "P");
        this.elements.put("Pa", "Pa");
        this.elements.put("Pb", "Pb");
        this.elements.put("Pd", "Pd");
        this.elements.put("Pm", "Pm");
        this.elements.put("Po", "Po");
        this.elements.put("Pr", "Pr");
        this.elements.put("Pt", "Pt");
        this.elements.put("Pu", "Pu");
        this.elements.put("Ra", "Ra");
        this.elements.put("Rb", "Rb");
        this.elements.put("Re", "Re");
        this.elements.put("Rh", "Rh");
        this.elements.put("Rn", "Rn");
        this.elements.put("Ru", "Ru");
        this.elements.put("S", "S");
        this.elements.put("Sb", "Sb");
        this.elements.put("Sc", "Sc");
        this.elements.put("Se", "Se");
        this.elements.put("Si", "Si");
        this.elements.put("Sm", "Sm");
        this.elements.put("Sn", "Sn");
        this.elements.put("Sr", "Sr");
        this.elements.put("T", "T");
        this.elements.put("Ta", "Ta");
        this.elements.put("Tb", "Tb");
        this.elements.put("Tc", "Tc");
        this.elements.put("Te", "Te");
        this.elements.put("Th", "Th");
        this.elements.put("Ti", "Ti");
        this.elements.put("Tl", "Tl");
        this.elements.put("Tm", "Tm");
        this.elements.put("U", "U");
        this.elements.put("V", "V");
        this.elements.put("W", "W");
        this.elements.put("Xe", "Xe");
        this.elements.put("Y", "Y");
        this.elements.put("Yb", "Yb");
        this.elements.put("Zn", "Zn");
        this.elements.put("Zr", "Zr");

    }
}
